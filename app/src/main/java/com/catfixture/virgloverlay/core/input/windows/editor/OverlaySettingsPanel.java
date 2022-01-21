package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.View.MeasureSpec.UNSPECIFIED;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.animations.ResizeWidthAnimation;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;


public class OverlaySettingsPanel extends TouchableWindowElement {
    private final int minimizedWidth;
    private final ImageView settingsIco;
    private ViewGroup panel;
    private boolean isExpanded;

    public OverlaySettingsPanel(Context context, ViewGroup viewGroup) {
        super(context);

        panel = (ViewGroup) View.inflate(context, R.layout.overlay_settings_panel, null);
        panel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(panel);
        View subPanel = panel.findViewById(R.id.subPanel);

        settingsIco = panel.findViewById(R.id.settingsIco);

        settingsIco.setOnClickListener((o) -> {
            isExpanded = !isExpanded;
            SetExpanded(isExpanded);
        });

        //new DragAndDropHandle(this);
        viewGroup.addView(this);

        panel.measure(UNSPECIFIED, UNSPECIFIED);
        minimizedWidth = panel.getMeasuredWidth();
    }

    private void SetExpanded(boolean is) {
        panel.clearAnimation();
        if ( is) {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(panel, 700);
            anim.setDuration(200);
            settingsIco.setImageResource(R.drawable.fx_ov_settings_active);
            panel.startAnimation(anim);
        } else {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(panel, minimizedWidth);
            anim.setDuration(200);
            settingsIco.setImageResource(R.drawable.fx_ov_settings);
            panel.startAnimation(anim);
        }
    }
}
