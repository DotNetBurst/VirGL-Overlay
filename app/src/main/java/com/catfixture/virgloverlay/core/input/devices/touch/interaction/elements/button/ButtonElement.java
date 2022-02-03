package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.TouchControlHelper.CreateButtonLabel;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;

public class ButtonElement extends TouchableWindowElement {
    private final TextView label;

    public ButtonElement(Context context, InputTouchControlElementData data) {
        super(context, data);

        initialSize.Set(150,150);

        label = CreateButtonLabel(context);
        addView(label);

        setBackgroundResource(R.drawable.fx_tc_circle_btn);

        SetText(KeyCodes.GetCodeName(data.keyCode));
    }

    public void SetText(String text) {
        label.setText(text);
    }

    @Override
    public void CreateActionEvents(IInputDevice inputDevice) {
        onDown.addObserver((observable, o) -> {
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, data.keyCode);
            inputDevice.SendKeyDown(keyEvent.getKeyCode());
        });
        onUp.addObserver((observable, o) -> {
            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, data.keyCode);
            inputDevice.SendKeyUp(keyEvent.getKeyCode());
        });
    }

    @Override
    public void CreateEditorEvents() {

    }

    @Override
    public void Select(ViewGroup customContainer) {
        getBackground().setColorFilter(context.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

        ButtonElementEditable editable = new ButtonElementEditable(context, this);
        customContainer.removeAllViews();
        customContainer.addView(editable.GetRoot());
    }

    @Override
    public void Deselect() {
        getBackground().setColorFilter(null);
    }
}

