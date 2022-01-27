package com.catfixture.virgloverlay.core.vgobridge;

public interface IVGOBridgeEvent {
    byte[] Compile();
    int GetType();
    Object GetArgs();
    void SetArgs(Object args);
}
