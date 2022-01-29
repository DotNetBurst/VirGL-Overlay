package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;

public class MouseZoneElement extends TouchableWindowElement {

    public MouseZoneElement(Context context, int id) {
        super(context, id);
        initialSize.Set(400,300);
        setBackgroundResource(R.drawable.fx_tc_rect_rnd_btn);
    }
}
