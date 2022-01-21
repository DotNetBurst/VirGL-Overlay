package com.catfixture.virgloverlay.core.utils.android;

import static com.catfixture.virgloverlay.core.utils.process.ProcUtils.RunSystemCommand;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.PERMISSION_REQUEST_CODE;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.catfixture.virgloverlay.BuildConfig;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ProcUtils;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ConstantConditions")
public class AndroidUtils {
    //DANGER (even harsh) approach that work only with root
    public static void ForceAppToImmersive(String appPackage, Action<Integer> onDone) {
        RunSystemCommand("su -c settings put global policy_control immersive.full=" + appPackage, onDone);
    }
    public static void ForceAppToNormal(String appPackage, Action<Integer> onDone) {
        RunSystemCommand("su -c settings put global policy_control immersive.off=" + appPackage, onDone);
    }

    public static String InExternalStorage(String s) {
        return Environment.getExternalStorageDirectory() + "/" + s;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static Size GetRealDisplaySize (WindowManager windowManager) {
        Point size = new Point();
        Display defDisp = windowManager.getDefaultDisplay();
        defDisp.getRealSize(size);

        if (size.x < size.y) {
            int sizeX = size.x;
            size.x = size.y;
            size.y = sizeX;
        }


        return new Size(size.x, size.y);
    }


    public static int GetNavbarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void OnPermissionGranted (Activity context, String permission, Runnable onGranted, Runnable onDenied) {
        if ( context instanceof IPermissionGrantable) {
            IPermissionGrantable permissionGrantable = (IPermissionGrantable) context;

            if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context,permission)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    context.startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(context, new String[] { permission }, PERMISSION_REQUEST_CODE);
                }
                permissionGrantable.onGranted.deleteObservers();
                permissionGrantable.onGranted.addObserver((observable, o) -> {
                    //int grantResult = (Integer)o;
                    if (ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED) {
                        onGranted.run();
                    } else {
                        Log.e(APP_TAG, "Error permission not granted!");
                        if ( onDenied != null) {
                            onDenied.run();
                        } else {
                            ConfirmDialog.Show(context, "Permission not granted!",
                        "Requested permission ("+permission+") not granted, " +
                                "this meants that part of application will not work!", "Try again", () ->
                                    OnPermissionGranted(context, permission, onGranted, onDenied), "Close", null);
                        }
                    }
                });
            } else {
                onGranted.run();
            }
        } else {
            Log.e(APP_TAG, "Assertion error : context must be IPermissionGrantable!");
            System.exit(-1);
        }
    }


    public static void RequestInstallationPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!context.getPackageManager().canRequestPackageInstalls()){
                context.startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                        .setData(Uri.parse(String.format("package:%s", context.getPackageName()))), 1);
            }
        }
    }


    static Uri UriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
    public static void InstallUpdate(AppCompatActivity context, String path, Runnable onInstalled, Runnable onError) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = UriFromFile(context, new File(path));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if ( context instanceof IActivityLaunchable) {
                ((IActivityLaunchable) context).Launch(intent, (result) -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        onInstalled.run();
                    } else {
                        onError.run();
                    }
                });
            } else {
                Log.e(APP_TAG, "FATAL ACTIVITY DOESN'T IMPLEMENT IACTIVITYLAUNCHANBLE!!!");
                System.exit(-1);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Dbg.Error("Error in opening the file!");
            Dbg.Error(e);
        }
    }

    /*
    public static void GatherAppLog(Context context, String dir, String fileName) {
        try {
            String[] command = { "logcat", "-t", "1000", "-e", "virgl"};
            Process p = Runtime.getRuntime().exec(command);
            String logData = ReadProcessOutput(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void GatherDeviceInfo(Context context, String dir, String fileName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        StringBuilder deviceInfo = new StringBuilder();
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);

        deviceInfo.append("OS: ").append(System.getProperty("os.version")).append('\n');
        deviceInfo.append("DEV: ").append(android.os.Build.DEVICE + "("+android.os.Build.MODEL+")").append('\n');
        deviceInfo.append("RAM: ").append(memInfo.totalMem / 1024 / 1024).append("(MB)").append('\n');
        deviceInfo.append("OGL: ").append(configurationInfo.reqGlEsVersion);

    }*/

    public static void SetSELinuxPermissive(boolean is, Action<Integer> onExecuted) {
        RunSystemCommand("su -c setenforce " + (is ? 0 : 1), onExecuted);
    }
    public static void CheckSELinuxPermissive(Action<Boolean> onChecked) {
        ProcUtils.RunSystemCommandString("su -c getenforce", (r) ->
                onChecked.Invoke(r != null && r.contains("Permissive")));
    }

    public static void WriteFile (String path, String data) {
        WriteFile(path, data.getBytes(StandardCharsets.UTF_8));
    }
    public static void WriteFile (String path, byte[] data) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Dbg.Error("Error couldn't create file!");
                }
            }

            FileOutputStream fw = new FileOutputStream(file);
            fw.write(data);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Dbg.Error(e);
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public static String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "virgloverlay_notifid";
        String channelName = "VirglOverlay";

        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }


    public static class BroadcastBlank {
        private Intent intent;

        public BroadcastBlank(String action) {
            intent = new Intent();
            intent.setAction(action);
        }

        public BroadcastBlank putIntExtra(String name, int extra) {
            intent.putExtra(name,extra);
            return this;
        }
        public BroadcastBlank putStringExtra(String name, String extra) {
            intent.putExtra(name,extra);
            return this;
        }

        public void Send(Context ctx) {
            ctx.sendBroadcast(intent);
            intent = null;
        }
    }

    public static BroadcastBlank BroadcastBuilder(String action) {
        return new BroadcastBlank(action);
    }
}
