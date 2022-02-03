package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.KeyCode;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.CommonElementEditor;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;

public class ButtonElementEditable extends CommonElementEditor {
    private Spinner buttonCode;
    private TableRow root;
    private ArrayAdapter<KeyCode> buttonCodesAdapter;

    public ButtonElementEditable(Context context, IInputWindowElement parentItem) {
        super(context, parentItem);

        InputTouchControlElementData data = (InputTouchControlElementData) parentItem.GetData();
        root = (TableRow) View.inflate(context, R.layout.editable_button_element, null);

        buttonCode = root.findViewById(R.id.buttonCode);
        buttonCodesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        buttonCodesAdapter.addAll(KeyCodes.codes);
        buttonCode.setAdapter(buttonCodesAdapter);
        buttonCode.setSelection(KeyCodes.GetCodeIndex(data.keyCode));

        buttonCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                final int newKeyCode = buttonCodesAdapter.getItem(is).code;
                if ( data.keyCode != newKeyCode) {
                    data.SetKeyCode(newKeyCode);
                    parentItem.Reinflate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ((TableLayout)super.root.findViewById(R.id.table)).addView(root);
    }
}