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

    public void Stop() {
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendData(byte[] data) throws IOException {
        out.write(data, 0, data.length);
        out.flush();
    }
}
