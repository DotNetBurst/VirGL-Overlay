package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.catfixture.virgloverlay.core.android.AndroidUtils;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.window.NativeWindow;
import com.catfixture.virgloverlay.core.window.WindowUtils;

public class Windows {
    private static WindowManager wm;
    private static Handler handler;
    private static Context ctx;

    private static int GetNavbarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @SuppressWarnings("unused")
    public static void Init(Context _ctx) {
        ctx = _ctx;

        handler = new Handler();
        wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        app.TryGetProfile(cfgProfile -> {
            cfgProfile.navBarHeight = GetNavbarHeight(ctx);
            Size displaySize = AndroidUtils.GetRealDisplaySize(wm);

            cfgProfile.deviceWidth = displaySize.getWidth();
            cfgProfile.deviceHeight = displaySize.getHeight();

            Log.d(APP_TAG, "Device width = " + cfgProfile.deviceWidth);
            Log.d(APP_TAG, "Device height = " + cfgProfile.deviceHeight);
            Log.d(APP_TAG, "Android navbar = " + cfgProfile.navBarHeight);
        });
    }

    //Terrible...
    static NativeWindow nw;
    @SuppressWarnings("unused")
    private static SurfaceView CreateWindow(final int x, final int y, final int width, final int height) {
        Thread mutex = Thread.currentThread();
        handler.post(() -> {
            nw = WindowUtils.CreateNativeWindow(ctx, 0,0,32,32);
            assert nw != null;
            nw.SetOverlay();
            WindowUtils.AttachSurface(wm, nw);
            synchronized (mutex) {mutex.notify();}
        });

        synchronized (mutex) {
            try {
                mutex.wait();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Dbg.Error(e);
            }
        }
        Log.d(APP_TAG,"SURF " + nw.GetFirstSurface().toString());
        return nw.GetFirstSurface();

    }


    @SuppressWarnings("unused")
    private static void UpdateWindow(final SurfaceView surface, final int x, final int y, final int width, final int height, final int visible) {
        handler.post(() -> {
            try {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams)surface.getLayoutParams();

                if( params == null ) return;
                if( visible != 0 ) {
                    params.x = x;
                    params.y = y;
                    params.width = width;
                    params.height = height;
                } else {
                    Log.d(APP_TAG,"Invisible! ");
                }
                wm.updateViewLayout(surface, params);
            } catch(Exception e) {
                Dbg.Error(e);
            }

            app.TryGetProfile(cfgProfile -> {
                if (cfgProfile.showControlsOnTopOfOverlay)
                    OverlayPanels.CreateControlPanel(cfgProfile, nw, surface.getContext());
            });
        });
    }

    @SuppressWarnings("unused")
    public static void DestroyWindow(final SurfaceView surface) {
        handler.post(() -> {
            wm.removeView(surface);
            Dbg.Msg("Window destroyed!");
        });
    }
    public static Surface GetSurface(SurfaceView surf)
    {
        return surf.getHolder().getSurface();
    }
}
