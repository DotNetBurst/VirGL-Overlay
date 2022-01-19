package com.catfixture.virgloverlay.core.impl;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.os.Debug;
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
    private boolean started;
    private IServerRemoteCallback serverRemoteCallback;

    private IServerStopRemoteCallback serverStopRemoteCallback = () -> {
        try {
            if (app.GetConfigData().automaticMode) {
                Dbg.Msg("RESTARTING FROM AUTOMODE!");
                Stop(false);
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

    public boolean SwitchServer(IServerRemoteCallback mRemoteCallback) {
        this.serverRemoteCallback = mRemoteCallback;

        if (!started) {
            Start(mRemoteCallback);
        } else {
            Stop(true);
        }

        started = !started;
        return started;
    }

    public void Start(IServerRemoteCallback mRemoteCallback) {
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
    }

    public void Stop(boolean forced) {
        if ( forced || started) {
            if (mLinker.isBind()) {
                try {
                    mLinker.unRegisterObject(serverStopRemoteCallback);
                    mLinker.unRegisterObject(serverRemoteCallback);
                    mLinker.unbind();
                    mLinker.setBindCallback(null);
                } catch (Exception x) {
                    Dbg.Error(x);
                }
            }
            serverRemoteCallback.onServerStopped();
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
}
