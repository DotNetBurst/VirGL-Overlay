package com.catfixture.virgloverlay.core.email;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareDialog {
    private final String authority;
    private Intent intent;
    private final StringBuilder extraText = new StringBuilder();
    private final ArrayList<Uri> extraFiles = new ArrayList<>();
    private final Context context;

    public ShareDialog(Context context) {
        this.context = context;
        this.authority = context.getApplicationContext().getPackageName() + ".provider";
    }


    public ShareDialog Prepare(String[] addresses, String subject) {
        intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        extraText.delete(0, extraText.length());
        extraFiles.clear();

        return this;
    }
    public ShareDialog AddFileAttachment(String filePath) {
        File file = new File(filePath);
        if ( file.exists() && file.length() > 0) {
            extraFiles.add(FileProvider.getUriForFile(context, authority, file));
        } else Log.e(APP_TAG, "File not exists or empty and wouldn't be added");
        return this;
    }
    public void AddStringLine(String line) {
        extraText.append(line).append("\n");
    }

    public ShareDialog Finalize() {
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, extraFiles);
        intent.putExtra(Intent.EXTRA_TEXT, extraText.toString());
        return this;
    }

    public void Send() {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(intent, "Share");

            List<ResolveInfo> resInfoList = context.getPackageManager()
                    .queryIntentActivities(chooserIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                for (Uri extraFile : extraFiles) {
                    context.grantUriPermission(packageName, extraFile,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
            }
            chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//dummy not working as well

            //THIS BITCH WILL NOT WORK WITHOUT ALL GRANTS AND EXTERNAL RW PERMS.
            context.startActivity(chooserIntent);
        }
    }
}
