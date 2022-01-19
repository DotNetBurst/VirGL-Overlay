package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.window.WindowUtils.AttachView;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.window.NativeWindow;
import com.catfixture.virgloverlay.core.window.WindowUtils;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.services.adapters.ServiceViewAdapterItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericListAdapter;

public class OverlayPanels {
    private static final int CONTROL_PANEL_WIDTH = 300;
    private static final int CONTROL_PANEL_HEIGHT = 100;
    private static GenericListAdapter<ServiceViewAdapterItem> serviceViewAdapterItemGenericListAdapter;
    private static boolean isControlPanelCreated;
    private static NativeWindow controlPanelNativeWindow;
    private static boolean isMainOverlayMinimized = false;

    public static void CreateControlPanel(ConfigProfile cfgProfile, NativeWindow mainWindow, Context context) {
        if (isControlPanelCreated) return;
        isControlPanelCreated = true;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        controlPanelNativeWindow = WindowUtils.CreateNativeWindow(context, 0, 0, CONTROL_PANEL_WIDTH, CONTROL_PANEL_HEIGHT);
        assert controlPanelNativeWindow != null;
        controlPanelNativeWindow.SetOverlay();
        controlPanelNativeWindow.SetTransparent();
        controlPanelNativeWindow.EnableEvents();

        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setBackgroundResource(R.color.transparent);
        View.inflate(context, R.layout.overlay_panel_layout, layout);
        LinearLayout controlsView = layout.findViewById(R.id.controlsView);
        //LinearLayout servicesView = layout.findViewById(R.id.servicesView);
        controlsView.setVisibility(cfgProfile.showControlsOnTopOfOverlay ? View.VISIBLE : View.GONE);
        //servicesView.setVisibility(cfgProfile.showServicesOnTopOfOverlay ? View.VISIBLE : View.GONE);

        AppCompatButton minimize = controlsView.findViewById(R.id.minimize);
        AppCompatButton close = controlsView.findViewById(R.id.close);

        minimize.setOnClickListener(view -> {
            isMainOverlayMinimized = !isMainOverlayMinimized;
            mainWindow.SetVisibility(isMainOverlayMinimized);
        });
        close.setOnClickListener(view -> {
            //TODO THEME OR CTX ERR
            //ConfirmDialog.ShowSystem(context, "Stop server",
            //        "Do you really want to stop server and it's services?", "Stop",
            //        () -> {
            //        }, "Cancel", null);

            Log.d(APP_TAG, "Closing server");
            //IInteropService nativeThreadInterchange = new GenericInteropService();
            //nativeThreadInterchange.Connect(view.getContext(), new ServerInteropProtocol());
            //nativeThreadInterchange.Prepare(ACTION_KILL_SERVER).Send();
        });
        //ConfigureServicesPanel(servicesView);

        int screenWidth = cfgProfile.deviceWidth;
        layout.measure(0,0);
        controlPanelNativeWindow.SetDims(screenWidth - layout.getMeasuredWidth(), 0,
                layout.getMeasuredWidth(), layout.getMeasuredHeight());
        AttachView(wm, controlPanelNativeWindow, layout);
    }
    public void DestroyControlPanel(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if ( isControlPanelCreated) {
            wm.removeView(controlPanelNativeWindow.GetFirstSurface());
        }
    }
}
