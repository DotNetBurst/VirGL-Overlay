package com.catfixture.virgloverlay.core.vgobridge;

public interface IVGOBridgeEvent {
    void Compile(VGOBridgeBinaryBuffer buffer);
    int GetType();
    Object GetArgs();
    void SetArgs(Object args);
}
