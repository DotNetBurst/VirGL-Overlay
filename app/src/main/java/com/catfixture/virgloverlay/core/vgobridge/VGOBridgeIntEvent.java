package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeIntEvent implements IVGOBridgeEvent {
    private byte type;
    private int[] args;

    public VGOBridgeIntEvent(byte type, int arg) {
        this.type = type;
        this.args = new int[]{arg};
    }
    public VGOBridgeIntEvent(byte type, int ... args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public void Compile(VGOBridgeBinaryBuffer buffer) {
        buffer.WriteByte(type);
        for (int arg : args) {
            buffer.WriteInt(arg);
        }
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
