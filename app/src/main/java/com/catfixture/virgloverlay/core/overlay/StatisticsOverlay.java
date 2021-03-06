package com.catfixture.virgloverlay.core.overlay;

import static com.catfixture.virgloverlay.core.AppContext.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;

public class StatisticsOverlay implements IOverlayFragment {
    public static final int ID_STATISTICS_OVERLAY_FRAGMENT = 10005;
    private final Context context;
    private final FrameLayout container;
    private final TextView fpsText;
    private final TextView ramText;
    private Handler handler;

    public StatisticsOverlay(Context context) {
        this.context = context;
        this.container = new FrameLayout(context);
        TableLayout ovstat = (TableLayout) View.inflate(context, R.layout.overlay_statistics, null);
        ovstat.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.container.addView(ovstat);
        handler = new Handler();


        fpsText = ovstat.findViewById(R.id.fpsText);
        ramText = ovstat.findViewById(R.id.ramText);

        new Thread(() -> {
            while (true) {
                handler.post(() -> {
                    fpsText.setText(GetFPS()+"");

                    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                    ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                    activityManager.getMemoryInfo(mi);
                    long availableMegs = mi.availMem / 1048576L;
                    long totalMegs = mi.totalMem / 1048576L;
                    long usedMegs = totalMegs - availableMegs;

                    ramText.setText(Long.toString(usedMegs));

                });
                ThreadUtils.Sleep(1000);
            }
        }).start();
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


    public native static int GetFPS();
}
