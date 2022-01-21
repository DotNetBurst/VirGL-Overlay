package com.catfixture.virgloverlay.core.input.windows;


import com.catfixture.virgloverlay.core.utils.math.Int2;

public interface IInputWindowElement {
    IInputWindowElement SetAlpha(float v);
    IInputWindowElement SetSize(Int2 size);
    IInputWindowElement SetPosition(Int2 pos);

    Int2 GetPosition();
}
