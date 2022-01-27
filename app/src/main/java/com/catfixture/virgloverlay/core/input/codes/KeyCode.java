package com.catfixture.virgloverlay.core.input.codes;

public class KeyCode {
    public String name;
    public int code;

    public KeyCode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
