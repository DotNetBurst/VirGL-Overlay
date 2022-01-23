package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.Devices.TOUCH_CONTROLS_DEVICE;

import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.objProvider.ITypedProvider;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.data.ConfigProfile;

import java.util.ArrayList;
import java.util.List;

public class InputConfigData implements ITypedProvider<Event> {
    public final transient Event onChanged = new Event();

    public int internalId;

    public int currentProfile = -1;
    public List<InputConfigProfile> profiles = new ArrayList<>();

    public int inputDevice = TOUCH_CONTROLS_DEVICE;
    public Int2 touchEditorPosition = Int2.Zero;
    public float uiOpacity = 1f;

    public void SetTouchEditorPosition(Int2 touchEditorPosition) {
        this.touchEditorPosition = touchEditorPosition; Save();}

    public void SetInputDevice(int inputDevice) { this.inputDevice = inputDevice; Save();}

    private void Save() {
        onChanged.notifyObservers();
    }


    public void AddProfile(InputConfigProfile newCfgProf) {
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

    public InputConfigProfile GetCurrentProfile() {
        return profiles.get(currentProfile);
    }

    public InputConfigProfile FindProfileByID(Integer i) {
        int id = 0;
        for (InputConfigProfile profile : profiles) {
            if ( id++ == i)
                return profile;
        }
        return null;
    }

    @Override
    public Event get() {
        return onChanged;
    }

    public void RemoveCurrentProfile() {
        if (HasCurrentProfile()) {
            profiles.remove(currentProfile);
            Save();
        }
    }

    public void SetCurrentProfile(int i) {
        currentProfile = i;
        Save();
    }

    public int GetInternalId() {
        return internalId++;
    }

    public void SetUiOpacity(float v) {
        this.uiOpacity = v;
        Save();
    }
}
