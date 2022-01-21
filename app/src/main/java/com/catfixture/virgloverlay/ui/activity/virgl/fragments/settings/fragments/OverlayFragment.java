package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.App.app;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;

public class OverlayFragment extends CoreSettingsFragment {

    public OverlayFragment() {
        super(R.layout.fragment_settings_overlay);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {

        ConfigData cfgData = app.GetConfigData();

        IObjectProvider settingsDtoProvider = cfgData::GetCurrentProfile;

        SwitchSettingItem showControlsOnTopOfOverlay = new SwitchSettingItem("Show controls",
                "show controls, helpful in case of fullscreen shrink", settingsDtoProvider, "showControlsOnTopOfOverlay");
        settingsViewAdapter.AddItem(showControlsOnTopOfOverlay);

        SwitchSettingItem showServicesOnTopOfOverlay = new SwitchSettingItem("Show services [Not impl.]",
                "show services status panel on top of overlay (warn!dbg)", settingsDtoProvider, "showServicesOnTopOfOverlay");
        settingsViewAdapter.AddItem(showServicesOnTopOfOverlay);

        SwitchSettingItem enableToasts = new SwitchSettingItem("Enable toasts",
                "enable annoying toasts, use for info about app", settingsDtoProvider, "enableToasts");
        settingsViewAdapter.AddItem(enableToasts);
    }
}
