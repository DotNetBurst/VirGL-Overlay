package com.catfixture.virgloverlay.ui.common.genAdapter;

public interface IMultiViewAdapterItem extends IAdapterItem {
    int GetViewType();
    void NotifyChanged(Object o);
    Object GetValue();
}
