package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import static com.catfixture.virgloverlay.core.input.overlay.touchControls.TouchControlHelper.CreateButtonLabel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;


public class RoundedButton extends TouchableWindowElement {
    private final TextView label;

    public RoundedButton(Context context, int id) {
        super(context, id);

        initialSize.Set(150,150);

        label = CreateButtonLabel(context);
        addView(label);

        setBackgroundResource(R.drawable.fx_tc_rect_rnd_btn);
    }

    public void SetText(String text) {
        label.setText(text);
    }
}