package com.catfixture.virgloverlay.core.input.windows;


import android.widget.LinearLayout;

import com.catfixture.virgloverlay.core.utils.math.Int2;

public interface IInputWindowElement {
    int GetId();

    IInputWindowElement SetAlpha(float v);
    IInputWindowElement SetScale(int i);
    IInputWindowElement SetSize(int x, int y);
    IInputWindowElement SetPosition(int x, int y);

    Int2 GetPosition();
    Object GetData();
}
