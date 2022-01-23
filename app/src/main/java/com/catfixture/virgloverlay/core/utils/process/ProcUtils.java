package com.catfixture.virgloverlay.core.utils.process;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.util.Log;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcUtils {
    public static <T> int GetProcessIDByClassName(Class<T> clazz) {
        return Integer.parseInt(clazz.getName().substring(1));
    }
 
    public static void RunSystemCommand(String command, Action<Integer> resultCode) {
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
                int res = p.exitValue();
                if ( resultCode != null) {
                    resultCode.Invoke(res);
                }
            } catch (IOException | InterruptedException e) {
                Dbg.Error(e);
                if ( resultCode != null) {
                    resultCode.Invoke(-1);
                }
            }
        }).start();
    }
    public static void RunSystemCommandString(String command, Action<String> resultCode) {
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String res = br.readLine();
                Log.d(APP_TAG, "RESULT WAS = " + res);
                resultCode.Invoke(res);
                p.destroy();
            } catch (IOException e) {
                Dbg.Error(e);
            }
        }).start();
    }
}
