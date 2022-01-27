package com.catfixture.virgloverlay.core.input.codes;

import android.content.Context;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KeyCodes {
    public static List<KeyCode> codes = new ArrayList<>();

    public static void LoadKeyCodes (Context ctx) {
        InputStream is =  ctx.getResources().openRawResource(R.raw.key);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<KeyCode> code = new ArrayList<>();

        try {
            String tempLine = null;
            while ((tempLine = br.readLine()) != null) {
                if (!tempLine.equals("")) {
                    final String[] parts = tempLine.split("=");
                    codes.add(new KeyCode(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (Exception x) {
            Dbg.Error(x);
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetCodeName(int code) {
        for (KeyCode inputCode : codes) {
            if (inputCode.code == code) return inputCode.name;
        }
        return null;
    }
    public static int GetCodeIndex(int code) {
        int i = 0;
        for (KeyCode inputCode : codes) {
            if (inputCode.code == code)
                return i;
            i++;
        }
        return -1;
    }
}
