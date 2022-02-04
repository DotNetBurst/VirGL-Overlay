package com.catfixture.virgloverlay.core.input.utils;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

import android.annotation.SuppressLint;
import android.view.View;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.BooleanHolder;
import com.catfixture.virgloverlay.core.utils.math.Int2;

import java.lang.ref.WeakReference;

public class EventUtils {

    @SuppressLint("ClickableViewAccessibility")
    public static void InitializeITouchableEvents(View root, ITouchable touchable) {
        Int2 clickPos = new Int2(0,0);
        root.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case ACTION_DOWN:
                case ACTION_POINTER_DOWN:
                    touchable.OnDown().notifyObservers(motionEvent);
                    clickPos.Set(motionEvent.getRawX(), motionEvent.getRawY());
                    return true;
                case ACTION_MOVE:
                    touchable.OnMove().notifyObservers(motionEvent);
                    return true;
                case ACTION_POINTER_UP:
                case ACTION_UP:
                    touchable.OnUp().notifyObservers(motionEvent);
                    if ( clickPos.Distance(motionEvent.getRawX(), motionEvent.getRawY()) < 5) {
                        touchable.OnClick().notifyObservers(motionEvent);
                    }
                    return true;
            }
            return false;
        });
    }
}
