package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import com.catfixture.virgloverlay.R;

public class BType {
    public final static int BUTTON_TYPE_KEYBOARD = 0;
    public final static int BUTTON_TYPE_MOUSE = 1;
    public final static int BUTTON_TYPE_GAMEPAD = 2;

    public static String[] types = new String[] {
            "Keyboard",
            "Mouse",
            "Gamepad [Not impl.]"};

    public static int SpinnerShapePos(String _shape) {
        int index = 0;
        for (String type : types) {
            if ( type.equals(_shape)) return index;
            index++;
        }
        return -1;
    }
}
