package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeByteEvent implements IVGOBridgeEvent {
    private byte type;
    private byte[] args;

    public VGOBridgeByteEvent(byte type, byte arg) {
        this.type = type;
        this.args = new byte[]{arg};
    }

    @Override
    public void Compile(VGOBridgeBinaryBuffer buffer) {
        buffer.WriteByte(type);
        for (byte arg : args) {
            buffer.WriteByte(arg);
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
        this.args = (byte[]) args;
    }
}
