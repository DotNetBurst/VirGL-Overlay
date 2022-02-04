package com.catfixture.virgloverlay.core.input.devices.touch.interaction;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_BUTTON;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_MOUSE_ZONE;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElementData;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.ButtonElementEditable;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross.CrossElementEditable;
import com.catfixture.virgloverlay.core.input.utils.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.TouchDeviceEditorOverlayFragment;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.cross.CrossElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.mouseZone.MouseZoneElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.stick.StickElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.button.ButtonElement;
import com.catfixture.virgloverlay.core.input.devices.touch.interaction.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;

import java.util.ArrayList;
import java.util.List;


public class TouchDeviceOverlayFragment implements IOverlayFragment {
    public final static int ID_TOUCH_CONTROLS_OVERLAY = 10001;
    private TouchDeviceEditorOverlayFragment touchControlsEditor;

    private final List<IInputWindowElement> windowElements;
    private final IInputDevice inputDevice;
    private final ViewGroup root;
    private final Context context;

    public TouchDeviceOverlayFragment(Context context, IInputDevice inputDevice) {
        this.context = context;
        this.inputDevice = inputDevice;
        windowElements = new ArrayList<>();
        root = new RelativeLayout(context);


        InflateControls();
    }

    public void TryGetWindowElementById(int selectedItemId, Action<IInputWindowElement> onFind) {
        for (IInputWindowElement windowElement : windowElements) {
            if ( windowElement.GetId() == selectedItemId) {
                onFind.Invoke(windowElement);
                return;
            }
        }
    }
    public IInputWindowElement TryGetWindowElementById(int selectedItemId) {
        for (IInputWindowElement windowElement : windowElements) {
            if ( windowElement.GetId() == selectedItemId) {
                return windowElement;
            }
        }
        return null;
    }

    public void InflateControls() {
        root.removeAllViews();
        windowElements.clear();

        InputConfigData cfgData = app.GetInputConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();

            boolean isEditorOverlayShown = app.GetOverlayManager().IsShown(touchControlsEditor);

            for (InputTouchControlElementData touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                if ( touchControlElement.type == TYPE_BUTTON) {
                    newTouchElement = new ButtonElement(context, touchControlElement);
                } else if ( touchControlElement.type == TYPE_CROSS) {
                    newTouchElement = new CrossElement(context, touchControlElement);
                } else if ( touchControlElement.type == TYPE_STICK) {
                    newTouchElement = new StickElement(context, touchControlElement);
                } else if ( touchControlElement.type == TYPE_MOUSE_ZONE) {
                    newTouchElement = new MouseZoneElement(context, touchControlElement);
                } else continue;

                final TouchableWindowElement ntl = newTouchElement;

                newTouchElement.SetReinflate(() -> {
                    InflateControls();
                    touchControlsEditor.SetSelected(ntl.GetId());
                });
                newTouchElement.SetEditorReset(() -> {
                    InflateControls();
                    touchControlsEditor.SetSelected(-1);
                });

                newTouchElement.SetScale(touchControlElement.scale)
                    .SetAlpha(touchControlElement.alpha);
                newTouchElement.SetPosition(touchControlElement.position.x,touchControlElement.position.y);

                if ( isEditorOverlayShown) {
                    DragAndDropHandle<TouchableWindowElement> dnd = new DragAndDropHandle<>(newTouchElement);
                    dnd.onPositionChanged.addObserver((obs, o) -> {
                        touchControlElement.SetPosition((Int2) o);
                    });
                    dnd.EnableSnap(25);

                    newTouchElement.CreateEditorEvents();
                    newTouchElement.onDown.addObserver((observable, o) -> {
                        touchControlsEditor.SetSelected(ntl.GetId());
                    });
                } else {
                    newTouchElement.CreateActionEvents(inputDevice);
                }
                root.addView(newTouchElement);
                windowElements.add(newTouchElement);
            }
        }
    }

    @Override
    public ViewGroup GetContainer() {
        return root;
    }

    @Override
    public void OnFragmentShown() {
        OverlayManager overlayManager = app.GetOverlayManager();
        touchControlsEditor = new TouchDeviceEditorOverlayFragment(context, this);
        overlayManager.Add(touchControlsEditor);
        overlayManager.onClick.addObserver((observable, o) -> touchControlsEditor.SetSelected(-1));
        touchControlsEditor.onSetChanged.addObserver((observable, o) -> InflateControls());
        touchControlsEditor.onClosed.addObserver((observable, o) -> OnEditorClosed());
        overlayManager.Hide(touchControlsEditor);
    }

    @Override
    public void OnFragmentHidden() {

    }

    @Override
    public int GetID() {
        return ID_TOUCH_CONTROLS_OVERLAY;
    }


    public void OnEditorClosed() {
        InflateControls();
    }

    public void DeselectAll() {
        for (IInputWindowElement windowElement : windowElements) {
            windowElement.Deselect();
        }
    }
}
