package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;


public class CrossButton extends TouchableWindowElement {

    public CrossButton(Context context) {
        super(context);
        setBackgroundResource(R.drawable.fx_tc_cross_btn);
    }
}