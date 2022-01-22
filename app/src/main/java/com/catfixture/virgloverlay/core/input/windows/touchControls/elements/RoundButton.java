package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import android.content.Context;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;


public class RoundButton extends TouchableWindowElement {
    private final TextView label;

    public RoundButton(Context context, int id) {
        super(context, id);

        initialSize.Set(150,150);

        label = new TextView(context);
        label.setTextColor(context.getColor(R.color.white));
        label.setTextSize(20);
        addView(label);
        setBackgroundResource(R.drawable.fx_tc_rect_btn);
    }

    public void SetText(String text) {
        label.setText(text);
    }
}