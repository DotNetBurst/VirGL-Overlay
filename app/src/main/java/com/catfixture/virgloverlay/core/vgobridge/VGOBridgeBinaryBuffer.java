package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeBinaryBuffer implements IBinaryBufferProvider {
    private int ptr = 0;
    private byte[] data;

    private static final byte[] _IntToByteArray_CACHE = new byte[4];
    public static final byte[] IntToByteArray(int value) {
        _IntToByteArray_CACHE[3] = (byte)(value >>> 24);
        _IntToByteArray_CACHE[2] = (byte)(value >>> 16);
        _IntToByteArray_CACHE[1] = (byte)(value >>> 8);
        _IntToByteArray_CACHE[0] = (byte)(value);
        return _IntToByteArray_CACHE;
    }

    public VGOBridgeBinaryBuffer() {
         data = new byte[VGOBridgeProtocol.MAX_INTERCHANGE_FRAME_BUFFER_SIZE];
    }

    public void Reset() {
        ptr = 0;
    }

    public VGOBridgeBinaryBuffer WriteByteBlock(byte[] block) {
        if ( CheckBounds(block.length)) {
            System.arraycopy(block, 0, data, 0, block.length);
            ptr += block.length;
        } else {
            Reinflate();
            WriteByteBlock(block);
        }
        return this;
    }
    public VGOBridgeBinaryBuffer WriteBuffer(VGOBridgeBinaryBuffer vgobBuff) {
        byte[] block = vgobBuff.GetData();
        if ( CheckBounds(block.length)) {
            System.arraycopy(block, 0, data, 0, block.length);
            ptr += block.length;
        } else {
            Reinflate();
            WriteBuffer(vgobBuff);
        }
        return this;
    }
    public VGOBridgeBinaryBuffer WriteByte(byte single) {
        if ( CheckBounds(1)) {
            data[ptr] = single;
            ptr ++;
        } else {
            Reinflate();
            WriteByte(single);
        }
        return this;
    }
    public VGOBridgeBinaryBuffer WriteInt(int dec) {
        if ( CheckBounds(4)) {
            byte[] ba = IntToByteArray(dec);
            System.arraycopy(ba, 0, data, ptr, ba.length);
            ptr += ba.length;
        } else {
            Reinflate();
            WriteInt(dec);
        }
        return this;
    }

    private void Reinflate() {
        byte[] tmp = new byte[data.length*2];
        System.arraycopy(data, 0, tmp, 0, data.length);
        data = tmp;
    }

    private boolean CheckBounds(int i) {
        return ptr + i < data.length;
    }

    @Override
    public byte[] GetData() {
        return data;
    }

    public boolean IsReady() {
        return ptr > 0;
    }
}
