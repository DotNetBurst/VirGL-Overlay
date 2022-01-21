package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem;

import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;

public class TextSettingItem extends SettingItem {
    public TextSettingItem(String name, String description, IObjectProvider dto, String fieldName) {
        super(String.class, name, description, dto, fieldName);
    }

    @Override
    public int GetViewType() {
        return Const.SETTING_DISPLAY_TYPE_TEXT;
    }
}