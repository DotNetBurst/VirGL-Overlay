package com.catfixture.virgloverlay.core.input.devices;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

public interface IInputDevice {
    void Destroy();
    void Show();
    void Hide();

    void SendMouseClick(int button);
    void SendMouseDown(int button);
    void SendMouseUp(int button);
    void SendKeyPressed(int keyCode);
    void SendKeyDown(int keyCode);
    void SendKeyUp(int keyCode);

    void SendMouseShift(float vericalCos, float horizontalCos);
}
