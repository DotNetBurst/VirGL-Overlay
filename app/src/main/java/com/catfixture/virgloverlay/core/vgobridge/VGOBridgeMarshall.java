package com.catfixture.virgloverlay.core.vgobridge;

import android.os.Handler;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class VGOBridgeMarshall {
    private final int port;
    private final Handler handler;
    private final int targetFPS;
    private final ScheduledExecutorService exec;
    private Thread marshallThread;
    private final VGOBridgeFrame currentFrame;
    private ServerSocket serverSocket;
    public final VGOBridgeEvents events = new VGOBridgeEvents();

    public VGOBridgeMarshall (int port, int targetFPS) {
        this.port = port;
        this.targetFPS = targetFPS;
        currentFrame = new VGOBridgeFrame();
        handler = new Handler();
        exec = Executors.newSingleThreadScheduledExecutor();
    }

    public void Run () {
        if ( marshallThread != null) return;
        marshallThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(events.onServerStarted::notifyObservers);

            while(!marshallThread.isInterrupted()) {
                try {
                    TryAcceptConnection();
                } catch (Exception x) {
                    Dbg.Error(x);
                    ThreadUtils.Sleep(1000);
                }
            }
            handler.post(events.onServerFallen::notifyObservers);
        });
        marshallThread.start();
    }

    private void TryAcceptConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();

        VGOBridgeHandle vgoBridgeHandle = new VGOBridgeHandle(clientSocket);
        handler.post(() -> events.onSlaveConnected.notifyObservers(vgoBridgeHandle));

        HandleLoop(vgoBridgeHandle);
    }

    private void HandleLoop(VGOBridgeHandle vgoBridgeHandle) throws IOException {
        int targetDelay = 1000 / targetFPS;
        long lastFrameTime = 0;

        while(!marshallThread.isInterrupted()) {
            long frameStartTime = System.currentTimeMillis();

            synchronized (currentFrame) {
                currentFrame.Compile();
                if (currentFrame.IsReady()) {
                    vgoBridgeHandle.SendData(currentFrame.GetBuffer());
                    currentFrame.Flush();
                }
            }

            lastFrameTime = (System.currentTimeMillis() - frameStartTime);

            long currentDelay = targetDelay - lastFrameTime;
            if ( currentDelay <= 0) { currentDelay = 0;}

            ThreadUtils.Sleep(currentDelay);
        }
        vgoBridgeHandle.Stop();
    }

    public void Stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Dbg.Error(e);
        }
        try {
            marshallThread.interrupt();
            marshallThread = null;
        } catch (Exception x) {
            Dbg.Error(x);
        }
    }

    public void SetEvent(byte type, int ... args) {
        synchronized (currentFrame) {
            currentFrame.SetEvent(new VGOBridgeIntEvent(type, args));
        }
    }

    public void AddByteEvent(byte type, int arg) {
        synchronized (currentFrame) {
            currentFrame.EnqueueEvent(new VGOBridgeByteEvent(type, (byte) arg));
        }
    }
    public void AddIntEvent(byte type, int arg) {
        synchronized (currentFrame) {
            currentFrame.EnqueueEvent(new VGOBridgeIntEvent(type, arg));
        }
    }
}
