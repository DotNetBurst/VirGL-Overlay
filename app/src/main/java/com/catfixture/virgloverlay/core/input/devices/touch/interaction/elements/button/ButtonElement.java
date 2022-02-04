package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.TouchControlHelper.CreateButtonLabel;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.codes.MouseCodes;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;

public class ButtonElement extends TouchableWindowElement {
    public ButtonElement(Context context, InputTouchControlElementData data) {
        super(context, data);

        if ( data.buttonShape == Shapes.BUTTON_SHAPE_RECT) {
            initialSize.Set(200,100);
        } else {
            initialSize.Set(150,150);
        }

        if ( data.icon != -1) {
            ImageView icon = new ImageView(context);
            icon.setPadding(15,15,15,15);
            icon.setImageResource(data.icon);
            LayoutUtils.SetMatchMatch(icon);
            addView(icon);
        } else {
            TextView label = CreateButtonLabel(context);
            label.setShadowLayer(10, 0, 0, context.getColor(R.color.black));

            if (data.buttonType == BType.BUTTON_TYPE_MOUSE) {
                label.setText(MouseCodes.GetCodeSmallName(data.mouseCode));
            } else if ( data.buttonType == BType.BUTTON_TYPE_KEYBOARD) {
                label.setText(KeyCodes.GetCodeName(data.keyCode));
            }

            addView(label);
        }

        setBackgroundResource( data.buttonShape == Shapes.BUTTON_SHAPE_CIRCLE ?
                R.drawable.fx_tc_circle_btn : data.buttonShape == Shapes.BUTTON_SHAPE_ROUNDED ? R.drawable.fx_tc_rect_rnd_btn :
                    R.drawable.fx_tc_rect_rnd_btn);
    }

    @Override
    public void CreateActionEvents(IInputDevice inputDevice) {
        if (data.buttonType == BType.BUTTON_TYPE_KEYBOARD) {
            onDown.addObserver((observable, o) -> {
                inputDevice.SendKeyDown(data.keyCode);
            });
            onUp.addObserver((observable, o) -> {
                inputDevice.SendKeyUp(data.keyCode);
            });
        } else if ( data.buttonType == BType.BUTTON_TYPE_MOUSE) {
            onDown.addObserver((observable, o) -> {
                inputDevice.SendMouseDown(data.mouseCode);
            });
            onUp.addObserver((observable, o) -> {
                inputDevice.SendMouseUp(data.mouseCode);
            });
        }
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

