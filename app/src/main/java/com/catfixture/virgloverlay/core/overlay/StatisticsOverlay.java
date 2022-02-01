package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;

public class StatisticsOverlay implements IOverlayFragment {
    public static final int ID_STATISTICS_OVERLAY_FRAGMENT = 10005;
    private final Context context;
    private final FrameLayout container;
    private Handler handler;

    public StatisticsOverlay(Context context) {
        this.context = context;
        this.container = new FrameLayout(context);
        TableLayout ovstat = (TableLayout) View.inflate(context, R.layout.overlay_statistics, null);
        ovstat.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.container.addView(ovstat);
        handler = new Handler();
    }

    @Override
    public int GetID() {
        return ID_STATISTICS_OVERLAY_FRAGMENT;
    }

    @Override
    public ViewGroup GetContainer() {
        return container;
    }

    @Override
    public void OnFragmentShown() {

    }

    @Override
    public void OnFragmentHidden() {

    }
}
