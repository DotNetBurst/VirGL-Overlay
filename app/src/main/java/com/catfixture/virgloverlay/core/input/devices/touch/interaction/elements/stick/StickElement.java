package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.stick;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.mouseZone.MouseZoneElementEditable;
import com.catfixture.virgloverlay.core.utils.math.Int2;


public class StickElement extends TouchableWindowElement {

    public StickElement(Context context, InputTouchControlElementData data) {
        super(context, data);
        initialSize.Set(300,300);
        setBackgroundResource(R.drawable.fx_tc_circle_btn);
    }

    @Override
    public void CreateActionEvents(IInputDevice inputDevice) {
        final Int2 elSize = GetSize();

        final Int2 startClickPos = new Int2(0,0);
        onDown.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            startClickPos.Set((int) (motionEvent.getX() - elSize.x / 2.0),
                    (int) (motionEvent.getY() - elSize.y / 2.0));
        });
        onMove.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                    (int) (motionEvent.getY() - elSize.y / 2.0));

            final Int2 diff = clickPos.Sub(startClickPos)
                    .Div(1.0f / (data.sensivity));
            final float mult = 0.01f;
            inputDevice.SendMouseShift(diff.x * mult, diff.y * mult);
        });
        onUp.addObserver((observable, o) -> {
            inputDevice.SendMouseShift(0,0);
        });
    }

    @Override
    public void CreateEditorEvents() {

    }

    @Override
    public void Select(ViewGroup customContainer) {
        getBackground().setColorFilter(context.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

        StickElementEditor editable = new StickElementEditor(context, this);
        customContainer.removeAllViews();
        customContainer.addView(editable.GetRoot());
    }

    @Override
    public void Deselect() {
        getBackground().setColorFilter(null);
    }
}
