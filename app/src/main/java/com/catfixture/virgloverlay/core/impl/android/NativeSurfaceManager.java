package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.overlay.RenderingOverlayFragment.ID_RENDERING_OVERLAY_FRAGMENT;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.overlay.RenderingOverlayFragment;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;

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
        return  renderingOverlayFragment.CreateSurface();
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
