package com.catfixture.virgloverlay.ui.common.interactions;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import static android.widget.LinearLayout.LayoutParams;

public class InputDialog {
    public static void Show(Context context, String title, String iniText, String ok, Action<String> onOk, String no, Runnable onNo) {
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

        builder.setPositiveButton(ok, (dialog, which) -> {
            onOk.Invoke(input.getText().toString());
            dialog.cancel();
        });
        builder.setNegativeButton(no, (dialog, which) -> {
            if (onNo != null) onNo.run();
            dialog.cancel();
        });

        builder.show();
    }
}
