package com.catfixture.virgloverlay.core.input.devices;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

public interface IInputDevice {
    void Destroy();
    void Show();
    void Hide();
    void Setup(InputConnection ic);

    void SendKeyEvent(KeyEvent keyEvent);
}
