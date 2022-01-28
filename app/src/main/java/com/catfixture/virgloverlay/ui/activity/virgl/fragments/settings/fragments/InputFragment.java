package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.App.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputFragment extends CoreSettingsFragment {
    private ButtonWithStatusSettingItem statusedButtonSettingItem;
    private Handler handler;

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

        UpdateVGOBRidgeInstalleState(getContext(), null);

        statusedButtonSettingItem.AddAction((observable, o) -> {
            InstallVGO(getContext());
        });

    }

    private void UpdateVGOBRidgeInstalleState(Context context, Runnable onDone) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        final String checkCmd = "su -c sh " + filesDir + "/installCheckScript.sh";
        ProcUtils.RunSystemCommand(checkCmd, res -> {
            handler.post(() -> {
                statusedButtonSettingItem.SetActionButtonVisible(true);
                if ( res == 0) {
                    Dbg.Msg( "WEWWE " + 1);
                    statusedButtonSettingItem.SetStatusDrawable(R.drawable.vgob_installed);
                    statusedButtonSettingItem.SetStatusMessage("Fully installed");
                    statusedButtonSettingItem.SetActionButtonVisible(false);
                } else if (res == 1) {
                    Dbg.Msg( "WEWWE " + 2);
                    statusedButtonSettingItem.SetStatusDrawable(R.drawable.vgob_not_fully_installed);
                    statusedButtonSettingItem.SetStatusMessage("Partially installed");
                } else {
                    Dbg.Msg( "WEWWE " + 3);
                    statusedButtonSettingItem.SetNoStatusDrawable();
                    statusedButtonSettingItem.SetStatusMessage("Not installed");
                }
                if ( onDone != null) onDone.run();
            });
        });
    }

    public File CopyRawToTemp (Context context, int rawId, String targetName) {
        InputStream rawIs = context.getResources().openRawResource(rawId);
        File filesDir = context.getFilesDir();
        File tempFile = new File(filesDir, targetName);
        try {
            if(tempFile.exists() && !tempFile.delete())
                throw new IOException("Cant delete file");

            if (tempFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(tempFile);

                byte[] tempBuffer = new byte[1024];
                int readen = 0;
                while((readen = rawIs.read(tempBuffer)) > 0) {
                    fos.write(tempBuffer, 0, readen);
                }
                fos.flush();
                fos.close();
                Dbg.Msg("Tempfile " + tempFile.getAbsolutePath() + " created");
                return tempFile;
            } else throw new IOException("Cant create file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            rawIs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void InstallVGO (Context context) {
        String filesDir = context.getFilesDir().getAbsolutePath();
        File installCheckScript = CopyRawToTemp(context, R.raw.vgo_bridge_service_install_check, "/installCheckScript.sh");
        File installerBinary = CopyRawToTemp(context, R.raw.vgo_bridge_service_binary, "/installerBinary.exe");
        File installerScript = CopyRawToTemp(context, R.raw.vgo_bridge_service_installer, "/installerScript.sh");

        final String installCmd = "su -c sh " + filesDir + "/installerScript.sh " + filesDir;
        Dbg.Msg("Running installer : " + installCmd);
        ProcUtils.RunSystemCommandWithOutput(installCmd, obj -> {
            Dbg.Msg("Installer returned : " + obj);
            handler.post(() -> {
                if (obj == 0) {
                    ConfirmDialog.Show(context, "Installed!", "VGOBridge was successfully installed!", "Ok", () -> {
                    }, "Close", null);
                } else {
                    ConfirmDialog.Show(context, "Not installed!", "VGOBridge wasn't installed, maybe your device not rooted!", "Ok", () -> {
                    }, "Close", null);
                }
                UpdateVGOBRidgeInstalleState(context, () -> {
                    UpdateAll();
                });
            });
            installerBinary.delete();
            installerScript.delete();
        });

        //RUN INSTALLER BASH
    }
}
