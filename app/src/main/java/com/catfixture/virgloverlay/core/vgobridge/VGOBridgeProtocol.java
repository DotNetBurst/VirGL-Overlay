package com.catfixture.virgloverlay.core.vgobridge;

public class VGOBridgeProtocol {
    public static final int MAX_INTERCHANGE_FRAME_BUFFER_SIZE = 1024;
    public static final int MAX_POOL_BUFFERS_COUNT = 4;

    public static final byte ACTION_SET_MOUSE_POS = 0;
    public static final byte ACTION_MOUSE_CLICK = 1;
    public static final byte ACTION_MOUSE_DOWN = 2;
    public static final byte ACTION_MOUSE_UP = 3;
    public static final byte ACTION_KEY_PRESSED = 4;
    public static final byte ACTION_KEY_DOWN = 5;
    public static final byte ACTION_KEY_UP = 6;
    public static final byte PROTOCOL_ENABLE = 7;
    public static final byte PROTOCOL_DISABLE = 8;
}
