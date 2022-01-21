package com.catfixture.virgloverlay.core.input.devices;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.windows.IInputWindow;


public abstract class BasicInputDevice implements IInputDevice {
    protected Context context;
    private IInputWindow window;

    public BasicInputDevice(Context context, IInputWindow window) {
        this.context = context;
        this.window = window;
    }

    @Override
    public void Initialize() {
        Initialize(window);
        window.Create();
    }

    @Override
    public void Destroy() {
        window.Destroy();
    }

    @Override
    public void Show() {
        window.Show();
    }

    @Override
    public void Hide() {
        window.Hide();
    }

    protected abstract void Initialize(IInputWindow window);
}
