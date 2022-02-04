package com.catfixture.virgloverlay.core.input.codes;

public class MouseCode {
    public final String name;
    public final int code;
    public final String smallName;

    public MouseCode(String name, String smallName, int code) {
        this.name = name;
        this.code = code;
        this.smallName = smallName;
    }

    @Override
    public String toString() {
        return name;
    }
}
