package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.ButtonElementEditable;
import com.catfixture.virgloverlay.core.utils.math.Int2;


public class CrossElement extends TouchableWindowElement {

    public CrossElement(Context context, InputTouchControlElementData data) {
        super(context, data);

        initialSize.Set(300,300);

        setBackgroundResource(R.drawable.fx_tc_cross_btn);

    }
    private void SetAxis(Int2 axisToSet, Int2 input) {
        final Int2 elSize = GetSize();
        final float deadZoneX = elSize.x * 0.25f;
        final float deadZoneY = elSize.y * 0.25f;
        Binding binding = Binding.Retrieve(data.mappingType);

        if (( input.x < -deadZoneX || input.x > deadZoneX) && ( input.y < -deadZoneY || input.y > deadZoneY)) {
            axisToSet.Set(input.y > 0 ? binding.yPositive : binding.yNegative, input.x > 0 ? binding.xPositive : binding.xNegative);
        } else {
            boolean xGreater = Math.abs(input.x) > Math.abs(input.y);
            if ( xGreater) axisToSet.Set(0, input.x > 0 ? binding.xPositive : binding.xNegative);
            else axisToSet.Set(input.y > 0 ? binding.yPositive : binding.yNegative,0);
        }
    }

    @Override
    public void CreateActionEvents(IInputDevice inputDevice) {
        final Int2 elSize = GetSize();

        final Int2 startAxis = new Int2(0,0);
        final Int2 currentAxis = new Int2(-1,-1);

        onDown.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            final Int2 dt = new Int2((int) motionEvent.getX() - elSize.x / 2,
                    (int) motionEvent.getY() - elSize.y / 2);

            SetAxis(startAxis, dt);

            if ( startAxis.x != -1) {
                inputDevice.SendKeyDown(startAxis.x);
                currentAxis.x = startAxis.x;
            } else currentAxis.x = -1;
            if ( startAxis.y != -1) {
                inputDevice.SendKeyDown(startAxis.y);
                currentAxis.y = startAxis.y;
            } else currentAxis.y = -1;
            Dbg.Msg("Click poos " + startAxis.x + " _ " + startAxis.y);
        });
        onMove.addObserver((observable, o) -> {
            MotionEvent motionEvent = (MotionEvent) o;
            final Int2 dt = new Int2((int) motionEvent.getX() - elSize.x / 2,
                    (int) motionEvent.getY() - elSize.y / 2);

            SetAxis(startAxis, dt);

            if ( startAxis.x != -1 && startAxis.x != currentAxis.x) {
                inputDevice.SendKeyUp(currentAxis.x);
                currentAxis.x = startAxis.x;
                inputDevice.SendKeyDown(currentAxis.x);
            }
            if ( startAxis.y != -1 && startAxis.y != currentAxis.y) {
                inputDevice.SendKeyUp(currentAxis.y);
                currentAxis.y = startAxis.y;
                inputDevice.SendKeyDown(currentAxis.y);
            }
            Dbg.Msg("Click poos " + startAxis.x + " _ " + startAxis.y);
        });
        onUp.addObserver((observable, o) -> {
            if ( currentAxis.x != -1)
                inputDevice.SendKeyUp(currentAxis.x);
            if ( currentAxis.y != -1)
                inputDevice.SendKeyUp(currentAxis.y);
        });
    }

    @Override
    public void CreateEditorEvents() {

    }

    @Override
    public void Select(ViewGroup customContainer) {
        getBackground().setColorFilter(context.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

        CrossElementEditable editable = new CrossElementEditable(context, this);
        customContainer.removeAllViews();
        customContainer.addView(editable.GetRoot());
    }

    @Override
    public void Deselect() {
        getBackground().setColorFilter(null);
    }
}