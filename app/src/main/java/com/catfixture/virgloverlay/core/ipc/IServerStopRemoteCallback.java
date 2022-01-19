package com.catfixture.virgloverlay.core.ipc;

import com.codezjx.andlinker.annotation.RemoteInterface;

@SuppressWarnings("unused")
@RemoteInterface
public interface IServerStopRemoteCallback {
    void onServerStopped();
}
