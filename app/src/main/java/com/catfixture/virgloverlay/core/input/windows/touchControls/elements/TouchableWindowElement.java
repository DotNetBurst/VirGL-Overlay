package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.core.utils.objProvider.ITypedProvider;
import com.catfixture.virgloverlay.core.utils.types.Event;


public class TouchableWindowElement extends LinearLayout implements IInputWindowElement {
    private RelativeLayout.LayoutParams lp;
    private Int2 pos = Int2.Zero;
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();

    public TouchableWindowElement(Context context) {
        super(context);
        Init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Init() {
        lp = new RelativeLayout.LayoutParams(0,0);
        lp.leftMargin = 0;
        lp.topMargin = 0;
        setGravity(Gravity.CENTER);

        Int2 clickPos = Int2.Zero;
        setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    onDown.notifyObservers(motionEvent);
                    clickPos.Set(motionEvent.getRawX(), motionEvent.getRawY());
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    onMove.notifyObservers(motionEvent);
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    onUp.notifyObservers(motionEvent);
                    if ( clickPos.Distance(motionEvent.getRawX(), motionEvent.getRawY()) < 5) {
                        onClick.notifyObservers(motionEvent);
                    }
                    return true;
                }
            }
            return false;
        });
    }

    public TouchableWindowElement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableWindowElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public IInputWindowElement SetAlpha(float v) {
        setAlpha(v);
        return this;
    }

    @Override
    public IInputWindowElement SetSize(Int2 size) {
        lp.width = size.x;
        lp.height =  size.y;
        setLayoutParams(lp);
        return this;
    }

    @Override
    public IInputWindowElement SetPosition(Int2 pos) {
        this.pos = pos;
        lp.leftMargin = pos.x;
        lp.topMargin = pos.y;
        setLayoutParams(lp);
        return this;
    }

    @Override
    public Int2 GetPosition() {
        return pos;
    }

    protected void Save() {

    }
}
