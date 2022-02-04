package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross;

import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross.BindingType.*;

public class Binding {
    public final int yPositive;
    public final int yNegative;
    public final int xPositive;
    public final int xNegative;

    public Binding(int yPositive, int yNegative, int xPositive, int xNegative) {
        this.yPositive = yPositive;
        this.yNegative = yNegative;
        this.xPositive = xPositive;
        this.xNegative = xNegative;
    }

    private static final Binding cachedWASDBinding = new Binding(83,87,68,65);
    private static final Binding cachedArrowsBinding = new Binding(40,38,39,37);
    private static final Binding cachedNumpadBinding = new Binding(98,104,102,100);

    public static Binding Retrieve(int mappingType) {
        switch (mappingType) {
            case BINDING_WASD:
                return cachedWASDBinding;
            case BINDING_ARROWS:
                return cachedArrowsBinding;
            case BINDING_NUMPAD:
                return cachedNumpadBinding;
            default:
                return null;
        }
    }
}
