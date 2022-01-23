package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeBuffer {
    private static final int MAX_BUFFER_SIZE = 512;
    private byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int bufferPtr;

    private static final byte[] _IntToByteArray_CACHE = new byte[4];
    public static final byte[] IntToByteArray(int value) {
        _IntToByteArray_CACHE[0] = (byte)(value >>> 24);
        _IntToByteArray_CACHE[1] = (byte)(value >>> 16);
        _IntToByteArray_CACHE[2] = (byte)(value >>> 8);
        _IntToByteArray_CACHE[3] = (byte)(value);
        return _IntToByteArray_CACHE;
    }

    public VGOBridgeBuffer WriteByte(byte code) {
        buffer[bufferPtr++] = code;
        return this;
    }

    public VGOBridgeBuffer WriteInt(int code) {
        byte[] ba = IntToByteArray(code);
        System.arraycopy(ba, 0, buffer, bufferPtr, ba.length);
        bufferPtr += ba.length;
        return this;
    }

    public byte[] Get() { return buffer;}

    public int Size() {
        return bufferPtr;
    }

    public VGOBridgeBuffer Reset() {
        bufferPtr = 0;
        return this;
    }
}
