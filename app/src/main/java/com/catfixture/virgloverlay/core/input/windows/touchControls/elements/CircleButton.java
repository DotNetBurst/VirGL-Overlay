package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import android.content.Context;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;


public class CircleButton extends TouchableWindowElement {
    private final TextView label;

    public CircleButton(Context context) {
        super(context);

        label = new TextView(context);
        label.setTextColor(context.getResources().getColor(R.color.white));
        label.setTextSize(20);
        addView(label);
        setBackgroundResource(R.drawable.fx_tc_circle_btn);
    }

    public void SetText(String text) {
        label.setText(text);
    }
}