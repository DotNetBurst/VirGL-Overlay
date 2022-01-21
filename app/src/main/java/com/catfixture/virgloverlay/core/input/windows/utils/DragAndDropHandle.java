package com.catfixture.virgloverlay.core.input.windows.utils;


import android.annotation.SuppressLint;
import android.view.MotionEvent;

import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.utils.math.Int2;

public class DragAndDropHandle {
    private Int2 elementStartPos;
    private Int2 startPosition;
    private boolean isDragging;

    @SuppressLint("ClickableViewAccessibility")
    public DragAndDropHandle(TouchableWindowElement element) {
        element.onDown.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            startPosition = GetPointerCoords(motionEvent);
            elementStartPos = element.GetPosition();
            isDragging = true;
        });

        element.onMove.addObserver((observable, o) -> {
            if ( isDragging) {
                MotionEvent motionEvent = (MotionEvent) o;
                Int2 pc = GetPointerCoords(motionEvent);
                element.SetPosition(elementStartPos.Add(pc.SubSelf(startPosition)));
            }
        });

        element.onUp.addObserver((observable, motionEvent) -> {
            isDragging = false;
        });

    }

    private Int2 GetPointerCoords(MotionEvent motionEvent) {
        return new Int2((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }
}
