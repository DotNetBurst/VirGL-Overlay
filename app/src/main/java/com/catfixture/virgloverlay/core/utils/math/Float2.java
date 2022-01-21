package com.catfixture.virgloverlay.core.utils.math;

import android.view.MotionEvent;

public class Float2 {
    public float x;
    public float y;

    public Float2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Float2(MotionEvent.PointerCoords getPointerCoords) {
        this.x = getPointerCoords.x;
        this.y = getPointerCoords.y;
    }

    public Float2 Add(Float2 f) {
        return new Float2(x + f.x, y + f.y);
    }
    public Float2 AddSelf(Float2 f) {
        x += f.x;
        y += f.y;
        return this;
    }

    public Float2 Sub(Float2 f) {
        return new Float2(x - f.x, y - f.y);
    }
    public Float2 SubSelf(Float2 f) {
        x -= f.x;
        y -= f.y;
        return this;
    }

}
