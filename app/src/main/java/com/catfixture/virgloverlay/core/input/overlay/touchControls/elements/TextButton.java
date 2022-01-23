package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import static com.catfixture.virgloverlay.core.input.overlay.touchControls.TouchControlHelper.CreateButtonLabel;

import android.content.Context;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;

public class TextButton extends TouchableWindowElement {
    private final TextView label;

    public TextButton(Context context, int id, int layout) {
        super(context, id);

        initialSize.Set(150,150);

        label = CreateButtonLabel(context);
        addView(label);

        setBackgroundResource(layout);
    }

    public void SetText(String text) {
        label.setText(text);
    }
}

