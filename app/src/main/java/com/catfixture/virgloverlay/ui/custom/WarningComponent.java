package com.catfixture.virgloverlay.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.catfixture.virgloverlay.R;

public class WarningComponent extends LinearLayout {
    public WarningComponent(Context context) {
        super(context);
        Create(context, "Warning!");
    }

    public WarningComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WarningComponent, 0, 0);
        String str = a.getString(R.styleable.WarningComponent_warningText);
        a.recycle();
        Create(context, str);
    }

    private void Create(Context context, String defaultText) {
        inflate(context, R.layout.warning_component, this);


        TextView tv = this.findViewById(R.id.text);
        tv.setText(defaultText);
    }
}
