package com.catfixture.virgloverlay.core.impl.handles;

public interface IService {
    int GetId();
    int GetFD();
    void SetFD(int fd);
    String GetThreadName();
    int GetServiceState();
    void ChangeState(int state);
}
