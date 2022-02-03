package com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.spinnerData;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.IEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementSpinnerData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;
import com.catfixture.virgloverlay.ui.utils.Utils;

public class CommonElementEditor implements IEditable {
    protected final Context context;
    protected final ViewGroup root;
    protected final IInputWindowElement parentItem;

    private final TextView alphaText;
    private final TextView sizeText;

    public CommonElementEditor(Context context, IInputWindowElement parentItem) {
        this.context = context;
        this.parentItem = parentItem;
        InputTouchControlElementData data = (InputTouchControlElementData) parentItem.GetData();

        root = (ViewGroup) View.inflate(context, R.layout.editable_common_element, null);

        SeekBar alpha = root.findViewById(R.id.opacitySlider);
        alphaText = root.findViewById(R.id.opacitySliderText);
        SeekBar size = root.findViewById(R.id.sizeSlider);
        sizeText = root.findViewById(R.id.sizeSliderText);
        Spinner type = root.findViewById(R.id.controlType);
        View removeControl = root.findViewById(R.id.removeControl);


        alpha.setProgress((int) (data.alpha * 100)-20);
        size.setProgress(data.scale-20);
        alphaText.setText("Opacity : " + (int)(data.alpha * 100) + "%");
        sizeText.setText("Size : " + (data.scale) + "%");


        ArrayAdapter<TouchableWindowElementSpinnerData> typesAdapter = Utils.InitSpinner(context, type, 0);
        typesAdapter.addAll(spinnerData);
        typesAdapter.notifyDataSetChanged();
        type.setSelection(TouchableWindowElementType.SpinnerDataPos(data.type));
        Utils.AttachSpinnerAction(type, i -> {
            final int currTypeId = spinnerData[i].id;

            if (currTypeId != data.type) {
                data.SetType(currTypeId);
                parentItem.Reinflate();
            }
        });


        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float alpha = (i + 20) / 100.0f;
                parentItem.SetAlpha(alpha);
                data.SetAlpha(alpha);
                alphaText.setText("Opacity : " + (int)(alpha * 100) + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int scale = i + 20;
                parentItem.SetScale(scale);
                data.SetScale(scale);
                sizeText.setText("Size : " + (scale) + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        removeControl.setOnClickListener(view -> {
            app.GetInputConfigData().RemoveCurrentProfile();
            parentItem.Reinflate();
        });
    }

    @Override
    public View GetRoot() {
        return root;
    }
}
