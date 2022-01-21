package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
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
import com.catfixture.virgloverlay.core.input.InputController;
import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

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
    public Event onSetChanged = new Event();
    private ViewGroup editorContent;
    private IInputWindowElement selectedItem;

    public TouchControlsEditor(Context context, IWindow window) {
        super(context);

        container = window.GetContainer();

        InputController inputController = app.GetInputController();
        cfg = inputController.GetConfigData();

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

/*
        List<String> typesList = new ArrayList<>();
        typesList.add("Circle btn");
        typesList.add("Rect btn");
        typesList.add("Cross");
        typesList.add("Stick");
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(context, typesList.size());

        Spinner type = editorContent.findViewById(R.id.typeSpinner);
        type.setAdapter(typesAdapter);
*/


        editorContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editorContent.setClipChildren(false);
        editorContent.setClipToPadding(false);
        setClipChildren(false);
        setClipToPadding(false);

        noItemErr = editorContent.findViewById(R.id.noItemErr);
        noProfilesErr = editorContent.findViewById(R.id.noProfilesErr);
        controlsView = editorContent.findViewById(R.id.controlsView);
        createControl = editorContent.findViewById(R.id.createControl);
        alpha = editorContent.findViewById(R.id.opacitySlider);
        alphaText = editorContent.findViewById(R.id.opacitySliderText);
        size = editorContent.findViewById(R.id.sizeSlider);
        sizeText = editorContent.findViewById(R.id.sizeSliderText);



        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float alpha = i / 100.0f;
                selectedItem.SetAlpha(alpha);
                InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                data.SetAlpha(alpha);
                alphaText.setText("Opacity : " + i + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                selectedItem.SetScale(i);
                InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                data.SetScale(i);
                sizeText.setText("Size : " + i + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        createControl.setOnClickListener(view -> {
            if ( cfg.HasCurrentProfile()) {
                InputConfigProfile cfgProfile = cfg.GetCurrentProfile();
                cfgProfile.AddControlElement();
                UpdateAll();
                onSetChanged.notifyObservers();
            }
        });

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

        boolean controlsVisible = hasProfile && (selectedItem != null);
        noItemErr.setVisibility(controlsVisible || !hasProfile ? GONE : VISIBLE);
        controlsView.setVisibility(controlsVisible ? VISIBLE : GONE);
        createControl.setVisibility(hasProfile && (selectedItem == null) ? VISIBLE : GONE);
    }

    private void ResetSelection() {
        selectedItem = null;
    }

    public void Destroy() {
        container.removeView(this);
    }

    public void SetSelected(IInputWindowElement newTouchElement) {
        if ( selectedItem != null) selectedItem.Deselect();
        selectedItem = newTouchElement;
        newTouchElement.Select();
        InitEditorView();
        InputTouchControlElement data = (InputTouchControlElement) newTouchElement.GetData();
        alpha.setProgress((int) (data.alpha * 100));
        size.setProgress(data.scale);
    }
}
