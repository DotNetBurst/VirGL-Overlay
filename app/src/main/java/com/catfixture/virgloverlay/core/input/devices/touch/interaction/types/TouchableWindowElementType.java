package com.catfixture.virgloverlay.core.input.devices.touch.interaction.types;

public class TouchableWindowElementType {
    public static final int TYPE_BUTTON = 1;
    public static final int TYPE_CROSS = 2;
    public static final int TYPE_STICK = 3;
    public static final int TYPE_MOUSE_ZONE = 4;

    public static final TouchableWindowElementSpinnerData[] spinnerData = new TouchableWindowElementSpinnerData[] {
            new TouchableWindowElementSpinnerData("Button", TYPE_BUTTON),
            new TouchableWindowElementSpinnerData("Cross", TYPE_CROSS),
            new TouchableWindowElementSpinnerData("Stick", TYPE_STICK),
            new TouchableWindowElementSpinnerData("Mouse zone", TYPE_MOUSE_ZONE)
    };

    public static int SpinnerDataPos(int type) {
        int index = 0;
        for (TouchableWindowElementSpinnerData spinnerDatum : spinnerData) {
            if ( spinnerDatum.id == type) return index;
            index++;
        }
        return -1;
    }
}
