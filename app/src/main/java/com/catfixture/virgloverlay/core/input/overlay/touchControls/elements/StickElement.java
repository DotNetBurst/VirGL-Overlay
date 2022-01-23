package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;


public class StickElement extends TouchableWindowElement {

    public StickElement(Context context, int id) {
        super(context, id);

        initialSize.Set(300,300);
        setBackgroundResource(R.drawable.fx_tc_circle_btn);
    }
}
