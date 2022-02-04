package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import com.catfixture.virgloverlay.R;

public class Shapes {
    public final static int BUTTON_SHAPE_CIRCLE = 0;
    public final static int BUTTON_SHAPE_ROUNDED = 1;
    public final static int BUTTON_SHAPE_RECT = 2;

    public static String[] shapes = new String[] {
            "Circle",
            "Rounded",
            "Rect"};

    public static int SpinnerShapePos(String _shape) {
        int index = 0;
        for (String shape : shapes) {
            if ( shape.equals(_shape)) return index;
            index++;
        }
        return -1;
    }
}
