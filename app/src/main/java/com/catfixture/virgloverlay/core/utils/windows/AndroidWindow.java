package com.catfixture.virgloverlay.core.utils.windows;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.Size;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;

public class AndroidWindow implements IWindow {
    private int x,y,w,h;
    private int LAYOUT_FLAG,
                EVENTS_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PIXEL_FORMAT = PixelFormat.OPAQUE;
    private ViewGroup container;
    private WindowManager winMan;
    private Context context;
    private float alpha;
    private boolean isAttached;

    public AndroidWindow(Context context, int x, int y, int w, int h) {
        this(context);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public AndroidWindow(Context context) {
        this.context = context;
        winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public IWindow SetFullscreen() {
        SetSize(MATCH_PARENT, MATCH_PARENT);
        return this;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static Size GetRealDisplaySize (WindowManager windowManager) {
        Point size = new Point();
        Display defDisp = windowManager.getDefaultDisplay();
        defDisp.getRealSize(size);

        if (size.x < size.y) {
            int sizeX = size.x;
            size.x = size.y;
            size.y = sizeX;
        }


        return new Size(size.x, size.y);
    }

    @Override
    public IWindow SetShirkWidthOnBottomSide() {
        boolean isHorizontal = GetOrientation();

        Size realDisplaySize = GetRealDisplaySize(winMan);
        int windowHeight = 550;

        SetSize(realDisplaySize.getHeight(),windowHeight);
        SetPosition(0, realDisplaySize.getWidth() + windowHeight);
        return this;
    }

    private boolean GetOrientation() {
        return false;
    }

    @Override
    public IWindow SetShirkWidthOnTopSide() {

        return this;
    }
    @Override
    public IWindow SetShirkHeightOnLeftSide() {
        return this;

    }
    @Override
    public IWindow SetShirkHeightOnRightSide() {

        return this;
    }

    @Override
    public IWindow CreateSurfaceViewContainer() {
        CreateLinearLayoutContainer();
        SurfaceView surf = new SurfaceView(context);
        LayoutUtils.SetMatchMatch(surf);
        container.addView(surf);

        return this;
    }
    @Override
    public IWindow CreateLinearLayoutContainer() {
        container = new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT));
        return this;
    }
    @Override
    public IWindow CreateRelativeLayoutContainer() {
        container = new RelativeLayout(context);
        container.setLayoutParams(new RelativeLayout.LayoutParams(
                MATCH_PARENT,
                MATCH_PARENT));
        return this;
    }

    @Override
    public IWindow SetPosition(int x, int y) {
        this.x = x;
        this.y = y;
        UpdateLayoutInternal();
        return this;
    }

    @Override
    public IWindow SetAlpha(float alpha) {
        this.alpha = alpha;
        container.setAlpha(alpha);
        return this;
    }

    @Override
    public IWindow SetSize(int w, int h) {
        this.w = w;
        this.h = h;
        UpdateLayoutInternal();
        return this;
    }

    @Override
    public IWindow SetSizeByContainer() {
        container.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        SetSize(container.getMeasuredWidth(), container.getMeasuredHeight());
        return this;
    }

    @Override
    public IWindow SetHeightByContainer() {
        container.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        SetSize(w, container.getMeasuredHeight());
        return this;
    }

    @Override
    public IWindow SetTranlucent() {
        PIXEL_FORMAT = PixelFormat.TRANSLUCENT;
        return this;
    }

    @Override
    public IWindow Attach() {
        winMan.addView(container, GetParams());
        isAttached = true;
        return this;
    }

    @Override
    public IWindow Detach() {
        try {
            winMan.removeView(container);
            isAttached = false;
        } catch (Exception x) {
            x.printStackTrace();
        }
        return this;
    }


    @Override
    public ViewGroup GetContainer() {
        return container;
    }

    @Override
    public IWindow SetOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return this;
    }

    @Override
    public IWindow SetVisibility (boolean vis) {
        int mag = vis ? 1 : 0;
        container.setScaleX(mag);
        container.setScaleY(mag);
        return this;
    }

    @Override
    public IWindow EnableEvents() {
        EVENTS_FLAG = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        return this;
    }

    @Override
    public AndroidWindow SetContainer(ViewGroup container) {
        this.container = container;
        return this;
    }


    private void UpdateLayoutInternal() {
        if ( isAttached) winMan.updateViewLayout(container, GetParams());
    }


    public ViewGroup.LayoutParams GetParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                EVENTS_FLAG,
                PIXEL_FORMAT);

        params.gravity = Gravity.START | Gravity.TOP;
        params.x = x;
        params.y = y;
        params.width = w;
        params.height = h;

        return params;
    }
}
