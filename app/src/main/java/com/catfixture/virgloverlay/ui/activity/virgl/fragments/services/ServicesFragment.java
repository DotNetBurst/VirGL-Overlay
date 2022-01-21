package com.catfixture.virgloverlay.ui.activity.virgl.fragments.services;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_ERROR;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_IDLE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_INITIALIZING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_LOADING_NATIVE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_RUNNING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_STARTING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_CONNECTED;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_ERROR;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_IDLE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_INITIALIZING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_LISTENING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_RUNNING;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.impl.handles.IService;
import com.catfixture.virgloverlay.core.impl.ServerController;
import com.catfixture.virgloverlay.core.impl.handles.ServiceHandle;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteCallback;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.ipc.ServiceParcelable;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.services.adapters.ServiceViewAdapterItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericListAdapter;
import com.catfixture.virgloverlay.ui.utils.Utils;

import java.util.List;

@SuppressWarnings("StringBufferReplaceableByString")
public class ServicesFragment extends Fragment {
    private View view;

    private static GenericListAdapter<ServiceViewAdapterItem> servicesViewAdapter;

    private IServerRemoteCallback remoteCallback;
    private Runnable onChanged;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remoteCallback = new IServerRemoteCallback() {
            @SuppressWarnings("unused")
            @Override
            public void onStateChanged(int state, int servicesCount) {
                view.post(() -> {
                    UpdateServerState(state);
                    UpdateServiceCount(servicesCount);
                    if ( onChanged != null)
                        onChanged.run();
                });
            }

            @SuppressWarnings("unused")
            @Override
            public void onServerStopped() {
                view.post(() -> {
                    servicesViewAdapter.Flush();
                    UpdateServerState(SERVER_STATE_IDLE);
                    UpdateServiceCount(0);
                    if ( onChanged != null)
                        onChanged.run();
                });
            }

            @SuppressWarnings("unused")
            @Override
            public void onServiceCreated(int id, String name, int servicesCount) {
                view.post(() -> {
                    UpdateServiceCreate(new ServiceHandle(id, name));
                    UpdateServiceCount(servicesCount);
                });
            }

            @SuppressWarnings("unused")
            @Override
            public void onServiceDestroyed(int id) {
                view.post(() -> UpdateServiceDestroy(id));
            }

            @SuppressWarnings("unused")
            @Override
            public void onServiceChanged(int id, int state, int fd) {
                view.post(() -> UpdateServiceChange(id, state, fd));
            }
        };
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_services, container, false);

        RecyclerView servicesView = view.findViewById(R.id.servicesView);
        servicesViewAdapter = CreateServicesAdapter(R.layout.service_item, getContext());
        servicesView.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesView.setAdapter(servicesViewAdapter);

        ConfigData config = app.GetConfigData();

        View manualServicesControlsContainer = view.findViewById(R.id.manualServicesControlsContainer);
        SwitchCompat manServicesMode = view.findViewById(R.id.manualServicesMode);
        manServicesMode.setChecked(config.automaticMode);
        ServerController svCont = app.GetServerController();
        manServicesMode.setOnCheckedChangeListener((compoundButton, mode) -> {
            config.SetAutomaticMode(mode);
            svCont.EnableAutomaticMode(remoteCallback, mode);
            ToggleManualControlsPanel(manualServicesControlsContainer, mode);
            SwitchManualServicesControlButtons(svCont.IsStarted());
        });
        if (!config.HasCurrentProfile()) config.SetAutomaticMode(false);
        else svCont.EnableAutomaticMode(remoteCallback, config.automaticMode);
        ToggleManualControlsPanel(manualServicesControlsContainer, config.automaticMode);

        Utils.InitComposedButton(view, R.id.startServicesComposedButton, () -> {
            boolean res = SwitchServerState();
            SwitchManualServicesControlButtons(res);
        });
        Utils.InitComposedButton(view, R.id.stopServicesComposedButton, () -> {
            boolean res = SwitchServerState();
            SwitchManualServicesControlButtons(res);
        });

        UpdateAll();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateAll();
    }

    public void OnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    public static GenericListAdapter<ServiceViewAdapterItem> CreateServicesAdapter(int layout,Context context) {
        return new GenericListAdapter<>(layout, (item, itemView) -> {
            IService service = item.GetService();
            TextView text = itemView.findViewById(R.id.text);
            text.setText(String.format(context.getString(R.string.srv_thrd_name),service.GetThreadName()));

            TextView idText = itemView.findViewById(R.id.idText);
            idText.setText(String.format(context.getString(R.string.srv_id),service.GetId()));

            TextView fdText = itemView.findViewById(R.id.fdText);
            int fd = service.GetFD();
            fdText.setText(String.format(context.getString(R.string.srv_fd), (fd > 0 ? Integer.toString(fd) : "None")));

            String resText = "IDLE";
            int resRes = R.drawable.conn_error;
            switch (service.GetServiceState()) {
                case SERVICE_STATE_IDLE:
                    break;
                case SERVICE_STATE_INITIALIZING:
                    resText = "INITIALIZING";
                    resRes = R.drawable.conn_starting;
                    break;
                case SERVICE_STATE_LISTENING:
                    resText = "LISTENING";
                    resRes = R.drawable.conn_listening;
                    break;
                case SERVICE_STATE_CONNECTED:
                    resText = "CONNECTED";
                    resRes = R.drawable.conn_listening;
                    break;
                case SERVICE_STATE_RUNNING:
                    resText = "RUNNING";
                    resRes = R.drawable.conn_connected;
                    break;
                case SERVICE_STATE_ERROR:
                    resText = "ERROR";
                    resRes = R.drawable.conn_error;
                    break;

            }

            TextView statusText = itemView.findViewById(R.id.statusText);
            ImageView statusImage = itemView.findViewById(R.id.statusImage);
            ImageView icon = itemView.findViewById(R.id.icon);

            statusText.setText(resText);
            statusImage.setImageResource(resRes);
            icon.setColorFilter(context.getColor(service.GetServiceState() >= 1 ? R.color.serviceGreen : R.color.serviceRed));
        });
    }

    private void UpdateServiceCount(int serviceCount) {
        TextView servicesCountText = view.findViewById(R.id.servicesCountText);
        ProgressBar servicesCountProgress = view.findViewById(R.id.servicesCountProgress);
        servicesCountText.setText(new StringBuilder()
                .append("Services count : ").append(serviceCount).append("/").append(1).toString());

        servicesCountProgress.setProgress((serviceCount * 100)); //TODO
    }


    public void UpdateMainView() {
        if ( view == null) return;

        boolean profileControlsVisible = app.GetConfigData().HasCurrentProfile();
        view.findViewById(R.id.viewport).setVisibility(profileControlsVisible ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.noProfilesLabel).setVisibility(profileControlsVisible ? View.GONE : View.VISIBLE);
    }
    public void UpdateAll() {
        UpdateMainView();
        IServerRemoteService serverInfo = app.GetServerController().GetRemote();

        if (serverInfo == null) {
            UpdateServerState(0);
            UpdateServiceCount(0);
            SwitchManualServicesControlButtons(false);
        } else {
            UpdateServerState(serverInfo.GetState());
            UpdateServiceCount(serverInfo.GetServicesCount());
            servicesViewAdapter.Flush();
            List<ServiceParcelable> srvc = serverInfo.GetServices();
            for (ServiceParcelable service : srvc) {
                IService srv = new ServiceHandle(service.id, service.threadName);
                srv.SetFD(service.fd);
                srv.ChangeState(service.state);
                servicesViewAdapter.AddItem(new ServiceViewAdapterItem(srv));
            }

            SwitchManualServicesControlButtons(app.GetServerController().IsStarted());
        }
    }

    public boolean SwitchServerState() {
        return app.GetServerController().SwitchServer(remoteCallback);
    }





    private void ToggleView(View view, boolean b) {
        view.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void ToggleManualControlsPanel(View manualServicesControlsContainer, boolean b) {
        manualServicesControlsContainer.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    private void SwitchManualServicesControlButtons(boolean serverRunning) {
        ToggleView(view.findViewById(R.id.startServicesComposedButton), !serverRunning);
        ToggleView(view.findViewById(R.id.stopServicesComposedButton), serverRunning);
    }

    private void UpdateServiceCreate(IService service) {
        servicesViewAdapter.AddItem(new ServiceViewAdapterItem(service));
    }
    private void UpdateServiceDestroy(int id) {
        servicesViewAdapter.RemoveItem(id);
    }

    private void UpdateServiceChange(int id, int state, int fd) {
        IService service = servicesViewAdapter.GetItems().get(id).GetService();
        service.ChangeState(state);
        service.SetFD(fd);
        servicesViewAdapter.notifyItemChanged(id);
    }


    private void UpdateServerState(int o) {
        ImageView statusIndicatorEl = view.findViewById(R.id.serverStatusPimp);
        TextView statusTextEl = view.findViewById(R.id.serverStatusText);

        int finalRes = R.drawable.pimp_offline_ico;
        String statusText = "SERVER OFFLINE";
        switch (o) {
            case SERVER_STATE_IDLE:
                break;
            case SERVER_STATE_INITIALIZING:
                finalRes = R.drawable.pimp_launching_ico;
                statusText = "INITIALIZING...";
                break;
            case SERVER_STATE_RUNNING:
                finalRes = R.drawable.pimp_online_ico;
                statusText = "SERVER RUNNING";
                break;
            case SERVER_STATE_LOADING_NATIVE:
                finalRes = R.drawable.pimp_online_ico;
                statusText = "LOADING NATIVE...";
                break;
            case SERVER_STATE_STARTING:
                finalRes = R.drawable.pimp_online_ico;
                statusText = "STARTING...";
                break;
            case SERVER_STATE_ERROR:
                finalRes = R.drawable.pimp_offline_ico;
                statusText = "ERROR!";
                break;
        }

        statusIndicatorEl.setImageResource(finalRes);
        statusTextEl.setText(statusText);
    }
}
