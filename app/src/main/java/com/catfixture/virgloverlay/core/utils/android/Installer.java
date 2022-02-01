package com.catfixture.virgloverlay.core.utils.android;

import static com.catfixture.virgloverlay.core.utils.android.AndroidUtils.CopyRawToTemp;

import android.content.Context;
import android.os.Handler;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ProcUtils;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;

import java.io.File;

public class Installer {
    public static void InstallVGO (Context context, boolean isInstallMode, Handler handler, Runnable onDone) {
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
                onDone.run();
            });
            binary.delete();
            installerScript.delete();
            launcher.delete();
        });

        //RUN INSTALLER BASH
    }
}
