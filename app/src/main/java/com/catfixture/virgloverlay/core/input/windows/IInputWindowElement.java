package com.catfixture.virgloverlay.core.input.windows;


import com.catfixture.virgloverlay.core.utils.math.Int2;

public interface IInputWindowElement {
    IInputWindowElement SetAlpha(float v);
    IInputWindowElement SetScale(int i);
    IInputWindowElement SetSize(int x, int y);
    IInputWindowElement SetPosition(int x, int y);

    Int2 GetPosition();

    void Select();
    void Deselect();

    Object GetData();
}
