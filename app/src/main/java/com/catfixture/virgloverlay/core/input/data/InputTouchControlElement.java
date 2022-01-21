package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;

import com.catfixture.virgloverlay.core.utils.math.Int2;

public class InputTouchControlElement {
    public Int2 position = new Int2(500,500);
    public void SetPosition(Int2 position) { this.position = position; Save();}

    public float alpha = 1f;
    public void SetAlpha(float alpha) { this.alpha = alpha; Save(); }

    public int scale = 100;
    public void SetScale(int scale) { this.scale = scale;Save();}


    public int type = TYPE_CIRCLE_BUTTON;

    private void Save() {
        app.GetInputController().Save();
    }
}
