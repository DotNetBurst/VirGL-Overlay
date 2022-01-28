package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;

public class RenderingOverlayFragment implements IOverlayFragment {
    public static final int ID_RENDERING_OVERLAY_FRAGMENT = 10004;
    private final Context context;
    private final FrameLayout container;
    private SurfaceView surfaceView;
    private Handler handler;

    public RenderingOverlayFragment(Context context) {
        this.context = context;
        this.container = new FrameLayout(context);
        handler = new Handler();
    }

    @Override
    public int GetID() {
        return ID_RENDERING_OVERLAY_FRAGMENT;
    }

    @Override
    public ViewGroup GetContainer() {
        return container;
    }

    public SurfaceView CreateSurface() {
        ThreadUtils.LockThreadUntilUITask(handler, (mutex) -> {
            surfaceView = new SurfaceView(context);
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    Dbg.Error("Surface ready!\n");
                    synchronized (mutex) { mutex.notifyAll(); }
                }
                @Override public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) { }
                @Override public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}
            });
            container.addView(surfaceView);
        });
        return surfaceView;
    }

    public void DestroySurface(SurfaceView surface) {
        ThreadUtils.LockThreadUntilUITask(handler, (mutex) -> {
            surface.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) { }
                @Override public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) { }
                @Override public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    Dbg.Error("Surface destroyed!\n");
                    synchronized (mutex) { mutex.notifyAll(); }
                }
            });
            container.removeView(surface);
        });
    }
}
