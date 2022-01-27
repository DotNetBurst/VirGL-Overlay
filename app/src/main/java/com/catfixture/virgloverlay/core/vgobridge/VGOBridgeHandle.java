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

    public void SendData(VGOBridgeBinaryBuffer buffer) throws IOException {
        int toWrite = buffer.Available();
        if (toWrite > 0) {
            byte[] header = VGOBridgeBinaryBuffer.IntToByteArray(buffer.Available());
            out.write(header, 0, header.length);
            out.flush();

            out.write(buffer.GetData(), 0, toWrite);
            out.flush();
        }
    }
}
