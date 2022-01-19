package com.catfixture.virgloverlay.core.ipc;

import com.codezjx.andlinker.annotation.RemoteInterface;

@SuppressWarnings("unused")
@RemoteInterface
public interface IServerRemoteCallback {
    void onStateChanged(int state, int servicesCount);
    void onServerStopped();
    void onServiceCreated(int id, String threadName, int servicesCount);
    void onServiceDestroyed(int id);
    void onServiceChanged(int id, int state, int fd);
}