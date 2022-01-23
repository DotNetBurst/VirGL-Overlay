package com.catfixture.virgloverlay.core.utils.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LayoutUtils {

    public static void SetMatchMatch(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
    public static void SetMatchWrap(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public static void SetWrapWrap(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public static void SetWrapWrapRelative(View view) {
        view.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public static void SetSizeRelative(View view, int x, int y) {
        view.setLayoutParams(new RelativeLayout.LayoutParams(x,y));
    }

    public static void SetRelativeLayoutPos(ViewGroup root, int x, int y) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) root.getLayoutParams();
        lp.leftMargin = x;
        lp.topMargin = y;
        root.requestLayout();
    }
}
