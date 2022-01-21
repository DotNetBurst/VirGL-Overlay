package com.catfixture.virgloverlay.ui.activity.virgl.fragments.about;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.catfixture.virgloverlay.BuildConfig;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.core.utils.android.UpdateDTO;
import com.catfixture.virgloverlay.core.utils.android.UpdateInfo;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("ALL")
public class AboutFragment extends Fragment {
    private Button update;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        Activity activity = getActivity();
        if ( activity == null) return view;


        TextView appLabel = view.findViewById(R.id.appLabel);
        appLabel.setText(new StringBuilder().append(getResources().getText(R.string.about_app_name)).append(BuildConfig.VERSION_NAME).toString());


        Button sendRequestToDevelopers = view.findViewById(R.id.sendDevRequest);
        sendRequestToDevelopers.setOnClickListener(view1 -> {
            Activity context = getActivity();
            AndroidUtils.OnPermissionGranted(context, READ_EXTERNAL_STORAGE, () -> DevRequestHelper.SendDevRequest(context), null);
        });

        String mainRepo = "https://virgl.000webhostapp.com/vget.php";
        String apkPath = activity.getFilesDir().getPath() + "/update.apk";

        update = view.findViewById(R.id.update);
        update.setOnClickListener((btn) -> {
            update.setEnabled(false);
            if ( updateInfo == null) {
                AndroidUtils.RequestInstallationPermission(activity);
                update.setText(R.string.rtv_text_lab);
                CheckForUpdates(mainRepo, (updateInfo) -> view.post(() -> {
                    this.updateInfo = updateInfo;
                    UpdateUpdatesButtonUI(updateInfo, update);
                    update.setEnabled(true);
                }));
            } else {
                boolean hasUpdates = BuildConfig.VERSION_CODE < updateInfo.versionCode;
                if ( hasUpdates) {
                    ConfirmDialog.Show(activity, "New version available",
                            "Version " + updateInfo.versionName + "", "Update", () -> {
                        update.setText(R.string.dwn_text_lab);
                        UpdateNow((response) -> view.post(() -> {
                            if (response.code() == 200) {
                                update.setText(R.string.save_text_lab);
                                try {
                                    AndroidUtils.WriteFile(apkPath, response.body().bytes());
                                } catch (IOException e) {
                                    Dbg.Error(e);
                                    SetFinalState("Can't write file");
                                }

                                update.setText(R.string.installing_text_lab);
                                AndroidUtils.OnPermissionGranted(activity, READ_EXTERNAL_STORAGE, () -> AndroidUtils.InstallUpdate((AppCompatActivity) activity, apkPath, () -> SetFinalState("Up to date"), () -> SetFinalState("Not installed!")), () -> SetFinalState("No permissions!"));
                            } else {
                                Dbg.Error("Error no app on server");
                                SetFinalState("Error no app on server");
                            }
                        }), () -> view.post(() -> SetFinalState("Server dead...")));
                    }, "Cancel", () -> SetFinalState("Check for updates"));
                } else {
                    SetFinalState("Up to date");
                }
            }
        });



        return view;
    }


    private void SetFinalState(String text) {
        update.setText(text);
        update.setEnabled(true);
        updateInfo = null;
    }

    private void UpdateNow(Action<Response> onDownloaded, Runnable onError) {
        String mainRepo = "https://virgl.000webhostapp.com/app.json";
        Get(mainRepo, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                onError.run();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                onDownloaded.Invoke(response);
            }
        });
    }


    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();
    private UpdateInfo updateInfo;


    void Get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
    private void CheckForUpdates(String url, Action<UpdateInfo> onDone) {
        Get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                onDone.Invoke(new UpdateInfo("-1", -1, null));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                UpdateDTO updateDTO = gson.fromJson(Objects.requireNonNull(response.body()).string(), UpdateDTO.class);
                onDone.Invoke(new UpdateInfo(updateDTO.vname, updateDTO.vcode, updateDTO.changelog));
            }
        });
    }

    private void UpdateUpdatesButtonUI (UpdateInfo updateInfo, Button update) {
        if ( updateInfo == null) {
            update.setText("CHECK FOR UPDATES");
        } else {
            if ( updateInfo.versionCode == -1) {
                update.setText("Error server dead!");
            } else {
                boolean hasUpdates = BuildConfig.VERSION_CODE < updateInfo.versionCode;
                update.setText(hasUpdates ? "UPDATE TO (" + updateInfo.versionName + ")" : "UP TO DATE!");
            }
        }
    }
}
