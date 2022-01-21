package com.catfixture.virgloverlay.core.input.windows.utils;


import android.annotation.SuppressLint;
import android.view.MotionEvent;

import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;

public class DragAndDropHandle {
    public Event onPositionChanged = new Event();
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
                Int2 pos = elementStartPos.Add(pc.Sub(startPosition));
                element.SetPosition(pos.x, pos.y);
            }
        });

        element.onUp.addObserver((observable, motionEvent) -> {
            isDragging = false;
            if ( onPositionChanged != null)
                onPositionChanged.notifyObservers(element.GetPosition());
        });

    }

    private Int2 GetPointerCoords(MotionEvent motionEvent) {
        return new Int2((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }
}
