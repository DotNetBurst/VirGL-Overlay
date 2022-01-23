package com.catfixture.virgloverlay.core.input.overlay.touchControls;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;

public class TouchControlHelper {
    public static TextView CreateButtonLabel (Context context) {
        TextView label = new TextView(context);
        label.setTextColor(context.getColor(R.color.white));
        label.setTextSize(18);
        label.setSingleLine(false);
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setLines(1);

        return label;
    }
}
