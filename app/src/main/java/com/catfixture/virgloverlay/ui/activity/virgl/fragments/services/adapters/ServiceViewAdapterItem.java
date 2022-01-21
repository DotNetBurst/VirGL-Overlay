package com.catfixture.virgloverlay.ui.activity.virgl.fragments.services.adapters;

import com.catfixture.virgloverlay.core.impl.handles.IService;
import com.catfixture.virgloverlay.ui.common.genAdapter.IAdapterItem;

@SuppressWarnings("unused")
public class ServiceViewAdapterItem implements IAdapterItem {
    private final IService service;
    private boolean isVisible = true;
    private int spacing;

    public ServiceViewAdapterItem(IService service) {
        this.service = service;
    }

    public IService GetService() {
        return service;
    }

    @SuppressWarnings("unused")
    @Override
    public void ToggleVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @SuppressWarnings("unused")
    public boolean IsVisible() {
        return isVisible;
    }

    @SuppressWarnings("unused")
    @Override
    public void SetSpacing(int spacing) {
        this.spacing = spacing;
    }

    @SuppressWarnings("unused")
    @Override
    public int GetSpacing() {
        return spacing;
    }
}
