package com.catfixture.virgloverlay.core.input.utils;


import android.view.ViewGroup;

import com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.IEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
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

    void Select(ViewGroup customContainer);
    void Deselect();

    void Reinflate();
}
