package com.catfixture.virgloverlay.core.input.windows.touchControls;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsOverlayFragment.ID_TOUCH_CONTROLS_OVERLAY;
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
import com.catfixture.virgloverlay.core.input.codes.InputCode;
import com.catfixture.virgloverlay.core.input.codes.InputCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.input.windows.utils.IDraggable;
import com.catfixture.virgloverlay.core.input.windows.utils.ITouchable;
import com.catfixture.virgloverlay.core.input.windows.utils.ITransformable;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;

public class TouchControlsEditorOverlayFragment implements IOverlayFragment, ITouchable, IDraggable, ITransformable {
    public Event onSetChanged = new Event();
    public Event onClosed = new Event();
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();

    private ViewGroup root;

    private View createControl;
    private View noItemErr;
    private View noProfilesErr;
    private View controlsView;
    private SeekBar alpha;
    private SeekBar size;
    private TextView alphaText;
    private TextView sizeText;
    private TextView uiOpacityText;
    private Spinner buttonCode;
    private View removeControl;
    private Button toggleSettings;
    private View controlsContainer;
    private View settingsContainer;
    private SeekBar uiOpacity;
    private Spinner type;

    private ArrayAdapter<String> profilesAdapter;
    private boolean settingsViewToggled;
    private int selectedItemId = -1;
    private Context context;
    private InputConfigData cfg;
    private TouchControlsOverlayFragment tcWindow;
    private Int2 position;

    @Override
    public int GetID() {
        return 0;
    }

    @Override
    public ViewGroup GetContainer() {
        return null;
    }

    @Override
    public void Create(Context context) {
        this.context = context;
        root = (ViewGroup) View.inflate(context, R.layout.touch_controls_editor, null);

        toggleSettings = root.findViewById(R.id.editorSettings);
        noItemErr = root.findViewById(R.id.noItemErr);
        noProfilesErr = root.findViewById(R.id.noProfilesErr);
        controlsView = root.findViewById(R.id.controlsView);
        createControl = root.findViewById(R.id.createControl);
        removeControl = root.findViewById(R.id.removeControl);
        alpha = root.findViewById(R.id.opacitySlider);
        alphaText = root.findViewById(R.id.opacitySliderText);
        size = root.findViewById(R.id.sizeSlider);
        sizeText = root.findViewById(R.id.sizeSliderText);
        buttonCode = root.findViewById(R.id.buttonCode);
        controlsContainer = root.findViewById(R.id.controlsContainer);
        settingsContainer = root.findViewById(R.id.settingsContainer);
        uiOpacity = root.findViewById(R.id.uiOpacity);
        uiOpacityText = root.findViewById(R.id.uiOpacityText);

        tcWindow = (TouchControlsOverlayFragment) app.GetOverlayManager().Get(ID_TOUCH_CONTROLS_OVERLAY);
        cfg = app.GetInputConfigData();

        //PROFILES
        Button addProfile = root.findViewById(R.id.addProfile);
        addProfile.setOnClickListener(view -> {
            InputConfigProfile icp = new InputConfigProfile();
            cfg.AddProfile(icp);
            cfg.SetCurrentProfile(cfg.profiles.size()-1);
            ResetSelection();
            UpdateAll();
            onSetChanged.notifyObservers();
        });
        Button removeProfile = root.findViewById(R.id.removeProfile);
        removeProfile.setOnClickListener(view -> {
            cfg.RemoveCurrentProfile();
            ResetSelection();
            UpdateAll();
            onSetChanged.notifyObservers();
        });

        profilesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        Spinner profilesSpinner = root.findViewById(R.id.inputProfiles);
        profilesSpinner.setAdapter(profilesAdapter);

        profilesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cfg.SetCurrentProfile(i);
                ResetSelection();
                InitEditorView();
                UpdateAll();
                onSetChanged.notifyObservers();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        profilesSpinner.setSelection(cfg.currentProfile);

        InflateProfiles();
        //PROFILES

        //EDITOR
        Button close = root.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            ResetSelection();
            onClosed.notifyObservers();
            app.GetOverlayManager().Hide(this);
        });

        toggleSettings.setOnClickListener(view -> {
            if ( !settingsViewToggled) {
                ResetSelection();
            }
            ToggleSettingsView();
        });
        //EDITOR

        //SETTINGS
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        typesAdapter.add("Circle button");
        typesAdapter.add("Rounded button");
        typesAdapter.add("Cross");
        typesAdapter.add("Stick");

        type = root.findViewById(R.id.controlType);
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

        ArrayAdapter<InputCode> buttonCodesAdapter = new ArrayAdapter<>(context, R.layout.touch_controls_list_item);
        buttonCodesAdapter.addAll(InputCodes.codes);
        buttonCode.setAdapter(buttonCodesAdapter);

        uiOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cfg.SetUiOpacity((i + 20) / 100.0f);
                uiOpacityText.setText("UI Opacity : " + (i + 20) + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {


                onSetChanged.notifyObservers();
            }
        });

        buttonCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int is, long l) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    if (is != data.buttonCode) {
                        data.SetButtonCode(InputCodes.codes[is].code);
                        UpdateAll();
                        onSetChanged.notifyObservers();
                    }
                });
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    float alpha = (i + 20) / 100.0f;
                    selectedItem.SetAlpha(alpha * cfg.uiOpacity);
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    data.SetAlpha(alpha);
                    alphaText.setText("Opacity : " + (int)(alpha * 100) + "%");
                });
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
                    int scale = i + 20;
                    selectedItem.SetScale(scale);
                    InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
                    data.SetScale(scale);
                    sizeText.setText("Size : " + (scale) + "%");
                });
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //SETTINGS

        //CONTROLS
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
                InitEditorView();
            }
        });
        //CONTROLS

        DragAndDropHandle<TouchControlsEditorOverlayFragment> dnd = new DragAndDropHandle<>(this);
        dnd.onPositionChanged.addObserver((observable, o) -> {
            cfg.SetTouchEditorPosition(GetPosition());
        });
        SetPosition(cfg.touchEditorPosition.x, cfg.touchEditorPosition.y);


        root.setClipChildren(false);
        root.setClipToPadding(false);
        selectedItemId = -1;
        InitEditorView();
    }

    @Override
    public void Destroy() {

    }

    private void UpdateAll() {
        InitEditorView();
        InflateProfiles();
    }

    public void ToggleSettingsView() {
        settingsViewToggled = !settingsViewToggled;
        controlsContainer.setVisibility(settingsViewToggled ? GONE : VISIBLE);
        settingsContainer.setVisibility(settingsViewToggled ? VISIBLE : GONE);
        if ( settingsViewToggled) toggleSettings.getBackground().setColorFilter(context.getColor(R.color.lightGray), PorterDuff.Mode.MULTIPLY);
        else toggleSettings.getBackground().setColorFilter(null);
        toggleSettings.setTextColor(context.getColor(settingsViewToggled ? R.color.white : R.color.black));

        int currUIOpacity = (int)(cfg.uiOpacity * 100 - 20);
        uiOpacity.setProgress(currUIOpacity);
        uiOpacityText.setText("UI Opacity : " + currUIOpacity + "%");
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

    public void SetSelected(int selectedItemId) {
        ResetSelection();
        this.selectedItemId = selectedItemId;
        InitEditorView();

        tcWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
            ((LinearLayout)selectedItem).getBackground().setColorFilter(context.getColor(R.color.orange), PorterDuff.Mode.MULTIPLY);

            InputTouchControlElement data = (InputTouchControlElement) selectedItem.GetData();
            alpha.setProgress((int) (data.alpha * 100)-20);
            size.setProgress(data.scale-20);
            type.setSelection(data.type);
            buttonCode.setSelection(data.buttonCode);
            alphaText.setText("Opacity : " + (int)(data.alpha * 100) + "%");
            sizeText.setText("Size : " + (data.scale) + "%");

            buttonCode.setVisibility(data.type == TYPE_ROUNDED_BUTTON || data.type == TYPE_CIRCLE_BUTTON ? VISIBLE : GONE);
        });

        if (settingsViewToggled)
            ToggleSettingsView();
    }

    @Override
    public Event OnDown() {
        return onDown;
    }

    @Override
    public Event OnMove() {
        return onMove;
    }

    @Override
    public Event OnUp() {
        return onUp;
    }

    @Override
    public Event OnClick() {
        return onClick;
    }

    @Override
    public Int2 GetPosition() {
        return position;
    }

    @Override
    public void SetPosition(int x, int y) {
        position.Set(x,y);
    }
}
