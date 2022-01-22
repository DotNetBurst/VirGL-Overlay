package com.catfixture.virgloverlay.core.input.data;

import static com.catfixture.virgloverlay.core.App.app;

import com.catfixture.virgloverlay.ui.common.genAdapter.IAdapterItem;

import java.util.ArrayList;
import java.util.List;

public class InputConfigProfile implements IAdapterItem {
    private transient boolean isVisible = true;
    public List<InputTouchControlElement> touchControlElements = new ArrayList<>();

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
        InputTouchControlElement el = new InputTouchControlElement();
        touchControlElements.add(el);
        el.SetId(id);
    }
    public void RemoveControlElement(int id) {
        for (InputTouchControlElement touchControlElement : touchControlElements) {
            if ( touchControlElement.id == id) {
                touchControlElements.remove(touchControlElement);
                return;
            }
        }
    }
}
