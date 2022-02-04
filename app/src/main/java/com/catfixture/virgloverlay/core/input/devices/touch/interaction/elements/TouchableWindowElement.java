package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.catfixture.virgloverlay.core.AppContext.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.IEditable;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.utils.EventUtils;
import com.catfixture.virgloverlay.core.input.utils.IDraggable;
import com.catfixture.virgloverlay.core.input.utils.ITouchable;
import com.catfixture.virgloverlay.core.input.utils.ITransformable;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;


public abstract class TouchableWindowElement extends LinearLayout implements IInputWindowElement, ITouchable, IDraggable, ITransformable {
    protected Context context;
    private RelativeLayout.LayoutParams lp;
    private Int2 pos = new Int2(0,0);
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();
    protected InputTouchControlElementData data;
    protected Int2 initialSize = new Int2(100,100);
    private TouchableWindowElement handle;
    Runnable reinflate;
    private Runnable editorReset;

    public TouchableWindowElement(Context context, InputTouchControlElementData data) {
        super(context);
        this.context = context;
        this.data = data;
        Init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Init() {
        lp = new RelativeLayout.LayoutParams(0,0);
        lp.leftMargin = 0;
        lp.topMargin = 0;
        setGravity(Gravity.CENTER);

        EventUtils.InitializeITouchableEvents(this, this);
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
        return data.id;
    }

    @Override
    public IInputWindowElement SetAlpha(float v) {
        v *= app.GetInputConfigData().uiOpacity;
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
    public IInputWindowElement SetInitialSize(int x, int y) {
        initialSize.Set(x,y);
        return this;
    }



    @Override
    public void SetPosition(int x, int y) {
        lp.leftMargin = x;
        lp.topMargin = y;
        setLayoutParams(lp);
    }

    @Override
    public Int2 GetPosition() {
        return new Int2(lp.leftMargin,lp.topMargin);
    }
    @Override
    public Int2 GetSize() {
        return new Int2(lp.width,lp.height);
    }

    protected void Save() {
    }

    @Override
    public Object GetData() {
        return data;
    }

    @Override
    public TouchableWindowElement GetHandle() {
        return handle;
    }

    @Override
    public void SetHandle(TouchableWindowElement handle) {
        this.handle = handle;
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

    public abstract void CreateActionEvents(IInputDevice inputDevice);
    public abstract void CreateEditorEvents();
    public void SetReinflate(Runnable reinflate) {
        this.reinflate = reinflate;
    }

    @Override
    public void Reinflate() {
        reinflate.run();
    }
    public void SetEditorReset(Runnable editorReset) { this.editorReset = editorReset;};

    @Override
    public void ResetEditor() {
        editorReset.run();
    }
}
