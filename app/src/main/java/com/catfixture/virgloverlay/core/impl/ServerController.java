package com.catfixture.virgloverlay.core.impl;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.CommonContext.comCtx;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_ERROR;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_IDLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Process;

import com.catfixture.virgloverlay.BuildConfig;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteCallback;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.ipc.IServerStopRemoteCallback;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.codezjx.andlinker.AndLinker;
import com.codezjx.andlinker.adapter.OriginalCallAdapterFactory;

public class ServerController implements AndLinker.BindCallback {
    public final static int ACTION_STOP_SERVER = 223;

    private final Context context;
    private final Handler handler = new Handler();
    private final Thread watcherThread;

    private AndLinker mLinker;
    private IServerRemoteService mRemoteService;
    private int serverPID = -1;
    private IServerRemoteCallback serverRemoteCallback;

    public ServerController(Context context) {
        this.context = context;

        watcherThread = new Thread(() -> {
            while(true) {
                if (Thread.currentThread().isInterrupted()) return;
                if ( app.GetMainConfigData().automaticMode) {
                    if (serverRemoteCallback == null) {
                        ThreadUtils.Sleep(1000);
                        continue;
                    }

                    if ( mLinker != null && mLinker.isBind()) {
                        boolean isServerStartedAndRunningNormally = mRemoteService != null &&
                                mRemoteService.GetState() != SERVER_STATE_IDLE && mRemoteService.GetState() != SERVER_STATE_ERROR;
                        if (!isServerStartedAndRunningNormally)
                            Restart();
                    } else if ( mLinker == null) {
                        Start();
                    }
                }
                ThreadUtils.Sleep(1000);
            }
        });
        watcherThread.start();
    }

    private void Start() {
        Dbg.Msg("START CMD!");
        AndLinker.enableLogger(true);
        mLinker = new AndLinker.Builder(context)
                .packageName(BuildConfig.APPLICATION_ID)
                .action(BuildConfig.APPLICATION_ID + ".REMOTE_SERVICE_ACTION")
                .addCallAdapterFactory(OriginalCallAdapterFactory.create())
                .build();
        mLinker.setBindCallback(this);
        mLinker.registerObject(serverRemoteCallback);
        mLinker.bind();
    }

    private void Stop() {
        try {
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
        mLinker = null;
        serverRemoteCallback.onServerStopped();
        Dbg.Msg("STOPPED!");
    }

    public IServerRemoteService GetRemote() {
        return mRemoteService;
    }

    @Override
    public void onBind() {
        mRemoteService = mLinker.create(IServerRemoteService.class);
        mRemoteService.RegisterCallback(serverRemoteCallback);
        serverPID = mRemoteService.GetServerPID();
        Dbg.Msg("BINDED!");
    }

    @Override
    public void onUnBind() {
        mRemoteService = null;
    }

    public void SwitchServer(IServerRemoteCallback serverRemoteCallback) {
        this.serverRemoteCallback = serverRemoteCallback;
        if ( mLinker == null) {
            Start();
        } else TryStop();
    }

    public void Restart() {
        TryStop();
        handler.postDelayed(this::Start,1000);
    }

    public void TryStop() {
        if ( mLinker != null && mLinker.isBind()) {
            Stop();
        }
    }

    public void SetCallback(IServerRemoteCallback serverRemoteCallback) {
        this.serverRemoteCallback = serverRemoteCallback;
    }

    public void Destroy() {
        watcherThread.interrupt();
    }

    public boolean IsProbablyRunning() {
        return mLinker != null && mLinker.isBind() && mRemoteService != null &&
                mRemoteService.GetState() != SERVER_STATE_IDLE && mRemoteService.GetState() != SERVER_STATE_ERROR;
    }
}
