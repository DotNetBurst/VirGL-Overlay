package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_BUTTON;

import android.view.KeyEvent;

import com.catfixture.virgloverlay.core.utils.math.Int2;

public class InputTouchControlElementData {
    public int id;
    public void SetId(int id) { this.id = id; Save();}

    public int mouseCode;
    public void SetMouseCode(int mouseCode) { this.mouseCode = mouseCode; Save();}

    public Int2 position = new Int2(500,500);
    public void SetPosition(Int2 position) { this.position = position; Save();}

    public float alpha = 1f;
    public void SetAlpha(float alpha) { this.alpha = alpha; Save(); }

    public int scale = 100;
    public void SetScale(int scale) { this.scale = scale;Save();}

    public int type = TYPE_BUTTON;
    public void SetType(int type) { this.type = type; Save();}

    public int mappingType;
    public void SetMappingType(int mappingType) { this.mappingType = mappingType; Save();}

    public int keyCode = KeyEvent.KEYCODE_A;
    public void SetKeyCode(int buttonCode) {this.keyCode = buttonCode; Save();}

    private void Save() {
        app.SaveInputConfig();
    }

    public int sensivity = 100;
    public void SetSensivity(int sensivity) {
        this.sensivity = sensivity; Save();
    }

    public int icon;
    public void SetIcon(int icon) { this.icon = icon; Save();}

    public int buttonType;
    public void SetButtonType(Integer buttonType) { this.buttonType = buttonType; Save();}

    public int buttonShape;
    public void SetButtonShape(Integer buttonShape) {
        this.buttonShape = buttonShape; Save();
    }

}
