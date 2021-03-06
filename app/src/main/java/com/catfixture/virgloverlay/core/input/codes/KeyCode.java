package com.catfixture.virgloverlay.core.input.codes;

public class KeyCode {
    public final String name;
    public final int code;

    public KeyCode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
