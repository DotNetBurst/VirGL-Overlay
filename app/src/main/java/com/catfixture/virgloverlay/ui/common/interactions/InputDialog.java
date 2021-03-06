package com.catfixture.virgloverlay.ui.common.interactions;

import android.content.Context;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;

import static android.widget.LinearLayout.LayoutParams;

public class InputDialog {
    public static void Show(Context context, String title, String iniText, String okText, Action<String> onOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);


        final EditText input = new EditText(context);
        input.setText(iniText);
        input.setHint("Name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setBackgroundResource(R.color.darkGray);
        input.setTextColor(context.getColor(R.color.white));

        LayoutParams lpars = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                100
        );
        lpars.setMargins(60,10,60,0);
        input.setLayoutParams(lpars);
        input.setPadding(10,0,10,0);

        LinearLayout lLay = new LinearLayout(context);
        lLay.addView(input);

        builder.setView(lLay);

        builder.setPositiveButton(okText, (dialog, which) -> {
            onOk.Invoke(input.getText().toString());
            dialog.cancel();
        });

        builder.show();
    }
}
