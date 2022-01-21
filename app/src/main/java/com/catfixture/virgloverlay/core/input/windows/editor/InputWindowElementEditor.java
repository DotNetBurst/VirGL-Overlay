package com.catfixture.virgloverlay.core.input.windows.editor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;


import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;

import java.util.ArrayList;
import java.util.List;

public class InputWindowElementEditor extends TouchableWindowElement {
    private ViewGroup editorContent;

    public InputWindowElementEditor(Context context, ViewGroup viewGroup) {
        super(context);

        editorContent = (ViewGroup) View.inflate(context, R.layout.input_window_element_editor, null);

        List<String> typesList = new ArrayList<>();
        typesList.add("Circle btn");
        typesList.add("Rect btn");
        typesList.add("Cross");
        typesList.add("Stick");
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(context, typesList.size());

        Spinner type = editorContent.findViewById(R.id.typeSpinner);
        type.setAdapter(typesAdapter);

        SeekBar opacity = editorContent.findViewById(R.id.opacitySlider);
        SeekBar size = editorContent.findViewById(R.id.sizeSlider);

        Button close = editorContent.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            viewGroup.removeView(this);
        });

        editorContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(editorContent);
        new DragAndDropHandle(this);
        viewGroup.addView(this);
    }
}
