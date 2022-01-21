package com.catfixture.virgloverlay.core.input.windows;


import com.catfixture.virgloverlay.core.utils.windows.IWindow;

public interface IInputWindow {
    IWindow GetIWindow();

    IWindow Create();
    void Destroy();

    void Show();
    void Hide();
}
