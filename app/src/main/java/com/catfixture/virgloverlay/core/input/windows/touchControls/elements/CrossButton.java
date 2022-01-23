package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TouchableWindowElement;


public class CrossButton extends TouchableWindowElement {

    public CrossButton(Context context, int id) {
        super(context, id);

        initialSize.Set(300,300);

        setBackgroundResource(R.drawable.fx_tc_cross_btn);
    }
}