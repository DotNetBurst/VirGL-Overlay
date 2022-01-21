package com.catfixture.virgloverlay.core.ipc;

import android.content.Context;
import android.content.res.Resources;

import com.codezjx.andlinker.annotation.Callback;
import com.codezjx.andlinker.annotation.RemoteInterface;

import java.util.List;

@SuppressWarnings("unused")
@RemoteInterface
public interface IServerRemoteService {
    int GetState();
    int GetServicesCount();
    List<ServiceParcelable> GetServices();

    void RegisterCallback(@Callback IServerRemoteCallback onServerUpdate);
    void RegisterStopCallback(@Callback IServerStopRemoteCallback serverRemoteCallback);

    int GetServerPID();
    void Stop();
}

