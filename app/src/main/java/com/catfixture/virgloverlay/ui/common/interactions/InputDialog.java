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
        LinearLayout lLay = new LinearLayout(context);
        lLay.setOrientation(LinearLayout.VERTICAL);

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

        lLay.addView(input);

        Button ok = new Button(context);
        ok.setText(okText);
        lLay.addView(ok);

        AndroidWindow androidWindow = new AndroidWindow(context);
        androidWindow.SetTranlucent()
                .SetOverlay()
                .EnableEvents()
                .SetContainer(lLay)
                .SetSizeByContainer()
                .SetVisibility(true);

        ok.setOnClickListener((s) -> {
            onOk.Invoke(input.getText().toString());
            androidWindow.Detach();
        });

        androidWindow.Attach();
    }
}
