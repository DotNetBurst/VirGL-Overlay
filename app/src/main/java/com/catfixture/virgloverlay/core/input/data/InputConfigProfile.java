package com.catfixture.virgloverlay.core.input.data;

import com.catfixture.virgloverlay.ui.common.genAdapter.IAdapterItem;

public class InputConfigProfile implements IAdapterItem {
    private boolean isVisible = true;

    @Override
    public void ToggleVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean IsVisible() {
        return isVisible;
    }

    @Override
    public void SetSpacing(int spacing) {

    }

    @Override
    public int GetSpacing() {
        return 0;
    }

    public String GetName() {
        return "Input profile";
    }
}
