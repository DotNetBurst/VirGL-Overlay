package com.catfixture.virgloverlay.ui.activity.virgl.fragments.about;

import android.content.Context;
import android.os.Environment;

import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.core.debug.email.ShareDialog;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;

public class DevRequestHelper {
    public static void SendDevRequest(Context context) {
        //final String deviceInfoName = "device.info";
        final String appLogName = "app.log";
        final String exagearErrPath = AndroidUtils.InExternalStorage( "x86-stderr.txt");
        final String exagearLogPath = AndroidUtils.InExternalStorage( "x86-stdout.txt");


        ShareDialog devRequest = new ShareDialog(context);
        devRequest.Prepare(Const.DEVEL_EMAIL, "")
                .AddStringLine("Developer request mail");

        //AndroidUtils.GatherDeviceInfo(context, deviceInfoPath);
        //devRequest.AddFileAttachment(deviceInfoPath);

        //AndroidUtils.GatherAppLog(context, Environment.DIRECTORY_DOWNLOADS, appLogName);
        devRequest.AddFileAttachment(Environment.DIRECTORY_DOWNLOADS + "/" + appLogName);

        devRequest.AddFileAttachment(exagearLogPath)
                .AddFileAttachment(exagearErrPath);

        devRequest.Finalize().Send();
    }
}
