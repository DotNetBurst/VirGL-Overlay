package com.catfixture.virgloverlay.core.vgobridge;

import java.util.ArrayList;
import java.util.List;

public class VGOBridgeFrame {
    private final VGOBridgeBinaryBuffer buffer = new VGOBridgeBinaryBuffer();
    private final List<IVGOBridgeEvent> events = new ArrayList<>();

    public VGOBridgeFrame() {
    }

    public void EnqueueEvent(IVGOBridgeEvent event) {
        events.add(event);
    }
    public void SetEvent(IVGOBridgeEvent event) {
        IVGOBridgeEvent existent = null;
        for (IVGOBridgeEvent ivgoBridgeEvent : events) {
            if ( ivgoBridgeEvent.GetType() == event.GetType()) {
                existent = ivgoBridgeEvent;
                break;
            }
        }
        if (existent != null) {
            existent.SetArgs(event.GetArgs());
        } else events.add(event);
    }

    public boolean IsReady() {
        return buffer.IsReady();
    }

    public void Compile() {
        buffer.Reset();
        if (events.size() > 0) {
            for (IVGOBridgeEvent event : events)
                buffer.WriteByteBlock(event.Compile());
        }
    }

    public byte[] GetData() {
        return buffer.GetData();
    }
}
