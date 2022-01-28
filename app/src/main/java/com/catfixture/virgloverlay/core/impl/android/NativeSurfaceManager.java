package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.overlay.MainControlsOverlayFragment.ID_MAIN_CONTROLS_OVERLAY;
import static com.catfixture.virgloverlay.core.overlay.RenderingOverlayFragment.ID_RENDERING_OVERLAY_FRAGMENT;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.overlay.MainControlsOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.overlay.RenderingOverlayFragment;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.ArrayList;
import java.util.List;

public class NativeSurfaceManager {
    public void Init(Context ctx) {
        app.TryGetProfile(cfgProfile -> {
            cfgProfile.navBarHeight = AndroidUtils.GetNavbarHeight(ctx);
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            Size displaySize = AndroidUtils.GetRealDisplaySize(wm);

            cfgProfile.deviceWidth = displaySize.getWidth();
            cfgProfile.deviceHeight = displaySize.getHeight();

            Log.d(APP_TAG, "Device width = " + cfgProfile.deviceWidth);
            Log.d(APP_TAG, "Device height = " + cfgProfile.deviceHeight);
            Log.d(APP_TAG, "Android navbar = " + cfgProfile.navBarHeight);
        });
    }

    private SurfaceView CreateWindow(final int x, final int y, final int width, final int height) {
        RenderingOverlayFragment renderingOverlayFragment =
                (RenderingOverlayFragment) app.GetOverlayManager().GetFragment(ID_RENDERING_OVERLAY_FRAGMENT);
        SurfaceView newSurface = renderingOverlayFragment.CreateSurface();
        return newSurface;
    }

    private void UpdateWindow(final SurfaceView surface, final int x, final int y, final int width, final int height, final int visible) {
    }

    public void DestroyWindow(final SurfaceView surface) {
        RenderingOverlayFragment renderingOverlayFragment =
                (RenderingOverlayFragment) app.GetOverlayManager().GetFragment(ID_RENDERING_OVERLAY_FRAGMENT);
        renderingOverlayFragment.DestroySurface(surface);
        Dbg.Msg("Window destroyed!");
    }
    public Surface GetSurface(SurfaceView surf) {
        return surf.getHolder().getSurface();
    }
}
