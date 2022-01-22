package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface IOverlayFragment {
    int GetID();
    ViewGroup GetContainer();

    void Create(Context context);
    void Destroy();
}
