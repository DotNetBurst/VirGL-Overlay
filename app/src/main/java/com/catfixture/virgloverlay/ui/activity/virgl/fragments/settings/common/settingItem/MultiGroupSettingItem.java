package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem;

import com.catfixture.virgloverlay.core.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.MultiGroupEntry;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;


public class MultiGroupSettingItem extends SettingItem {
    private final MultiGroupEntry[] entries;

    public MultiGroupSettingItem(String name, MultiGroupEntry[] entries, String description, IObjectProvider dto, String fieldName) {
        super(Integer.class, name, description, dto, fieldName);
        this.entries = entries;
    }

    @Override
    public int GetViewType() {
        return Const.SETTING_DISPLAY_TYPE_MULTI_GROUP;
    }

    public MultiGroupEntry[] GetEntries() { return entries;}
}
