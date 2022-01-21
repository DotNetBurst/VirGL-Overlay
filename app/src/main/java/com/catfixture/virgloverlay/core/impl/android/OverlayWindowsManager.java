package com.catfixture.virgloverlay.core.impl.android;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.appcompat.view.menu.MenuWrapperICS;

import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.ArrayList;
import java.util.List;

public class OverlayWindowsManager {
    private Handler handler;
    private Context ctx;
    private IServerRemoteService serverRemoteService;
    private List<IWindow> windows = new ArrayList<>();

    @SuppressWarnings("unused")
    public void Init(Context ctx, IServerRemoteService serverRemoteService) {
        this.ctx = ctx;
        this.serverRemoteService = serverRemoteService;
        handler = new Handler();

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

    IWindow window;
    @SuppressWarnings("unused")
    private SurfaceView CreateWindow(final int x, final int y, final int width, final int height) {
        Thread mutex = Thread.currentThread();
        handler.post(() -> {
            window = new AndroidWindow(ctx);
            window.CreateSurfaceViewContainer()
                    .SetOverlay()
                    .SetPosition(0,0)
                    .SetSize(32,32)
                    .Attach();
            windows.add(window);
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
        Log.d(APP_TAG,"SURF " + window.GetContainer());
        return window.GetContainer();

    }


    @SuppressWarnings("unused")
    private void UpdateWindow(final SurfaceView surface, final int x, final int y, final int width, final int height, final int visible) {
        handler.post(() -> {
            try {
                if( visible != 0 ) {
                    window.SetPosition(x,y);
                    window.SetSize(width,height);
                } else {
                    Log.d(APP_TAG,"Invisible! ");
                }
            } catch(Exception e) {
                Dbg.Error(e);
            }

            //app.TryGetProfile(cfgProfile -> {
            //    if (cfgProfile.showControlsOnTopOfOverlay)
            //        OverlayPanels.CreateControlPanel(cfgProfile, nw, surface.getContext(), serverRemoteService);
            //});
        });
    }

    @SuppressWarnings("unused")
    public void DestroyWindow(final SurfaceView surface) {
        handler.post(() -> {
            window.Detach();
            Dbg.Msg("Window destroyed!");
        });
    }
    public Surface GetSurface(SurfaceView surf)
    {
        return surf.getHolder().getSurface();
    }
}
