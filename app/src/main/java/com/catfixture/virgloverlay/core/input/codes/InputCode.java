package com.catfixture.virgloverlay.core.input.codes;

public class InputCode {
    public String name;
    public int code;

    public InputCode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
