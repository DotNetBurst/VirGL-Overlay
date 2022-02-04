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

public class MouseCodes {
    public static MouseCode[] codes = new MouseCode[] {
        new MouseCode("Left button", "LB", 0),
        new MouseCode("Right button", "RB", 1),
        new MouseCode("Middle button", "MB", 2)
    };

    public static String GetCodeName(int code) {
        for (MouseCode inputCode : codes) {
            if (inputCode.code == code) return inputCode.name;
        }
        return null;
    }
    public static int GetCodeIndex(int code) {
        int i = 0;
        for (MouseCode inputCode : codes) {
            if (inputCode.code == code)
                return i;
            i++;
        }
        return -1;
    }

    public static String GetCodeSmallName(int code) {
        for (MouseCode inputCode : codes) {
            if (inputCode.code == code) return inputCode.smallName;
        }
        return null;
    }
}
