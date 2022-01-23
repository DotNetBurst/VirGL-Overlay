package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;

import android.view.KeyEvent;

import com.catfixture.virgloverlay.core.utils.math.Int2;

public class InputTouchControlElement {
    public int id;
    public void SetId(int id) { this.id = id;Save();}

    public Int2 position = new Int2(500,500);
    public void SetPosition(Int2 position) { this.position = position; Save();}

    public float alpha = 1f;
    public void SetAlpha(float alpha) { this.alpha = alpha; Save(); }

    public int scale = 100;
    public void SetScale(int scale) { this.scale = scale;Save();}

    public int type = TYPE_CIRCLE_BUTTON;
    public void SetType(int i) { type = i; Save();}

    public int buttonCode = KeyEvent.KEYCODE_A;
    public void SetButtonCode(int buttonCode) {this.buttonCode = buttonCode; Save();}

    public int handleScale = 100;
    public void SetHandleScale(int handleScale) { this.handleScale = handleScale; Save();}

    public Int2 handlePosition = new Int2(500,500);
    public void SetHandlePosition(Int2 handlePosition) { this.handlePosition = handlePosition; Save();}


    private void Save() {
        app.SaveInputConfig();
    }
}
