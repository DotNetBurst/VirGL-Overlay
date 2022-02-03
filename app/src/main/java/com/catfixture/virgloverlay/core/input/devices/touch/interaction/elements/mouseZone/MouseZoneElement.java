package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.mouseZone;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross.CrossElementEditable;
import com.catfixture.virgloverlay.core.utils.math.Int2;

public class MouseZoneElement extends TouchableWindowElement {

    public MouseZoneElement(Context context, InputTouchControlElementData data) {
        super(context, data);
        initialSize.Set(400,300);
        setBackgroundResource(R.drawable.fx_tc_rect_rnd_btn);

    }

    @Override
    public void CreateActionEvents(IInputDevice inputDevice) {
        final Int2 elSize = GetSize();

        final Int2 startClickPos = new Int2(0,0);
        onDown.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                    (int) (motionEvent.getY() - elSize.y / 2.0));
            startClickPos.Set(clickPos.x, clickPos.y);
        });
        onMove.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                    (int) (motionEvent.getY() - elSize.y / 2.0));

            final Int2 diff = clickPos.Sub(startClickPos)
                    .Div(1.0f / (data.sensivity));

            float slope = 0.5f;
            diff.x += (int)(Math.max(Math.pow(Math.abs(diff.x), slope),0f));
            diff.y += (int)(Math.max(Math.pow(Math.abs(diff.y), slope),0f));

            inputDevice.SendMouseShift(diff.x, diff.y);
            startClickPos.Set(clickPos.x, clickPos.y);
        });

        onClick.addObserver((observable, o) -> {
            inputDevice.SendMouseClick(0);
        });
        onUp.addObserver((observable, o) -> {
            inputDevice.SendMouseShift(0,0);
        });


        /*newTouchElement.setOnHoverListener((view, motionEvent) -> {
            Dbg.Msg("EVT " + motionEvent.getAction() + " " + motionEvent.getRawX() + " " + motionEvent.getRawY());
            return false;
        });*/
    }

    @Override
    public void CreateEditorEvents() {

    }

    @Override
    public void Select(ViewGroup customContainer) {
        getBackground().setColorFilter(context.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

        MouseZoneElementEditable editable = new MouseZoneElementEditable(context, this);
        customContainer.removeAllViews();
        customContainer.addView(editable.GetRoot());
    }

    @Override
    public void Deselect() {

    }
}
