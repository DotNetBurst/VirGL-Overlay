package com.catfixture.virgloverlay.ui.common.genAdapter;

@SuppressWarnings("unused")
public interface IAdapterItem {
    void ToggleVisibility(boolean isVisible);
    boolean IsVisible();
    void SetSpacing(int spacing);
    int GetSpacing();
}
