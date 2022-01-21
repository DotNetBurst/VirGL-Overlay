package com.catfixture.virgloverlay.core.input.windows;

import android.content.Context;

import com.catfixture.virgloverlay.core.utils.windows.IWindow;


public abstract class BasicInputWindow implements IInputWindow {
    protected final Context context;
    protected IWindow window;

    public abstract IWindow Init();

    public BasicInputWindow(Context context) {
        this.context = context;
        this.window = Init();
    }

    @Override
    public IWindow Create() {
        return window;
    }

    @Override
    public void Destroy() {
        window.Detach();
    }

    @Override
    public void Show() {
        window.SetVisibility(true);
    }

    @Override
    public void Hide() {
        window.SetVisibility(false);
    }

    @Override
    public IWindow GetIWindow() {
        return window;
    }
}
