package com.catfixture.virgloverlay.core.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

public class NativeWindow {
    private int LAYOUT_FLAG;
    private int x;
    private int y;
    private int w;
    private int h;
    private final SurfaceView[] surf = new SurfaceView[1];
    private int EVENTS_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    private int pixelFormat = PixelFormat.OPAQUE;

    public NativeWindow(Context context, int x, int y, int w, int h) {
        surf[0] = new SurfaceView(context);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public SurfaceView GetFirstSurface() {
        return surf[0];
    }


    public void SetOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
    }

    public void SetDims( int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public ViewGroup.LayoutParams GetParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                EVENTS_FLAG,
                pixelFormat);

        params.gravity = Gravity.START | Gravity.TOP;
        params.x = x;
        params.y = y;
        params.width = w;
        params.height = h;

        return params;
    }

    public void EnableEvents() {
        EVENTS_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
    }

    public void SetVisibility (boolean vis) {
        //NEED DRIVER REINIT, RAISES Adreno ERROR!
        //mainOverlay.setVisibility(isMainOverlayMinimized ? View.GONE : View.VISIBLE);

        //STUPID WORKAROUND, BUT WORK WELL WITH NOT FOCUSABLE EVENT-TRANSPARENT WINDOWS.
        SurfaceView mainOverlay = GetFirstSurface();
        int mag = vis ? 1 : 0;
        mainOverlay.setScaleX(mag);
        mainOverlay.setScaleY(mag);
    }

    public void SetTransparent() {
        pixelFormat = PixelFormat.TRANSPARENT;
    }
    public void SetTranslucent() {
        pixelFormat = PixelFormat.TRANSLUCENT;
    }
}
