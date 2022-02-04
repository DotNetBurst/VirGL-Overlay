package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross;

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

public class CrossElementEditable extends CommonElementEditor {
    private Spinner mappingType;
    private TableRow root;
    private ArrayAdapter<String> mappingTypesAdapter;

    public CrossElementEditable(Context context, IInputWindowElement parentItem) {
        super(context, parentItem);

        InputTouchControlElementData data = (InputTouchControlElementData) parentItem.GetData();
        root = (TableRow) View.inflate(context, R.layout.editable_cross_element, null);

        mappingType = root.findViewById(R.id.mappingType);

        mappingTypesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        mappingTypesAdapter.addAll("WASD","Arrows","Numpad");
        mappingType.setAdapter(mappingTypesAdapter);
        mappingType.setSelection(data.mappingType);

        mappingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                data.SetMappingType(is);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ((TableLayout)super.root.findViewById(R.id.table)).addView(root);
    }
}