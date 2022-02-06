package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.utils.android.Installer.CopyInstallerToDownload;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.android.FileUtils;
import com.catfixture.virgloverlay.core.utils.android.Installer;
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
    private int systemInstalledState;

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
        UpdateSettingItem(R.drawable.vgob_not_fully_installed, "Not installed", "INSTALL", true);
        statusedButtonSettingItem.SetNoStatusDrawable();

        UpdateVGOBRidgeInstalleState(getContext(), null);
        UpdateVGOBRidgeInstallButton();

        statusedButtonSettingItem.AddAction((observable, o) -> {
            if ( currentInstallMode) {
                ConfirmDialog.Show(getContext(), "Service installation",
                        "Auto mode:\n" +
                                "Service will be installed in all Exagear containers\n" +
                                "Note! Auto install requires root\n" +
                                "\n" +
                                "Manual mode:\n" +
                                "Service installer (Download/VGOBridge/install.bat) will be copied to Download folder\n" +
                                "Start Exagear and run installer, it will install service automatically\n" +
                                "\n" +
                                "Note! You can fully uninstall service later", "Auto", () -> {
                    Installer.InstallVGO(getActivity(), true, handler, this::OnInstallComplete);
                }, "Manual", () -> {
                    CopyInstallerToDownload(getContext(), handler);
                    OnInstallComplete();
                });
            } else {
                ConfirmDialog.Show(getContext(), "Service uninstallation", "Do you really want to uninstall VGOBridge service, " +
                        "it will be uninstalled from all containers?\n" +
                        "Please note! ROOT REQUIRED to uninstall", "Uninstall now", () -> {
                    Installer.InstallVGO(getActivity(), false, handler, this::OnInstallComplete);
                }, "Close", null);
            }
        });
    }

    private void OnInstallComplete() {
        UpdateVGOBRidgeInstalleState(getContext(), () -> {
            UpdateAll();
        });
    }

    private void UpdateSettingItem(int draw, String statMsg, String actBtnMsg, boolean currMode) {
        statusedButtonSettingItem.SetStatusDrawable(draw);
        statusedButtonSettingItem.SetStatusMessage(statMsg);
        statusedButtonSettingItem.SetActionButtonMessage(actBtnMsg);
        currentInstallMode = currMode;

    }

    private void UpdateVGOBRidgeInstalleState(Context context, Runnable onDone) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        final String checkCmd = "su -c sh " + filesDir + "/installCheckScript.sh";
        ProcUtils.RunSystemCommandWithOutput(checkCmd, res -> {
            Dbg.Msg("VGOB installed state : " + res);
            if ( res > 2 || res < 0) {
                handler.post(() -> {
                    boolean installerExists = FileUtils.CheckFileExists(Environment.DIRECTORY_DOWNLOADS + "/VGOBridge/install.bat");
                    systemInstalledState = installerExists ? 3 : 13;
                    UpdateVGOBRidgeInstallButton();
                    if ( onDone != null) onDone.run();
                });
            } else {
                handler.post(() -> {
                    systemInstalledState = res;
                    UpdateVGOBRidgeInstallButton();
                    if ( onDone != null) onDone.run();
                });
            }
        });
    }

    private void UpdateVGOBRidgeInstallButton() {
        if ( systemInstalledState == 0) {
            UpdateSettingItem(R.drawable.vgob_installed, "Fully installed", "UNINSTALL", false);
        } else if (systemInstalledState == 1) {
            UpdateSettingItem(R.drawable.vgob_not_fully_installed, "Partially installed", "REINSTALL", true);
        } else if (systemInstalledState == 3) {
            UpdateSettingItem(R.drawable.vgob_not_fully_installed, "Manual", "REINSTALL", true);
        } else {
            UpdateSettingItem(R.drawable.vgob_not_fully_installed, "Not installed", "INSTALL", true);
            statusedButtonSettingItem.SetNoStatusDrawable();
        }
    }
}
