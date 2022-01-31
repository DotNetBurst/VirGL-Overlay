package com.catfixture.virgloverlay.core.impl;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.os.Process;

import com.catfixture.virgloverlay.BuildConfig;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteCallback;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.ipc.IServerStopRemoteCallback;
import com.codezjx.andlinker.AndLinker;
import com.codezjx.andlinker.adapter.OriginalCallAdapterFactory;

public class ServerController implements AndLinker.BindCallback {
    private final Context context;

    private AndLinker mLinker;
    private IServerRemoteService mRemoteService;
    private boolean started, opCompleted;
    private int serverPID = -1;
    private IServerRemoteCallback serverRemoteCallback;

    private IServerStopRemoteCallback serverStopRemoteCallback = () -> {
        try {
            if (app.GetMainConfigData().automaticMode) {
                Dbg.Msg("RESTARTING FROM AUTOMODE!");
                Stop(true);
                started = false;
                Thread.sleep(1000);
                started = true;
                Start(serverRemoteCallback);
            } else {
                Stop(false);
                started = false;
            }
        } catch (Exception x) {
            Dbg.Error("Cant run from automode");
            Dbg.Error(x);
        }
    };

    public ServerController(Context context) {
        this.context = context;
    }
 
    public void Start(IServerRemoteCallback mRemoteCallback) {
        opCompleted = false;
        Dbg.Msg("START CMD!");
        AndLinker.enableLogger(true);
        mLinker = new AndLinker.Builder(context)
                .packageName(BuildConfig.APPLICATION_ID)
                .action(BuildConfig.APPLICATION_ID + ".REMOTE_SERVICE_ACTION")
                .addCallAdapterFactory(OriginalCallAdapterFactory.create())
                .build();
        mLinker.setBindCallback(this);
        mLinker.registerObject(mRemoteCallback);
        mLinker.registerObject(serverStopRemoteCallback);
        mLinker.bind();
        started = true;
        opCompleted = true;
    }

    public void Stop(boolean forced) {
        if ( forced || started) {
            opCompleted = false;
            if ( forced) started = false;
            try {
                mLinker.unRegisterObject(serverStopRemoteCallback);
                mLinker.unRegisterObject(serverRemoteCallback);
                mLinker.unbind();
                mLinker.setBindCallback(null);
            } catch (Exception x) {
                Dbg.Error(x);
            }
            try {
                if (serverPID != -1) {
                    Process.killProcess(serverPID);
                    serverPID = -1;
                } else Dbg.Error("Wrong pid");
            } catch (Exception x) {
                Dbg.Error(x);
            }
            serverRemoteCallback.onServerStopped();
            opCompleted = true;
            Dbg.Msg("STOPPED force = " + forced);
        }
    }

    public IServerRemoteService GetRemote() {
        return mRemoteService;
    }

    @Override
    public void onBind() {
        mRemoteService = mLinker.create(IServerRemoteService.class);
        mRemoteService.RegisterCallback(serverRemoteCallback);
        mRemoteService.RegisterStopCallback(serverStopRemoteCallback);
        serverPID = mRemoteService.GetServerPID();
        Dbg.Msg("BINDED!");
    }

    @Override
    public void onUnBind() {
        mRemoteService = null;
    }

    public boolean IsStarted() {
        return started;
    }

    public void EnableAutomaticMode(IServerRemoteCallback serverRemoteCallback, boolean automaticMode) {
        this.serverRemoteCallback = serverRemoteCallback;

        if (automaticMode) {
            if (!started) {
                started = true;
                Start(serverRemoteCallback);
            }
        }
    }

    public void SwitchServer(IServerRemoteCallback serverRemoteCallback) {
        this.serverRemoteCallback = serverRemoteCallback;
        if (started) {
            Stop(true);
        } else Start(serverRemoteCallback);
    }
}
