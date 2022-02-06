package com.catfixture.virgloverlay.core.utils.android;

import static com.catfixture.virgloverlay.core.utils.android.FileUtils.CopyRawToTemp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ProcUtils;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Installer {
    public static void InstallVGO (Activity context, boolean isInstallMode, Handler handler, Runnable onDone) {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage((isInstallMode ? "Installing" : "Uninstalling") + "... please wait!\n");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.show();

        String filesDir = context.getFilesDir().getAbsolutePath();
        CopyRawToTemp(context, R.raw.vgo_bridge_service_android_root_install_check, "/installCheckScript.sh");
        File installerScript = CopyRawToTemp(context, R.raw.vgo_bridge_service_android_root_installer, "/installerScript.sh");
        File binary = CopyRawToTemp(context, R.raw.vgob, "/vgob.exe");
        File launcher = CopyRawToTemp(context, R.raw.vo, "/vo");

        final String installCmd = "su -c sh " + filesDir + "/installerScript.sh " + (isInstallMode ? 0 : 1) + " " + filesDir;
        ProcUtils.RunSystemCommandWithOutput(installCmd, obj -> {
            Dbg.Msg("Installer returned : " + obj);
            handler.post(() -> {
                pd.setMessage("Done!");
                if (obj == 0) {
                    ConfirmDialog.Show(context, isInstallMode ? "Installed!" : "Uninstalled!", "VGOBridge was successfully " +
                            (isInstallMode ? "installed!" : "uninstalled!"));
                } else {
                    ConfirmDialog.Show(context, "Not installed!", "VGOBridge wasn't installed, maybe your device not rooted!\n" +
                            "As VGOBridge doesn't require root you can install it manually.\n");
                }
                onDone.run();
                pd.dismiss();
            });
            binary.delete();
            installerScript.delete();
            launcher.delete();
        });
    }

    public static byte[] GetRawBytes(Context context, int id) {
        InputStream is = context.getResources().openRawResource(id);

        try {
            int targetSize = is.available();
            if ( targetSize > 0) {
                byte[] data = new byte[targetSize];
                if (is.read(data) == targetSize) {
                    return data;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void CopyInstallerToDownload(Context context, Handler handler) {
        String path = Environment.DIRECTORY_DOWNLOADS + "/VGOBridge";

        FileUtils.RemoveAllFilesInDir(context, path);
        boolean installDone = FileUtils.WriteFileToDownloads(context, path,"install.bat",
                    GetRawBytes(context, R.raw.vgo_bridge_service_windows_installer)) |
                FileUtils.WriteFileToDownloads(context, path,"vo", GetRawBytes(context, R.raw.vo)) |
                FileUtils.WriteFileToDownloads(context, path,"vgob.exe", GetRawBytes(context, R.raw.vgob));

        if (installDone) {
            ConfirmDialog.Show(context, "Done!", "Installer saved to Download folder\n" +
                    "Now you can install it from Exagear Windows emulator!");
        } else {
            ConfirmDialog.Show(context, "Error!", "Installed not saved, try to download from github...");
        }
    }
}
