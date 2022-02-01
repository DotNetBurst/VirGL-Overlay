package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.utils.android.AndroidUtils.CopyRawToTemp;

import android.content.Context;
import android.os.Handler;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.objProvider.IObjectProvider;
import com.catfixture.virgloverlay.core.utils.process.ProcUtils;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.ButtonWithStatusSettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.SwitchSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;

import java.io.File;

public class InputFragment extends CoreSettingsFragment {
    private ButtonWithStatusSettingItem statusedButtonSettingItem;
    private Handler handler;
    private boolean currentInstallMode;

    public InputFragment() {
        super(R.layout.fragment_settings_input);
    }

    @Override
    protected void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter) {
        handler = new Handler();

        MainConfigData cfgData = app.GetMainConfigData();
        IObjectProvider settingsDtoProvider = cfgData::GetCurrentProfile;

        SwitchSettingItem enableNativeInput = new SwitchSettingItem("Enable native input bridge",
                "allows to use gamepads like native", settingsDtoProvider, "enableNativeInput");
        enableNativeInput.SetSpacing(15);
        enableNativeInput.OnChanged(obj -> {
            statusedButtonSettingItem.ToggleVisibility((Boolean)obj);
            UpdateAll();
        });
        settingsViewAdapter.AddItem(enableNativeInput);

        statusedButtonSettingItem = new ButtonWithStatusSettingItem("VGOBridge service",
                "Required to use native input", settingsDtoProvider, "enableNativeInput");
        statusedButtonSettingItem.SetSpacing(15);
        settingsViewAdapter.AddItem(statusedButtonSettingItem);
        statusedButtonSettingItem.ToggleVisibility((Boolean)enableNativeInput.GetValue());

        UpdateVGOBRidgeInstalleState(getContext(), null);

        statusedButtonSettingItem.AddAction((observable, o) -> {
            if ( currentInstallMode) {

                ConfirmDialog.Show(getContext(), "Service installation", "Do you really want to install VGOBridge service, " +
                        "it will be installed in all containers?\n" +
                        "Please note! ROOT REQUIRED to install\n" +
                        "VGOBridge service size: 15KB", "Install now", () -> {
                    InstallVGO(getContext(), true);
                }, "Close", null);
            } else {
                ConfirmDialog.Show(getContext(), "Service uninstallation", "Do you really want to uninstall VGOBridge service, " +
                        "it will be uninstalled in all containers?\n" +
                        "Please note! ROOT REQUIRED to uninstall", "Uninstall now", () -> {
                    InstallVGO(getContext(), false);
                }, "Close", null);
            }
        });

    }

    private void UpdateVGOBRidgeInstalleState(Context context, Runnable onDone) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        final String checkCmd = "su -c sh " + filesDir + "/installCheckScript.sh";
        ProcUtils.RunSystemCommand(checkCmd, res -> {
            handler.post(() -> {
                if ( res == 0) {
                    Dbg.Msg( "WEWWE " + 1);
                    statusedButtonSettingItem.SetStatusDrawable(R.drawable.vgob_installed);
                    statusedButtonSettingItem.SetStatusMessage("Fully installed");
                    statusedButtonSettingItem.SetActionButtonMessage("UNINSTALL");
                    currentInstallMode = false;
                } else if (res == 1) {
                    Dbg.Msg( "WEWWE " + 2);
                    statusedButtonSettingItem.SetStatusDrawable(R.drawable.vgob_not_fully_installed);
                    statusedButtonSettingItem.SetStatusMessage("Partially installed");
                    statusedButtonSettingItem.SetActionButtonMessage("REINSTALL");
                    currentInstallMode = true;
                } else {
                    Dbg.Msg( "WEWWE " + 3);
                    statusedButtonSettingItem.SetNoStatusDrawable();
                    statusedButtonSettingItem.SetStatusMessage("Not installed");
                    statusedButtonSettingItem.SetActionButtonMessage("INSTALL");
                    currentInstallMode = true;
                }
                if ( onDone != null) onDone.run();
            });
        });
    }



    public void InstallVGO (Context context, boolean isInstallMode) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        File installCheckScript = CopyRawToTemp(context, R.raw.vgo_bridge_service_install_check, "/installCheckScript.sh");
        File installerScript = CopyRawToTemp(context, R.raw.vgo_bridge_service_installer, "/installerScript.sh");
        File binary = CopyRawToTemp(context, R.raw.vgob, "/vgob.exe");
        File launcher = CopyRawToTemp(context, R.raw.vgo, "/vgo");

        final String installCmd = "su -c sh " + filesDir + "/installerScript.sh " + (isInstallMode ? 0 : 1) + " " + filesDir;
        Dbg.Msg("Running installer : " + installCmd);
        ProcUtils.RunSystemCommandWithOutput(installCmd, obj -> {
            Dbg.Msg("Installer returned : " + obj);
            handler.post(() -> {
                if (obj == 0) {
                    ConfirmDialog.Show(context, isInstallMode ? "Installed!" : "Uninstalled!", "VGOBridge was successfully " +
                            (isInstallMode ? "installed!" : "uninstalled!"), "Ok", () -> {
                    }, "Close", null);
                } else {
                    ConfirmDialog.Show(context, "Not installed!", "VGOBridge wasn't installed, maybe your device not rooted!", "Ok", () -> {
                    }, "Close", null);
                }
                UpdateVGOBRidgeInstalleState(context, () -> {
                    UpdateAll();
                });
            });
            binary.delete();
            installerScript.delete();
            launcher.delete();
        });

        //RUN INSTALLER BASH
    }
}
