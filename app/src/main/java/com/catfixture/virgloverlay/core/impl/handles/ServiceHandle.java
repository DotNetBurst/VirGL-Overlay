package com.catfixture.virgloverlay.core.impl.handles;

import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_IDLE;

import com.catfixture.virgloverlay.core.IService;

public class ServiceHandle implements IService {
    private final int id;
    private int fd;
    private int state = SERVICE_STATE_IDLE;
    private final String threadName;


    public ServiceHandle(int id, String threadName) {
        this.id = id;
        this.threadName = threadName;
    }

    @Override
    public int GetFD() {
        return fd;
    }

    @Override
    public int GetId() {
        return id;
    }

    @Override
    public String GetThreadName() {
        return threadName;
    }

    @Override
    public int GetServiceState() {
        return state;
    }


    public void ChangeState(int state) { this.state = state;}

    @Override
    public void SetFD(int fd) {
        this.fd = fd;
    }
}
