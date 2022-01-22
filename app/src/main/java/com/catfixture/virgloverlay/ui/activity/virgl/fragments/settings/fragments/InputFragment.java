package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.App.app;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;

public class InputFragment extends CoreSettingsFragment {

    public InputFragment() {
        super(R.layout.fragment_settings_input);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {
        MainConfigData cfgData = app.GetMainConfigData();
        IObjectProvider settingsDtoProvider = cfgData::GetCurrentProfile;

        SwitchSettingItem enableSystemKeyboard = new SwitchSettingItem("Enable system keyboard [Not impl]",
                "add button to activate system keyboard overlay", settingsDtoProvider, "enableSystemKeyboard");
        enableSystemKeyboard.SetSpacing(15);
        settingsViewAdapter.AddItem(enableSystemKeyboard);

        SwitchSettingItem enableNativeInput = new SwitchSettingItem("Enable native input bridge [Not impl]",
                "allows to use gamepads like native", settingsDtoProvider, "enableNativeInput");
        enableNativeInput.SetSpacing(15);
        settingsViewAdapter.AddItem(enableNativeInput);
    }
}
