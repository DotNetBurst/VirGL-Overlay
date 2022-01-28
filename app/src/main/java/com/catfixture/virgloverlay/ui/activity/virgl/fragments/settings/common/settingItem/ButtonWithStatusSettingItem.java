package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem;

import android.view.View;


import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import java.util.Observer;

public class ButtonWithStatusSettingItem extends SettingItem {
    private int statusDrawable;
    private String statusMessage;
    private Event event = new Event();
    private boolean actionButtonVisible;

    public ButtonWithStatusSettingItem(String name, String description, IObjectProvider dto, String fieldName) {
        super(Boolean.class, name, description, dto, fieldName);
    }

    @Override
    public int GetViewType() {
        return Const.SETTING_DISPLAY_TYPE_BUTTON_WITH_STATUS;
    }


    public void SetStatusDrawable(int statusDrawable) {
        this.statusDrawable = statusDrawable;
    }
    public void SetNoStatusDrawable() {
        this.statusDrawable = -1;
    }

    public void SetActionButtonVisible(boolean actionButtonVisible) {
        this.actionButtonVisible = actionButtonVisible;
    }
    public boolean GetActionButtonVisible() {
        return actionButtonVisible;
    }


    public int GetStatusDrawable() {
        return statusDrawable;
    }

    public void AddAction(Observer r) {
        event.addObserver(r);
    }

    public void ExecuteAction() {
        event.notifyObservers();
    }

    public void SetStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String GetStatusMessage() {
        return statusMessage;
    }
}
