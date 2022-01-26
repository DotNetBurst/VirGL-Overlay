package com.catfixture.virgloverlay.core.vgobridge;

public interface IBinaryBufferProvider {
    byte[] GetData();
    int Size();
    int Capacity();
}
