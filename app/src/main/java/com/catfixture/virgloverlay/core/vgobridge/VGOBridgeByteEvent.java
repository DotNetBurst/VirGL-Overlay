package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeByteEvent implements IVGOBridgeEvent {
    private int type;
    private byte[] args;

    public VGOBridgeByteEvent(int type, byte arg) {
        this.type = type;
        this.args = new byte[]{arg};
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
        this.args = (byte[]) args;
    }
}
