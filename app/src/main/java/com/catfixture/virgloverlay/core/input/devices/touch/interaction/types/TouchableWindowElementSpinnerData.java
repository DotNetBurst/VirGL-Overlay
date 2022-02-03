package com.catfixture.virgloverlay.core.input.devices.touch.interaction.types;

public class TouchableWindowElementSpinnerData {
    public final String name;
    public final int id;

    public TouchableWindowElementSpinnerData(String name, int value) {
        this.name = name;
        this.id = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
