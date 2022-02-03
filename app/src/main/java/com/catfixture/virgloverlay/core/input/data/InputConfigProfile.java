package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.AppContext.app;

import com.catfixture.virgloverlay.ui.common.genAdapter.IAdapterItem;

import java.util.ArrayList;
import java.util.List;

public class InputConfigProfile implements IAdapterItem {
    private transient boolean isVisible = true;
    public List<InputTouchControlElementData> touchControlElements = new ArrayList<>();

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

    public void AddControlElement(int id) {
        InputTouchControlElementData elementData = new InputTouchControlElementData();
        elementData.SetId(id);
        touchControlElements.add(elementData);
        Save();
    }

    private void Save() {
        app.SaveInputConfig();
    }

    public void RemoveControlElement(int id) {
        for (InputTouchControlElementData touchControlElement : touchControlElements) {
            if ( touchControlElement.id == id) {
                touchControlElements.remove(touchControlElement);
                Save();
                return;
            }
        }
    }
}
