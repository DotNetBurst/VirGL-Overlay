package com.catfixture.virgloverlay.core.input.overlay;


import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.utils.math.Int2;

public interface IInputWindowElement {
    int GetId();

    IInputWindowElement SetAlpha(float v);
    IInputWindowElement SetScale(int i);
    IInputWindowElement SetSize(int x, int y);
    IInputWindowElement SetInitialSize(int x, int y);

    Int2 GetPosition();
    Object GetData();

    TouchableWindowElement GetHandle();
    void SetHandle(TouchableWindowElement handle);
}
