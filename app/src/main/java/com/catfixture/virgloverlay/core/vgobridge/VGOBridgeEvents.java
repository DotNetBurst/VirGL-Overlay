package com.catfixture.virgloverlay.core.vgobridge;

import com.catfixture.virgloverlay.core.utils.types.Event;

public class VGOBridgeEvents {
    public final Event onSlaveConnected = new Event();
    public final Event onSlaveDisconnected = new Event();
    public final Event onServerFallen = new Event();
    public final Event onServerStarted = new Event();
    public final Event onErrorOccur = new Event();

}
