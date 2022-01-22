package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsWindow;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.types.Event;

public class TouchControlsEditor extends TouchableWindowElement {
    private final View noItemErr;
    private final View noProfilesErr;
    private final View controlsView;
    private final InputConfig cfg;
    private final ArrayAdapter<String> profilesAdapter;
    private final View createControl;
    private final ViewGroup container;
    private final SeekBar alpha;
    private final SeekBar size;
    private final TextView alphaText;
    private final TextView sizeText;
    private final Spinner type;
    private final Spinner buttonCode;
    private final TouchControlsWindow tcWindow;
    private final View removeControl;
    private final Button toggleSettings;
    private final View controlsContainer;
    private final View settingsContainer;
    public Event onSetChanged = new Event();
    private ViewGroup editorContent;
    private int selectedItemId = -1;
    private boolean settingsViewToggled;

    public TouchControlsEditor(Context context, TouchControlsWindow tcWindow, InputConfig cfg) {
        super(context, -812043);
        this.cfg = cfg;
        this.tcWindow = tcWindow;


        container = tcWindow.GetIWindow().GetContainer();

        //viewGroup.setOnClickListener(view -> {
        //    ResetSelection();
        //});

        editorContent = (ViewGroup) View.inflate(context, R.layout.touch_controls_editor, null);

        profilesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        Spinner items = editorContent.findViewById(R.id.inputProfiles);
        items.setAdapter(profilesAdapter);


        items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cfg.SetCurrentProfile(i);
                UpdateAll();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        Button close = editorContent.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            ResetSelection();
            container.removeView(this);
        });

        Button addProfile = editorContent.findViewById(R.id.addProfile);
        addProfile.setOnClickListener(view -> {
            InputConfigProfile icp = new InputConfigProfile();
            cfg.AddProfile(icp);
            ResetSelection();
            UpdateAll();
        });
        Button removeProfile = editorContent.findViewById(R.id.removeProfile);
        removeProfile.setOnClickListener(view -> {
            cfg.RemoveCurrentProfile();
            ResetSelection();
            UpdateAll();
        });

        InflateProfiles();


        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        typesAdapter.add("Circle button");
        typesAdapter.add("Rounded button");
        typesAdapter.add("Cross");
        typesAdapter.add("Stick");

        type = editorContent.findViewById(R.id.controlType);
        type.setAdapter(typesAdapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    if (is != data.type) {
                        data.SetType(is);
                        UpdateAll();
                        onSetChanged.notifyObservers();
                    }
                });
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        editorContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editorContent.setClipChildren(false);
        editorContent.setClipToPadding(false);
        setClipChildren(false);
        setClipToPadding(false);

        toggleSettings = editorContent.findViewById(R.id.editorSettings);
        noItemErr = editorContent.findViewById(R.id.noItemErr);
        noProfilesErr = editorContent.findViewById(R.id.noProfilesErr);
        controlsView = editorContent.findViewById(R.id.controlsView);
        createControl = editorContent.findViewById(R.id.createControl);
        removeControl = editorContent.findViewById(R.id.removeControl);
        alpha = editorContent.findViewById(R.id.opacitySlider);
        alphaText = editorContent.findViewById(R.id.opacitySliderText);
        size = editorContent.findViewById(R.id.sizeSlider);
        sizeText = editorContent.findViewById(R.id.sizeSliderText);
        buttonCode = editorContent.findViewById(R.id.buttonCode);
        controlsContainer = editorContent.findViewById(R.id.controlsContainer);
        settingsContainer = editorContent.findViewById(R.id.settingsContainer);

        ArrayAdapter<String> buttonCodesAdapter = new ArrayAdapter<String>(context, R.layout.touch_controls_list_item);
        buttonCodesAdapter.add("A");
        buttonCode.setAdapter(buttonCodesAdapter);


        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    float alpha = i / 100.0f;
                    selectedItem.SetAlpha(alpha);
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    data.SetAlpha(alpha);
                    alphaText.setText("Opacity : " + i + "%");
                });
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    selectedItem.SetScale(i);
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    data.SetScale(i);
                    sizeText.setText("Size : " + i + "%");
                });
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        createControl.setOnClickListener(view -> {
            if ( cfg.HasCurrentProfile()) {
                InputConfigProfile cfgProfile = cfg.GetCurrentProfile();
                int newObjId = cfg.GetInternalId();
                cfgProfile.AddControlElement(newObjId);
                onSetChanged.notifyObservers();
                SetSelected(newObjId);
            }
        });
        removeControl.setOnClickListener(view -> {
            if ( cfg.HasCurrentProfile()) {
                InputConfigProfile cfgProfile = cfg.GetCurrentProfile();
                cfgProfile.RemoveControlElement(selectedItemId);
                onSetChanged.notifyObservers();
                ResetSelection();
            }
        });

        toggleSettings.setOnClickListener(view -> {
            if ( !settingsViewToggled) {
                ResetSelection();
            }
            ToggleSettingsView();
        });

        selectedItemId = -1;
        InitEditorView();

        addView(editorContent);
        DragAndDropHandle dnd = new DragAndDropHandle(this);
        dnd.onPositionChanged.addObserver((observable, o) -> {
            cfg.SetTouchEditorPosition(GetPosition());
        });
        container.addView(this);

        SetPosition(cfg.touchEditorPosition.x, cfg.touchEditorPosition.y);
        SetSize(800, WRAP_CONTENT);
    }

    private void UpdateAll() {
        InitEditorView();
        InflateProfiles();
    }

    public void ToggleSettingsView() {
        settingsViewToggled = !settingsViewToggled;
        controlsContainer.setVisibility(settingsViewToggled ? GONE : VISIBLE);
        settingsContainer.setVisibility(settingsViewToggled ? VISIBLE : GONE);
        if ( settingsViewToggled) toggleSettings.getBackground().setColorFilter(getContext().getColor(R.color.lightGray), PorterDuff.Mode.MULTIPLY);
        else toggleSettings.getBackground().setColorFilter(null);
        toggleSettings.setTextColor(getContext().getColor(settingsViewToggled ? R.color.white : R.color.black));
    }

    private void InflateProfiles() {
        profilesAdapter.clear();
        for (InputConfigProfile cfgConfigProfile : cfg.profiles) {
            profilesAdapter.add(cfgConfigProfile.GetName());
        }
        profilesAdapter.notifyDataSetChanged();
    }

    private void InitEditorView() {
        boolean hasProfile = cfg.HasCurrentProfile();
        noProfilesErr.setVisibility(hasProfile ? GONE : VISIBLE);

        boolean controlsVisible = hasProfile && (selectedItemId != -1);
        noItemErr.setVisibility(controlsVisible || !hasProfile ? GONE : VISIBLE);
        controlsView.setVisibility(controlsVisible ? VISIBLE : GONE);
        createControl.setVisibility(hasProfile && (selectedItemId == -1) ? VISIBLE : GONE);
    }

    private void ResetSelection() {
        tcWindow.TryGetWindowElementById( this.selectedItemId, (selectedItem) -> {
            if (selectedItem != null)
                ((LinearLayout) selectedItem).getBackground().setColorFilter(null);
        });
        selectedItemId = -1;
    }

    public void Destroy() {
        container.removeView(this);
    }

    public void SetSelected(int selectedItemId) {
        ResetSelection();
        this.selectedItemId = selectedItemId;
        InitEditorView();

        tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
            ((LinearLayout)selectedItem).getBackground().setColorFilter(getContext().getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

            InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
            alpha.setProgress((int) (data.alpha * 100));
            size.setProgress(data.scale);
            type.setSelection(data.type);

            buttonCode.setVisibility(data.type == TYPE_ROUNDED_BUTTON || data.type == TYPE_CIRCLE_BUTTON ? VISIBLE : GONE);
        });

        if (settingsViewToggled)
            ToggleSettingsView();


    }
}
