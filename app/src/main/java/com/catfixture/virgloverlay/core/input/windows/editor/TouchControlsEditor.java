package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.InputController;
import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.math.Int2;

public class TouchControlsEditor extends TouchableWindowElement {
    private final View noItemErr;
    private final View noProfilesErr;
    private final View controlsView;
    private final InputConfig cfg;
    private final ArrayAdapter<String> profilesAdapter;
    private ViewGroup editorContent;
    private IInputWindowElement selectedItem;

    public TouchControlsEditor(Context context, ViewGroup viewGroup) {
        super(context);

        InputController inputController = app.GetInputController();
        cfg = inputController.GetConfigData();

        //viewGroup.setOnClickListener(view -> {
        //    ResetSelection();
        //});

        editorContent = (ViewGroup) View.inflate(context, R.layout.touch_controls_editor, null);

        profilesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        Spinner items = editorContent.findViewById(R.id.inputProfiles);
        items.setAdapter(profilesAdapter);



        Button close = editorContent.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            viewGroup.removeView(this);
        });

        Button addProfile = editorContent.findViewById(R.id.addProfile);
        addProfile.setOnClickListener(view -> {
            InputConfigProfile icp = new InputConfigProfile();
            cfg.AddProfile(icp);
            UpdateAll();
        });
        Button removeProfile = editorContent.findViewById(R.id.removeProfile);
        removeProfile.setOnClickListener(view -> {
            cfg.RemoveCurrentProfile();
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

        SeekBar opacity = editorContent.findViewById(R.id.opacitySlider);
        SeekBar size = editorContent.findViewById(R.id.sizeSlider);*/


        editorContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editorContent.setClipChildren(false);
        editorContent.setClipToPadding(false);
        setClipChildren(false);
        setClipToPadding(false);

        noItemErr = editorContent.findViewById(R.id.noItemErr);
        noProfilesErr = editorContent.findViewById(R.id.noProfilesErr);
        controlsView = editorContent.findViewById(R.id.controlsView);


        InitEditorView();

        addView(editorContent);
        DragAndDropHandle dnd = new DragAndDropHandle(this);
        dnd.onPositionChanged.addObserver((observable, o) -> {
            cfg.SetTouchEditorPosition(GetPosition());
        });
        viewGroup.addView(this);

        SetPosition(cfg.touchEditorPosition);
        SetSize(new Int2(800, WRAP_CONTENT));
    }

    private void UpdateAll() {
        InflateProfiles();
        InitEditorView();
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
        noItemErr.setVisibility(controlsVisible ? GONE : VISIBLE);
        controlsView.setVisibility(controlsVisible ? VISIBLE : GONE);
    }

    private void ResetSelection() {
        selectedItem = null;
    }

    public void Destroy() {
    }
}
