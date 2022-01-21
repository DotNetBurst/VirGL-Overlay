package com.catfixture.virgloverlay.data;


import static com.catfixture.virgloverlay.core.App.app;

import com.catfixture.virgloverlay.core.utils.objProvider.ITypedProvider;
import com.catfixture.virgloverlay.core.utils.types.Event;

import java.util.ArrayList;
import java.util.List;

public class ConfigData implements ITypedProvider<Event> {
    public final transient Event onChanged = new Event();
    public final List<ConfigProfile> profiles = new ArrayList<>();
    public boolean automaticMode;
    public int lastSelectedMainTab;
    public int lastSelectedSettingsTab;
    public int currentProfile;

    public boolean sELinuxPermissive;

    public void SetSELinuxPermissive (Boolean sELinuxPermissive) {this.sELinuxPermissive=sELinuxPermissive; Save();}


    public void SetAutomaticMode(boolean b) {
        automaticMode = b;
        Save();
    }
    public void SetLastSelectedMainTab(int tab) {
        lastSelectedMainTab = tab;
        Save();
    }

    public void SetLastSelectedSettingsTab(int tab) {
        lastSelectedSettingsTab = tab;
        Save();
    }

    public void SetCurrentProfile(int id) {
        if ( currentProfile >= profiles.size())
            currentProfile = 0;
        else
            currentProfile = id;
        Save();
    }

    public void AddProfile(ConfigProfile newCfgProf) {
        profiles.add(newCfgProf);
        Save();
    }

    public void RemoveProfile(int i) {
        profiles.remove(i);
        Save();
    }

    public boolean HasCurrentProfile() {
        return !profiles.isEmpty() && currentProfile >= 0 && currentProfile < profiles.size();
    }

    public ConfigProfile GetCurrentProfile() {
        return profiles.get(currentProfile);
    }

    public ConfigProfile FindProfileByID(Integer i) {
        int id = 0;
        for (ConfigProfile profile : profiles) {
            if ( id++ == i)
                return profile;
        }
        return null;
    }

    private void Save() {
        onChanged.notifyObservers();
    }

    @Override
    public Event get() {
        return onChanged;
    }
}
