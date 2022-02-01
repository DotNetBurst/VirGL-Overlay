package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.AppContext.app;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.MultiGroupEntry;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.MultiGroupSettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.TextSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;


public class RenderingFragment extends CoreSettingsFragment {
    private Event updater;

    public RenderingFragment() {
        super(R.layout.fragment_settings_rendering);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {

        MainConfigData cfgData = app.GetMainConfigData();

        IObjectProvider settingsDtoProvider = cfgData::GetCurrentProfile;

        SwitchSettingItem useSocketSI = new SwitchSettingItem("Use GLES 3.1",
                "use OpenGLES instead of OpenGL API", settingsDtoProvider, "useGLES");
        settingsViewAdapter.AddItem(useSocketSI);

        SwitchSettingItem useS3TC = new SwitchSettingItem("Use S3TC",
                "enables S3TC (DXTn) essential to run some games", settingsDtoProvider, "useS3TC");
        useS3TC.SetSpacing(15);
        settingsViewAdapter.AddItem(useS3TC);

        SwitchSettingItem useStencilMirror = new SwitchSettingItem("Use stencil mirror",
                "enables stencil mirror hack", settingsDtoProvider, "useStencilMirror");
        useStencilMirror.SetSpacing(15);
        settingsViewAdapter.AddItem(useStencilMirror);

        SwitchSettingItem useVertexShaderHack = new SwitchSettingItem("Use vertex shader hack",
                "hack shaders might help to run some games", settingsDtoProvider, "useVertexShaderHack");
        useVertexShaderHack.SetSpacing(15);
        settingsViewAdapter.AddItem(useVertexShaderHack);

        SwitchSettingItem useFragmentShaderHack = new SwitchSettingItem("Use fragment shader hack",
                "hacks some shader logic, helps run (Outlast, GTA IV)", settingsDtoProvider, "useFragmentShaderHack");
        settingsViewAdapter.AddItem(useFragmentShaderHack);


        SwitchSettingItem useBlendHack = new SwitchSettingItem("Use blend hack",
                "hacks blending in games, causes color glitches", settingsDtoProvider, "useBlendHack");
        settingsViewAdapter.AddItem(useBlendHack);


        SwitchSettingItem useViewportShrink = new SwitchSettingItem("Use viewport shrink",
                "enable shrink and use lower res to run game faster", settingsDtoProvider, "useViewportShrink");
        useViewportShrink.SetSpacing(15);
        settingsViewAdapter.AddItem(useViewportShrink);

        MultiGroupEntry[] entrys = new MultiGroupEntry[] {
                new MultiGroupEntry(0, "Rect", "Manually set width, height of viewport"),
                new MultiGroupEntry(1, "Immersive", "Use with immersive mode (system tab)"),
                new MultiGroupEntry(2, "Scissored", "Shrinks to system panel"),
        };
        MultiGroupSettingItem viewportShrinkType = new MultiGroupSettingItem("Viewport shrink type", entrys,
                "", settingsDtoProvider, "viewportShrinkType");
        settingsViewAdapter.AddItem(viewportShrinkType);

        SwitchSettingItem centerViewportRect = new SwitchSettingItem("Center viewport rect",
                "transforms position of rect to center of screen", settingsDtoProvider, "centerViewportRect");
        settingsViewAdapter.AddItem(centerViewportRect);

        TextSettingItem customShrinkResolution = new TextSettingItem("Shrink resolution",
                "custom shrinking resolution ([Number]x[Number])", settingsDtoProvider, "customShrinkResolution");
        settingsViewAdapter.AddItem(customShrinkResolution);

        Action<Integer> toggleGroupVis = (i) -> {
            viewportShrinkType.ToggleVisibility(i >= 0);
            centerViewportRect.ToggleVisibility(i == 0);
            customShrinkResolution.ToggleVisibility(i == 0);
        };

        useViewportShrink.OnChanged(b -> {
            updater.notifyObservers(b);
            UpdateAll();
        });

        viewportShrinkType.OnChanged(b -> {
            toggleGroupVis.Invoke((Integer) b);
            UpdateAll();
        });

        updater = new Event();
        updater.addObserver((observable, o) -> {
            boolean useVShrink = (boolean) o;
            viewportShrinkType.ToggleVisibility(useVShrink);
            if (useVShrink) toggleGroupVis.Invoke((Integer) viewportShrinkType.GetValue());
            else toggleGroupVis.Invoke(-1);
        });
        updater.notifyObservers(useViewportShrink.GetValue());
    }
}
