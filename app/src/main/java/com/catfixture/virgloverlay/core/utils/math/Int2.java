package com.catfixture.virgloverlay.core.utils.math;

import android.view.MotionEvent;

public class Int2 {
    public static final Int2 Zero = new Int2(0,0);
    public int x;
    public int y;

    public Int2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Int2(MotionEvent.PointerCoords getPointerCoords) {
        this.x = (int) getPointerCoords.x;
        this.y = (int) getPointerCoords.y;
    }

    public Int2 Add(Int2 f) {
        return new Int2(x + f.x, y + f.y);
    }
    public Int2 AddSelf(Int2 f) {
        x += f.x;
        y += f.y;
        return this;
    }

    public Int2 Sub(Int2 f) {
        return new Int2(x - f.x, y - f.y);
    }
    public Int2 SubSelf(Int2 f) {
        x -= f.x;
        y -= f.y;
        return this;
    }

    public void Set(float rawX, float rawY) {
        this.x = (int) rawX;
        this.y = (int) rawY;
    }

    public int Distance (Int2 other) {
        int xm = other.x-x;
        int ym = other.y-y;
        return (int) Math.sqrt(xm*xm + ym*ym);
    }
    public int Distance (int _x, int _y) {
        int xm = _x-x;
        int ym = _y-y;
        return (int) Math.sqrt(xm*xm + ym*ym);
    }
    public int Distance (float _x, float _y) {
        return Distance((int)_x,(int)_y);
    }

    public float Dot(Int2 vec) {
        float length = Distance(this.Sub(vec));
        if ( length <= 0) return 0;

        float dot = x * vec.x + y * vec.y;
        return dot / length;
    }
}
