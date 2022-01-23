package com.catfixture.virgloverlay.core.vgobridge;

import android.os.Handler;

import com.catfixture.virgloverlay.core.debug.Dbg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class VGOBridge {
    private final int port;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private Consumer<VGOBridgeHandle> onConnected;

    public void Start() {
        Handler handler = new Handler();
        new Thread(() -> {
            isRunning = true;
            try {
                serverSocket = new ServerSocket(port);
                Dbg.Msg("INPUT BRIDGE RUNNING!");
                while(isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    VGOBridgeHandle vgoBridgeHandle = new VGOBridgeHandle(clientSocket);
                    Dbg.Msg("INPUT BRIDGE ACCEPT INPUT CONNECTION!");
                    if ( onConnected != null) {
                        handler.post(() -> {
                            onConnected.accept(vgoBridgeHandle);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void Stop() {
        try {
            serverSocket.close();
        } catch (Exception x) { x.printStackTrace();}
        isRunning = false;
    }

    public VGOBridge(int port) {
        this.port = port;
    }

    public void OnConnected(Consumer<VGOBridgeHandle> onConnected) {
        this.onConnected = onConnected;
    }
}
