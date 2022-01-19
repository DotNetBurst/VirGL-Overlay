package com.catfixture.virgloverlay.ui.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.catfixture.virgloverlay.R;

public class Utils {
    public static void InitToolbarText(Toolbar toolbar) {
        TextView toolbarTitlePart0 = toolbar.findViewById(R.id.toolbar_titlePart0);
        toolbarTitlePart0.setText(R.string.app_name_p1);

        Utils.ApplyVertGradient(toolbarTitlePart0, "#ffffff", "#d0d0d0");

        TextView toolbarTitlePartGL = toolbar.findViewById(R.id.toolbar_titlePartGL);
        toolbarTitlePartGL.setText(R.string.app_name_p2);
        toolbarTitlePartGL.setTextColor(Color.RED);

        TextView toolbarTitlePart1 = toolbar.findViewById(R.id.toolbar_titlePart1);
        toolbarTitlePart1.setText(R.string.app_name_p3);
        Utils.ApplyVertGradient(toolbarTitlePart1, "#ffffff", "#d0d0d0");
    }

    public static void ApplyVertGradient(TextView tv, String s, String s1) {
        float textHeight = tv.getTextSize();
        Shader textShader = new LinearGradient(0, 0, 0, textHeight,
                new int[]{
                        Color.parseColor(s),
                        Color.parseColor(s1)}, null, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(textShader);
    }

    public static void InitComposedButton(View view, int id, Runnable action) {
        View btnCont = view.findViewById(id);
        Button button = btnCont.findViewById(R.id.button);
        button.setOnClickListener(view1 -> action.run());
    }
}
