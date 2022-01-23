package com.catfixture.virgloverlay.core.input.overlay.utils;

import com.catfixture.virgloverlay.core.utils.math.Int2;

public interface ITransformable {
    Int2 GetPosition();
    void SetPosition(int x, int y);
}
