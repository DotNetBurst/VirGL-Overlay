package com.catfixture.virgloverlay.core.input.overlay.utils;


import android.annotation.SuppressLint;
import android.view.MotionEvent;

import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;

public class DragAndDropHandle<T extends ITouchable & ITransformable & IDraggable> {
    public Event onPositionChanged = new Event();
    private Int2 elementStartPos;
    private Int2 startPosition;
    private boolean isDragging;
    private int snappingSize;
    private boolean snappingOn;

    @SuppressLint("ClickableViewAccessibility")
    public DragAndDropHandle(T element) {
        element.OnDown().addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            startPosition = GetPointerCoords(motionEvent);
            elementStartPos = element.GetPosition();
            isDragging = true;
        });

        element.OnMove().addObserver((observable, o) -> {
            if ( isDragging) {
                MotionEvent motionEvent = (MotionEvent) o;
                Int2 pc = GetPointerCoords(motionEvent);
                Int2 pos = elementStartPos.Add(pc.Sub(startPosition));

                if ( snappingOn) {
                    pos.x -= pos.x % snappingSize;
                    pos.y -= pos.y % snappingSize;
                }
                element.SetPosition(pos.x, pos.y);
            }
        });

        element.OnUp().addObserver((observable, motionEvent) -> {
            isDragging = false;
            onPositionChanged.notifyObservers(element.GetPosition());
        });

    }

    private Int2 GetPointerCoords(MotionEvent motionEvent) {
        return new Int2((int)motionEvent.getRawX(), (int)motionEvent.getRawY());
    }

    public void EnableSnap(int i) {
        this.snappingOn = true;
        this.snappingSize = i;
    }
}
