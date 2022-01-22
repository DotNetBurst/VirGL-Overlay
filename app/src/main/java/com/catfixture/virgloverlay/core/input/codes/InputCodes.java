package com.catfixture.virgloverlay.core.input.codes;

import static android.view.KeyEvent.*;

import android.view.KeyEvent;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.codezjx.andlinker.annotation.In;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InputCodes {
    public static InputCode[] codes;

    static {
        List<InputCode> temp = new ArrayList<InputCode>();

        Field[] fields = KeyEvent.class.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if ( name.contains("KEYCODE_")) {
                name = name.replace("KEYCODE_", "");
                try {
                    temp.add(new InputCode(name, field.getInt(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        codes = temp.toArray(new InputCode[0]);
    }

    public static String GetCodeName(int code) {
        for (InputCode inputCode : codes) {
            if (inputCode.code == code) return inputCode.name;
        }
        return null;
    }
}
