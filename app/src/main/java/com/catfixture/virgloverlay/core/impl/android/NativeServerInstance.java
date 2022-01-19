package com.catfixture.virgloverlay.core.impl.android;

import static android.app.PendingIntent.FLAG_ONE_SHOT;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;
import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_ERROR;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_IDLE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_INITIALIZING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_LOADING_NATIVE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_RUNNING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServerState.SERVER_STATE_STARTING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_CONNECTED;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_ERROR;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_IDLE;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_INITIALIZING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_LISTENING;
import static com.catfixture.virgloverlay.core.impl.states.NativeServiceState.SERVICE_STATE_RUNNING;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.android.AndroidUtils;
import com.catfixture.virgloverlay.core.IService;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.debug.logging.GlobalExceptions;
import com.catfixture.virgloverlay.core.impl.handles.ServiceHandle;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteCallback;
import com.catfixture.virgloverlay.core.ipc.IServerRemoteService;
import com.catfixture.virgloverlay.core.ipc.IServerStopRemoteCallback;
import com.catfixture.virgloverlay.core.ipc.ServiceParcelable;
import com.catfixture.virgloverlay.core.types.delegates.Functions;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.ui.activity.virgl.Virgl;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.codezjx.andlinker.AndLinkerBinder;

import java.util.ArrayList;
import java.util.List;

public class NativeServerInstance extends Service {
    private static native void initialize(ConfigData settings);
    private static native int runServer();
    private static native void stopSocket(int fileDescriptor);
    private static native int acceptSocket(int fileDescriptor);
    private static native void runSocketLoop(Class<Windows> windowKlass, int fileDescriptor);

    private int state;
    private int mainSocketDescriptor;
    private final List<IService> services = new ArrayList<>();
    private int serverPID;
    private ConfigData cfgData;
    private AndLinkerBinder mLinkerBinder;
    private IServerRemoteCallback serverRemoteCallback;
    private IServerStopRemoteCallback serverStopRemoteCallback;

    //****REMOTE SERVICE IMPL******//
    @SuppressWarnings("unused")
    private final IServerRemoteService mRemoteService = new IServerRemoteService() {
        @Override
        public int GetState() {
            return state;
        }

        @Override
        public int GetServicesCount() {
            return services.size();
        }

        @Override
        public void RegisterCallback(IServerRemoteCallback _onServerUpdate) {
            serverRemoteCallback = _onServerUpdate;
            serverRemoteCallback.onStateChanged(SERVER_STATE_IDLE, 0);
            Run();
        }

        @Override
        public void RegisterStopCallback(IServerStopRemoteCallback _serverStopRemoteCallback) {
            serverStopRemoteCallback = _serverStopRemoteCallback;
        }

        @Override
        public int GetServerPID() {
            return serverPID;
        }

        @Override
        public List<ServiceParcelable> GetServices() {
            List<ServiceParcelable> parcelables = new ArrayList<>();
            for (IService service : services)
                parcelables.add(new ServiceParcelable(service));
            return parcelables;
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        GlobalExceptions.Init();

        mLinkerBinder = AndLinkerBinder.Factory.newBinder();
        mLinkerBinder.registerObject(mRemoteService);
        cfgData = app.GetConfigData();

        Intent notificationIntent = new Intent(this, Virgl.class);
        notificationIntent.putExtra("stopServer", "true");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE | FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? AndroidUtils.createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId).setOngoing(true)
                .setSmallIcon(R.drawable.main_ico)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);

        if (cfgData.automaticMode) {
            notificationBuilder.setContentText("Running in automatic mode");
        } else {
            notificationBuilder
                    .setContentText("Tap to stop service")
                    .setContentIntent(pendingIntent);
        }

        Notification notification = notificationBuilder.build();
        startForeground(Const.SERVER_THREAD_CODE, notification);
        ShowToast("Server started");


        Windows.Init(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(APP_TAG, "Service onBind()");
        return mLinkerBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(APP_TAG, "Stopping server");

        mLinkerBinder.unRegisterObject(mRemoteService);

        if (mainSocketDescriptor != -1) {
            stopSocket(mainSocketDescriptor);
        } else Log.e(APP_TAG, "Wrong file descriptor");
        Process.killProcess(serverPID);
        services.clear();

        Log.d(APP_TAG, "Server stopped");
        ShowToast("Server stopped");
        stopSelf();
    }

    private void Run() {
        new Thread(() -> {
            SetServerState(SERVER_STATE_INITIALIZING);
            Log.d(APP_TAG, "Starting server");
            serverPID = Process.myPid();
            TryRunVigGLServer(cfgData);
        }).start();
    }

    private void TryRunVigGLServer(ConfigData cfgData) {
        ConfigProfile cfgProfile = cfgData.GetCurrentProfile();

        SetServerState(SERVER_STATE_LOADING_NATIVE);
        Log.d(APP_TAG, "Loading native-lib...");
        System.loadLibrary("native-lib");
        Log.d(APP_TAG, "Native-lib loaded");
        SetServerState(SERVER_STATE_INITIALIZING);

        Log.d(APP_TAG, "Initializing native-lib...");
        initialize(cfgData);
        Log.d(APP_TAG, "Native-lib initialized");

        Log.d(APP_TAG, "Server mode " + (cfgProfile.useMultithreadedEGLAccess ? "Multi-Threaded" : "Single-Threaded"));
        Log.d(APP_TAG, "Starting server...");
        SetServerState(SERVER_STATE_STARTING);


        Functions.Function1<Integer, Integer> runServerInstance = (id) -> {
            int fileDescriptor = runServer();
            if ( fileDescriptor < 0) {
                Dbg.Error("Error could not start server...");
                return -1;
            } else {
                Log.d(APP_TAG, "Server " + id + " running");
                SetServerState(SERVER_STATE_RUNNING);
            }
            return fileDescriptor;
        };


        mainSocketDescriptor = runServerInstance.Invoke(0);
        if (mainSocketDescriptor == -1) {
            SetServerState(SERVER_STATE_ERROR);
            throw new RuntimeException("Fatal exception could not start server! Check SELinux mode");
        }
        Log.d(APP_TAG, "Main socket FD = " + mainSocketDescriptor);

        if ( cfgProfile.useMultithreadedEGLAccess) {
            Thread mutex = Thread.currentThread();

            for (int i = 0; i < cfgProfile.eglAccessMaxThreads; i++) {
                int finalI = i;
                Thread procThread = new Thread(() -> {
                    IService service = AddNewService(finalI, Thread.currentThread().getName());
                    synchronized (mutex) {
                        mutex.notifyAll();
                    }
                    TryRunVirGLProcessor(service, mainSocketDescriptor);
                });
                procThread.start();
                synchronized (mutex) {
                    try {
                        mutex.wait();
                    } catch (InterruptedException e) {
                        Dbg.Error(e);
                    }
                }
            }
        } else {
            IService service = AddNewService(0, Thread.currentThread().getName());
            TryRunVirGLProcessor(service, mainSocketDescriptor);
        }

    }

    private void TryRunVirGLProcessor(IService service, int fileDescriptor) {
        SetServiceState(service, SERVICE_STATE_IDLE);
        SetServiceState(service, SERVICE_STATE_LISTENING);
        Log.d(APP_TAG, "Listening for connection...");
        fileDescriptor = acceptSocket(fileDescriptor);
        SetServiceFD(service, fileDescriptor);
        SetServiceState(service, SERVICE_STATE_CONNECTED);
        Log.d(APP_TAG, "Connection accepted");
        SetServiceState(service, SERVICE_STATE_INITIALIZING);
        Log.d(APP_TAG, "Running per socket loop");
        Log.d(APP_TAG, "Native-lib running");
        SetServiceState(service, SERVICE_STATE_RUNNING);
        runSocketLoop(Windows.class, fileDescriptor);
        Log.d(APP_TAG, "Loop ended");
        serverStopRemoteCallback.onServerStopped();
        try {
            Process.killProcess(serverPID);
            SetServerState(SERVER_STATE_IDLE);
        } catch (Exception x) {
            Dbg.Error("Cant kill server");
        }
    }

    private void ShowToast(String text) {
        app.TryGetProfile(cfgProfile -> {
            if ( cfgProfile.enableToasts)
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        });
    }
    void SetServerState(int serverState) {
        state = serverState;
        try {
        serverRemoteCallback.onStateChanged(state, services.size());   } catch (Exception x) {
            Dbg.Error(x);
        }
    }
    private IService AddNewService(int id, String threadName) {
        IService service = new ServiceHandle(id, "["+threadName+"]");
        services.add(service);
        try {
            serverRemoteCallback.onServiceCreated(id, threadName, services.size());
        } catch (Exception x) {
            Dbg.Error(x);
        }
        return service;
    }
    private void SetServiceState(IService service, int state) {
        service.ChangeState(state);
        try {
            serverRemoteCallback.onServiceChanged(service.GetId(), state, service.GetFD());
        } catch (Exception x) {
            Dbg.Error(x);
        }
    }
    private void SetServiceFD(IService service, int fd) {
        service.SetFD(fd);
        try {
            serverRemoteCallback.onServiceChanged(service.GetId(), state, service.GetFD());
        } catch (Exception x) {
            Dbg.Error(x);
        }
    }
}
