package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeIntEvent implements IVGOBridgeEvent {
    private int type;
    private int[] args;

    public VGOBridgeIntEvent(int type, int arg) {
        this.type = type;
        this.args = new int[]{arg};
    }

    @Override
    public byte[] Compile() {
        return new byte[0];
    }

    @Override
    public int GetType() {
        return type;
    }

    @Override
    public Object GetArgs() {
        return args;
    }

    @Override
    public void SetArgs(Object args) {
        this.args = (int[]) args;
    }
}
