package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.android.AndroidUtils;
import com.catfixture.virgloverlay.core.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.core.objProvider.SimpleObjectProvider;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.IntSettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.TextSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;

public class SystemFragment extends CoreSettingsFragment {

    public SystemFragment() {
        super(R.layout.fragment_settings_system);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {
        ConfigData cfgData = app.GetConfigData();

        IObjectProvider globalSettingsProfileProvider = new SimpleObjectProvider(() -> cfgData);
        IObjectProvider settingsDtoProvider = new SimpleObjectProvider(cfgData::GetCurrentProfile);

        SwitchSettingItem useSocket = new SwitchSettingItem("Use socket [ROOT]",
                "use socket as transport pipeline", settingsDtoProvider, "useSocket");
        settingsViewAdapter.AddItem(useSocket);

        TextSettingItem socketPath = new TextSettingItem("Socket path",
                "/tmp/.virgl_test", settingsDtoProvider, "socketPath");
        settingsViewAdapter.AddItem(socketPath);

        TextSettingItem ringPath = new TextSettingItem("Ring buffer path [Not impl.]",
                "/dev/shm", settingsDtoProvider, "ringBufferPath");
        settingsViewAdapter.AddItem(ringPath);

        socketPath.ToggleVisibility((Boolean) useSocket.GetValue());
        ringPath.ToggleVisibility(!(Boolean)useSocket.GetValue());
        useSocket.OnChanged((b) -> {
            socketPath.ToggleVisibility((Boolean)b);
            ringPath.ToggleVisibility(!(Boolean)b);
            UpdateAll();
        });

        SwitchSettingItem useMultithreadedEGLAccess = new SwitchSettingItem("Use multithreaded EGL access [Not impl.]",
                "enable multithreaded native decoder", settingsDtoProvider, "useMultithreadedEGLAccess");
        settingsViewAdapter.AddItem(useMultithreadedEGLAccess);
        useMultithreadedEGLAccess.SetSpacing(15);

        IntSettingItem eglAccessMaxThreads = new IntSettingItem("Max threads",
                "Limit number of threads, might be usefull is some cases", settingsDtoProvider, "eglAccessMaxThreads");
        settingsViewAdapter.AddItem(eglAccessMaxThreads);

        eglAccessMaxThreads.ToggleVisibility((Boolean) useMultithreadedEGLAccess.GetValue());
        useMultithreadedEGLAccess.OnChanged((b) -> {
            eglAccessMaxThreads.ToggleVisibility((Boolean)b);
            UpdateAll();
        });

        SwitchSettingItem useImmersiveMode = new SwitchSettingItem("Use immersive mode [ROOT]",
                "Hide system panel, requires ROOT", settingsDtoProvider, "useImmersiveMode");
        useImmersiveMode.SetSpacing(15);
        settingsViewAdapter.AddItem(useImmersiveMode);
        useImmersiveMode.OnChanged(obj -> TrySetImmersiveMode((ConfigProfile) settingsDtoProvider.get(),
                (Boolean)obj, true));
        TrySetImmersiveMode((ConfigProfile)settingsDtoProvider.get(), (Boolean) useImmersiveMode.GetValue(), false);


        SwitchSettingItem sELinuxPermissive = new SwitchSettingItem("SELinux permissive mode [GLOBAL, ROOT]",
                "Required to use UNIX socket", globalSettingsProfileProvider, "sELinuxPermissive");
        sELinuxPermissive.SetSpacing(15);
        settingsViewAdapter.AddItem(sELinuxPermissive);
        sELinuxPermissive.OnChanged(obj -> TrySetSELinuxPermissive(cfgData, (Boolean)obj, true));
        TrySetSELinuxPermissive(cfgData, cfgData.sELinuxPermissive, false);

        SwitchSettingItem showOverlayByProcess = new SwitchSettingItem("Show overlay by process [Not impl.]",
                "binds to system process to handle its state", settingsDtoProvider, "showOverlayByProcess");
        showOverlayByProcess.SetSpacing(15);
        settingsViewAdapter.AddItem(showOverlayByProcess);


        TextSettingItem overlayProcessName = new TextSettingItem("Process name",
                "default : com.eltechs.ed", settingsDtoProvider, "overlayProcessName");
        overlayProcessName.SetSpacing(15);
        settingsViewAdapter.AddItem(overlayProcessName);

        overlayProcessName.ToggleVisibility((Boolean) showOverlayByProcess.GetValue());
        showOverlayByProcess.OnChanged(obj -> {
            overlayProcessName.ToggleVisibility((Boolean)obj);
            UpdateAll();
        });
    }

    private void TrySetImmersiveMode(ConfigProfile configProfile, boolean useImmersive, boolean showDialog) {
        if (!useImmersive && !showDialog) return;

        if (useImmersive) {
            AndroidUtils.ForceAppToImmersive(Const.EXAGEAR_APP_PACKAGE, (r) -> {
                if ( r != 0) {
                    Activity activity = getActivity();
                    if ( activity != null) {
                        activity.runOnUiThread(() -> {
                            ConfirmDialog.Show(getActivity(), "Can't get immersive mode",
                                    "This is probably because your device isn't rooted.",
                                    "Ok", () -> {}, "Close", null);
                            configProfile.SetUseImmersiveMode(false);
                            UpdateAll();
                        });
                    } else Log.e(APP_TAG, "Activity null error");
                }
            });
        } else {
            AndroidUtils.ForceAppToNormal(Const.EXAGEAR_APP_PACKAGE, (r) -> {
            });
        }
    }

    private void FallbackSE (ConfigData cfgData) {
        View view = getView();
        if ( view != null) {
            view.post(() -> {
                cfgData.SetSELinuxPermissive(false);
                UpdateAll();
                ConfirmDialog.Show(getContext(), "Can't get permissive mode",
                        "This is probably because your device isn't rooted.",
                        "Ok", ()->{}, "Close", null);
            });
        } else Log.e(APP_TAG, "View null error");
    }

    private void TrySetSELinuxPermissive(ConfigData cfgData, boolean is, boolean showSuccessDialog) {
        if (!is && !showSuccessDialog) return;

        AndroidUtils.SetSELinuxPermissive(is, (Integer r) -> {
            if(r == 0) {
                AndroidUtils.CheckSELinuxPermissive((isPermissive) -> {
                    if (isPermissive && showSuccessDialog && is) {
                        View view = getView();
                        if ( view != null) {
                            view.post(() -> ConfirmDialog.Show(getContext(), "Done!",
                                    "You are in permissive mode!", "Ok", () -> {
                                    }, "Close", null));
                        }
                    } else if (!isPermissive && is) {
                        FallbackSE(cfgData);
                    }
                });
            } else {
                FallbackSE(cfgData);
            }
        });
    }
}
