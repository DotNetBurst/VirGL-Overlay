package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.types.Event;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import com.catfixture.virgloverlay.core.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.ui.common.genAdapter.IMultiViewAdapterItem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@SuppressWarnings("unused")
public abstract class SettingItem implements IMultiViewAdapterItem {
    public static final Event OnAppWideSettingsChanged = new Event();

    protected final IObjectProvider dto;
    protected Field cachedField;
    protected Method cachedSetter;
    protected int spacing;
    private final String description;
    private final String name;
    private boolean isVisible = true;

    public void ToggleVisibility(boolean isVisible) {
        this.isVisible = isVisible;
    }
    @SuppressWarnings("unused")
    public boolean IsVisible() { return isVisible;}

    private Action<Object> onChanged;
    public void OnChanged(Action<Object> onChanged) {
        this.onChanged = onChanged;
    }

    @Override
    public Object GetValue() {
        if ( cachedField == null || dto.get() == null) return null;
        try {
            return cachedField.get(dto.get());
        } catch (IllegalAccessException e) {
            Dbg.Error(e);
        }
        return null;
    }

    public SettingItem(Class<?> clazz, String name, String description, IObjectProvider dto, String fieldName) {
        this.name = name;
        this.description = description;
        this.dto = dto;

        final Class<?> dtoClazz = dto.get().getClass();

        try {
            this.cachedField = dtoClazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            Dbg.Error(e);
        }

        try {
            this.cachedSetter = dtoClazz.getMethod("Set"+ fieldName.substring(0,1).toUpperCase(Locale.ROOT) +
                    fieldName.substring(1), clazz);
        } catch (NoSuchMethodException e) {
            Dbg.Error(e);
        }
    }
    @Override
    public void NotifyChanged(Object o) {
        if ( cachedSetter == null || dto.get() == null) return;
        if (onChanged != null) onChanged.Invoke(o);
        OnAppWideSettingsChanged.notifyObservers();
        try {
            cachedSetter.invoke(dto.get(), o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Dbg.Error(e);
        }
    }

    public String GetName() {
        return name;
    }
    public String GetDescription() {
        return description;
    }

    @Override
    public void SetSpacing(int spacing) {
        this.spacing = spacing;
    }

    @SuppressWarnings("unused")
    @Override
    public int GetSpacing() {
        return spacing;
    }

    private Object customData;
    public <T> void SetCustomData(T customData) {
        this.customData = customData;
    }
    public Object GetCustomData() {
        return this.customData;
    }
}
