package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import com.catfixture.virgloverlay.R;

public class Icon {
    public final String name;
    public final int drawable;

    public Icon(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    @Override
    public String toString() {
        return name;
    }


    public static Icon[] icons = new Icon[] {
            new Icon("No icon", -1),
            new Icon("Fire 1", R.drawable.tc_aim_1),
            new Icon("Health 1", R.drawable.tc_health_1),

            new Icon("Up arrow 1", R.drawable.tc_up_1),
            new Icon("Down arrow 1", R.drawable.tc_down_1),
            new Icon("Left arrow 1", R.drawable.tc_left_1),
            new Icon("Right arrow 1", R.drawable.tc_right_1),

            new Icon("Plus 1", R.drawable.tc_plus_1),
            new Icon("Minus 1", R.drawable.tc_minus_1),

            new Icon("Close 1", R.drawable.tc_close_1),
    };

    public static int SpinnerIconPos(int drawable) {
        int index = 0;
        for (Icon spinnerDatum : icons) {
            if ( spinnerDatum.drawable == drawable) return index;
            index++;
        }
        return -1;
    }
}
