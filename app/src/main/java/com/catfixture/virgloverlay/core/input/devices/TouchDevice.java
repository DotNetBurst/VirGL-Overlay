package com.catfixture.virgloverlay.core.input.devices;

import android.content.Context;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeHandle;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeMarshall;
import com.catfixture.virgloverlay.core.vgobridge.VGOBridgeProtocol;


public class TouchDevice implements IInputDevice {
    private VGOBridgeMarshall vgoBridgeMarshall;
    private VGOBridgeHandle handle;
    private Context context;

    public TouchDevice(Context context) {
        this.context = context;

        if ( vgoBridgeMarshall != null) {
            vgoBridgeMarshall.Stop();
            vgoBridgeMarshall = null;
        }

        KeyCodes.LoadKeyCodes(context);

        vgoBridgeMarshall = new VGOBridgeMarshall(8888, 25);
        vgoBridgeMarshall.events.onSlaveConnected.addObserver((obs, handle) -> {
            Dbg.Msg("CREATING INPUT BRIDGE CONNECTION!");
            Dbg.Msg("INPUT BRIDGE CONNECTED!");
        });
        vgoBridgeMarshall.Run();
    }

    @Override
    public void Destroy() {
        vgoBridgeMarshall.Stop();
    }

    @Override
    public void SendMouseClick(int button) {
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_MOUSE_CLICK, button);
    }

    @Override
    public void SendMouseDown(int button) {
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_MOUSE_DOWN, button);
    }

    @Override
    public void SendMouseUp(int button) {
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_MOUSE_UP, button);
    }

    @Override
    public void SendKeyPressed(int keyCode) {
        Dbg.Msg("DEV_KEY = " + keyCode);
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_KEY_PRESSED, keyCode);
    }

    @Override
    public void SendKeyDown(int keyCode) {
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_KEY_DOWN, keyCode);
    }

    @Override
    public void SendKeyUp(int keyCode) {
        vgoBridgeMarshall.AddEvent(VGOBridgeProtocol.ACTION_KEY_UP, keyCode);
    }

    @Override
    public void SendMouseShift(float verticalCos, float horizontalCos) {
        float sens = 0.02f;

        vgoBridgeMarshall.SetEvent(VGOBridgeProtocol.ACTION_SET_MOUSE_POS,
                (int) (verticalCos * sens), (int) (horizontalCos * sens));

    }
}
