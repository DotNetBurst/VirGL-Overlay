package com.catfixture.virgloverlay.core.input.windows.touchControls.elements;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.IDraggable;
import com.catfixture.virgloverlay.core.input.windows.utils.ITouchable;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;


public class TouchableWindowElement extends LinearLayout implements IInputWindowElement, ITouchable, IDraggable {
    public int id;
    private RelativeLayout.LayoutParams lp;
    private Int2 pos = new Int2(0,0);
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();
    private Object customData;
    protected Int2 initialSize = new Int2(100,100);

    public TouchableWindowElement(Context context, int id) {
        super(context);
        this.id = id;
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

        SetSize(WRAP_CONTENT, WRAP_CONTENT);
    }

    public TouchableWindowElement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableWindowElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int GetId() {
        return id;
    }

    @Override
    public IInputWindowElement SetAlpha(float v) {
        setAlpha(v);
        return this;
    }

    @Override
    public IInputWindowElement SetScale(int i) {
        float scale = i / 100.0f;
        SetSize((int) (initialSize.x * scale), (int) (initialSize.y * scale));
        return this;
    }

    @Override
    public IInputWindowElement SetSize(int x, int y) {
        lp.width = x;
        lp.height = y;
        setLayoutParams(lp);
        return this;
    }

    @Override
    public IInputWindowElement SetPosition(int x, int y) {
        lp.leftMargin = x;
        lp.topMargin = y;
        setLayoutParams(lp);
        return this;
    }

    @Override
    public Int2 GetPosition() {
        return new Int2(lp.leftMargin,lp.topMargin);
    }

    protected void Save() {
    }

    @Override
    public Object GetData() {
        return customData;
    }


    public void SetCustomData(InputTouchControlElement touchControlElement) {
        this.customData = touchControlElement;
    }

    @Override
    public Event OnDown() {
        return onDown;
    }

    @Override
    public Event OnMove() {
        return onMove;
    }

    @Override
    public Event OnUp() {
        return onUp;
    }

    @Override
    public Event OnClick() {
        return onClick;
    }
}
