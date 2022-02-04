package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.stick;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.IEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.CommonElementEditor;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;

public class StickElementEditor extends CommonElementEditor {
    private TableRow root;
    private TextView sensivityText;
    private SeekBar sensivity;

    public StickElementEditor(Context context, IInputWindowElement parentItem) {
        super(context, parentItem);

        InputTouchControlElementData data = (InputTouchControlElementData) parentItem.GetData();
        root = (TableRow) View.inflate(context, R.layout.editable_stick_element, null);

        sensivityText = root.findViewById(R.id.sensivitySliderText);
        sensivity = root.findViewById(R.id.sensivitySlider);

        sensivityText.setText("Sensivity : " + (data.sensivity) + "%");
        sensivity.setProgress(data.sensivity);

        sensivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                data.SetSensivity(i);
                sensivityText.setText("Sensivity : " + (i) + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ((TableLayout)super.root.findViewById(R.id.table)).addView(root);
    }
}