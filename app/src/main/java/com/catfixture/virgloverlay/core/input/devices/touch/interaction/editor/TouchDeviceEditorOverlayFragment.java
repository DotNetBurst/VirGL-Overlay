package com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_BUTTON;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_MOUSE_ZONE;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_STICK;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.spinnerData;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.KeyCode;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.TouchDeviceOverlayFragment;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.ButtonElementEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross.CrossElementEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementSpinnerData;
import com.catfixture.virgloverlay.core.input.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.input.utils.EventUtils;
import com.catfixture.virgloverlay.core.input.utils.IDraggable;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.utils.ITouchable;
import com.catfixture.virgloverlay.core.input.utils.ITransformable;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.objProvider.ITypedProvider;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.common.interactions.InputDialog;
import com.catfixture.virgloverlay.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TouchDeviceEditorOverlayFragment implements IOverlayFragment, ITouchable, IDraggable, ITransformable {
    public final static int ID_TOUCH_CONTROLS_EDITOR_OVERLAY = 10002;
    private final ViewGroup customContainer;

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
    private TextView uiOpacityText;
    private Button toggleSettings;
    private View controlsContainer;
    private View settingsContainer;
    private SeekBar uiOpacity;

    private ArrayAdapter<String> profilesAdapter;
    private boolean settingsViewToggled;
    private int selectedItemId = -1;
    private Context context;
    private InputConfigData cfg;
    private TouchDeviceOverlayFragment parentWindow;
    private Int2 position = new Int2(0,0);
    private View editProfileName;

    @Override
    public int GetID() {
        return ID_TOUCH_CONTROLS_EDITOR_OVERLAY;
    }

    @Override
    public ViewGroup GetContainer() {
        return root;
    }

    @Override
    public void OnFragmentShown() {
        selectedItemId = -1;
        InitEditorView();
        onSetChanged.notifyObservers();
    }

    @Override
    public void OnFragmentHidden() {

    }

    public TouchDeviceEditorOverlayFragment(Context context, TouchDeviceOverlayFragment parentWindow) {
        this.context = context;
        root = (ViewGroup) View.inflate(context, R.layout.touch_controls_editor, null);

        toggleSettings = root.findViewById(R.id.editorSettings);
        noItemErr = root.findViewById(R.id.noItemErr);
        noProfilesErr = root.findViewById(R.id.noProfilesErr);
        createControl = root.findViewById(R.id.createControl);

        controlsContainer = root.findViewById(R.id.controlsContainer);
        customContainer = root.findViewById(R.id.customContainer);
        settingsContainer = root.findViewById(R.id.settingsContainer);
        uiOpacity = root.findViewById(R.id.uiOpacity);
        uiOpacityText = root.findViewById(R.id.uiOpacityText);

        this.parentWindow = parentWindow;
        cfg = app.GetInputConfigData();


        //EVENTS
        EventUtils.InitializeITouchableEvents(root, this);
        //EVENTS

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

        Spinner profilesSpinner = root.findViewById(R.id.inputProfiles);
        profilesAdapter = Utils.InitSpinner(context, profilesSpinner, cfg.currentProfile, R.layout.touch_controls_list_item);
        Utils.AttachSpinnerAction(profilesSpinner, i -> {
            cfg.SetCurrentProfile(i);
            ResetSelection();
            InitEditorView();
            UpdateAll();
            onSetChanged.notifyObservers();
        });

        editProfileName = root.findViewById(R.id.editProfileName);
        editProfileName.setOnClickListener(view1 -> {
            /*InputConfigProfile cfgProf = app.GetInputConfigData().GetCurrentProfile();
            InputDialog.Show(context, "Edit name", cfgProf.name, "Save", (newName) -> {
                app.GetInputConfigData().GetCurrentProfile().SetName(newName);
                InitEditorView();
                UpdateAll();
                onSetChanged.notifyObservers();
            }, "Cancel", null);*/
        });

        InflateProfiles();
        //PROFILES

        //EDITOR
        Button close = root.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            ResetSelection();
            app.GetOverlayManager().Hide(this);
            onClosed.notifyObservers();
        });

        toggleSettings.setOnClickListener(view -> {
            ToggleSettingsView();
            ResetSelection();
            InitEditorView();
        });

        //EDITOR

        //SETTINGS
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
        //SETTINGS

        //CONTROLS
        createControl.setOnClickListener(view -> {
            if ( cfg.HasCurrentProfile()) {
                final InputConfigProfile cfgProfile = cfg.GetCurrentProfile();
                final int newId = cfg.GetInternalId();
                cfgProfile.AddControlElement(newId);
                onSetChanged.notifyObservers();
                SetSelected(newId);
            }
        });
        //CONTROLS

        DragAndDropHandle<TouchDeviceEditorOverlayFragment> dnd = new DragAndDropHandle<>(this);
        dnd.onPositionChanged.addObserver((observable, o) -> {
            cfg.SetTouchEditorPosition(GetPosition());
        });

        root.setClipChildren(false);
        root.setClipToPadding(false);
        selectedItemId = -1;
        InitEditorView();

        LayoutUtils.SetSizeRelative(root, 800, WRAP_CONTENT);
        SetPosition(cfg.touchEditorPosition.x, cfg.touchEditorPosition.y);
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
        uiOpacityText.setText("UI Opacity : " + (currUIOpacity + 20) + "%");
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
        customContainer.setVisibility(controlsVisible ? VISIBLE : GONE);
        createControl.setVisibility(hasProfile && (selectedItemId == -1) ? VISIBLE : GONE);
    }

    private void ResetSelection() {
        parentWindow.TryGetWindowElementById( this.selectedItemId, (selectedItem) -> {
            if (selectedItem != null)
                ((LinearLayout) selectedItem).getBackground().setColorFilter(null);
        });
        selectedItemId = -1;
        customContainer.removeAllViews();
    }

    public void SetSelected(int selectedItemId) {
        ResetSelection();
        this.selectedItemId = selectedItemId;
        InitEditorView();

        parentWindow.TryGetWindowElementById(selectedItemId, (selectedItem) -> {
            selectedItem.Select(customContainer);
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
        position = new Int2(x,y);
        if ( root != null) {
            LayoutUtils.SetRelativeLayoutPos(root, x,y);
        }
    }

    @Override
    public Int2 GetSize() {
        return new Int2(0,0); //TODO NOT NEEDED NOW
    }
}
