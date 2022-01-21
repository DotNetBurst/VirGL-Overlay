package com.catfixture.virgloverlay.core.input;


import android.content.Context;

import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.windows.editor.OverlaySettingsPanel;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.data.GenericConfig;

public class InputController {
    private final Context context;
    private GenericConfig<InputConfig> config;

    public InputController (Context context) {
        this.context = context;
        config = new GenericConfig<>( context.getFilesDir()+"/input.json", InputConfig.class);
    }

    public void SetInputDevice(int currentInputDevice) {
        config.GetData().SetInputDevice(currentInputDevice);
        AndroidUtils.BroadcastBuilder("inputDeviceChanged")
                .Send(context);
    }

    public void Create() {
        IInputProvider mainInputProvider = new MainInputProvider();
        IInputDevice inputDevice = mainInputProvider.ResolveDevice(context, GetConfigData());
        inputDevice.Show();

        OverlaySettingsPanel overlaySettingsPanel = new OverlaySettingsPanel(context, GetConfigData());
        overlaySettingsPanel.SetAlpha(0.5f);
    }

    public InputConfig GetConfigData() {
        return config.GetData();
    }
}
