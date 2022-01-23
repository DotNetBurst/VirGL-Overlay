package com.catfixture.virgloverlay.core.vgobridge;

import com.catfixture.virgloverlay.core.debug.Dbg;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class VGOBridgeHandle {
    private final Socket socket;
    private OutputStream out;

    public VGOBridgeHandle(Socket socket) {
        this.socket = socket;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean IsConnected() {
        return socket.isConnected();
    }

    public void SendBuffer(VGOBridgeBuffer buff) {
        new Thread(() -> {
            try {
                out.write(buff.Get(), 0, buff.Size());
                out.flush();
                Dbg.Msg("DEV_SENT OK " + buff.Size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void Stop() {
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
