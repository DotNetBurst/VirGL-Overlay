package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.AppContext.app;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;

public class OverlayFragment extends CoreSettingsFragment {

    public OverlayFragment() {
        super(R.layout.fragment_settings_overlay);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {

        MainConfigData cfgData = app.GetMainConfigData();

        IObjectProvider settingsDtoProvider = cfgData::GetCurrentProfile;

        SwitchSettingItem showControlsOnTopOfOverlay = new SwitchSettingItem("Show controls",
                "show controls, helpful in case of fullscreen shrink", settingsDtoProvider, "showControlsOnTopOfOverlay");
        settingsViewAdapter.AddItem(showControlsOnTopOfOverlay);

        SwitchSettingItem enableToasts = new SwitchSettingItem("Enable toasts",
                "enable annoying toasts, use for info about app", settingsDtoProvider, "enableToasts");
        settingsViewAdapter.AddItem(enableToasts);
    }
}
