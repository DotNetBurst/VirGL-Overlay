package com.catfixture.virgloverlay.core.input;

import static com.catfixture.virgloverlay.core.App.app;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;

import com.catfixture.virgloverlay.core.input.devices.IInputDevice;


public class MainInputMethodService extends InputMethodService {
    private IInputProvider mainInputProvider;
    private IInputDevice inputDevice;

    @Override
    public View onCreateInputView() {
        Log.d("ee", "IME ACTIVE");
        mainInputProvider = new MainInputProvider();
        return null;
    }

    @Override
    public void onWindowShown() {
        super.onWindowShown();
        inputDevice = mainInputProvider.ResolveDevice(getApplicationContext(),
                app.GetInputController().GetConfigData());
        inputDevice.Show();
    }

    @Override
    public void onWindowHidden() {
        super.onWindowHidden();
        inputDevice.Hide();
        Log.d("ee", "IME WIN HIDDEN");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inputDevice.Destroy();
        Log.d("ee", "IME DESTROYED");
    }
}
