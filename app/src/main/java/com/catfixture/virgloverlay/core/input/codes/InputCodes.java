package com.catfixture.virgloverlay.core.input.codes;

import static android.view.KeyEvent.*;

import android.view.KeyEvent;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.codezjx.andlinker.annotation.In;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InputCodes {
    public static InputCode[] codes = new InputCode[] {
            new InputCode("UP", 0xC8),
            new InputCode("DOWN", 0xD0),
            new InputCode("LEFT", 0xCB),
            new InputCode("RIGHT", 0xCD),

            new InputCode("ENTER", 0x1C),
            new InputCode("ESC", 0x01),
            new InputCode("BACK", 0x0E),

            new InputCode("TAB", 0x0F),
            new InputCode("SPACE", 0x39),


            new InputCode("W", 0x11),
            new InputCode("S", 0x1F),
            new InputCode("A", 0x1E),
            new InputCode("D", 0x20),
    };

    public static String GetCodeName(int code) {
        for (InputCode inputCode : codes) {
            if (inputCode.code == code) return inputCode.name;
        }
        return null;
    }
    public static int GetCodeIndex(int code) {
        int i = 0;
        for (InputCode inputCode : codes) {
            if (inputCode.code == code)
                return i;
            i++;
        }
        return -1;
    }
}
