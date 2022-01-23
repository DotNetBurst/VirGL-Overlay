package com.catfixture.virgloverlay.core.input.overlay;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.overlay.TouchControlsEditorOverlayFragment.ID_TOUCH_CONTROLS_EDITOR_OVERLAY;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_RECT_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.InputCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.RoundedButton;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.StickElement;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TextButton;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.overlay.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CrossButton;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;

import java.util.ArrayList;
import java.util.List;


public class TouchControlsOverlayFragment implements IOverlayFragment {
    public final static int ID_TOUCH_CONTROLS_OVERLAY = 10001;

    private List<IInputWindowElement> windowElements;
    private IInputDevice inputDevice;
    private ViewGroup root;
    private Context context;

    public TouchControlsOverlayFragment(IInputDevice inputDevice) {
        this.inputDevice = inputDevice;
    }


    public void TryGetWindowElementById(int selectedItemId, Action<IInputWindowElement> onFind) {
        for (IInputWindowElement windowElement : windowElements) {
            if ( windowElement.GetId() == selectedItemId) {
                onFind.Invoke(windowElement);
                return;
            }
        }
    }

    public void InflateControls() {
        root.removeAllViews();
        windowElements.clear();

        InputConfigData cfgData = app.GetInputConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();

            TouchControlsEditorOverlayFragment touchControlsEditor =
                    (TouchControlsEditorOverlayFragment) app.GetOverlayManager().Get(ID_TOUCH_CONTROLS_EDITOR_OVERLAY);

            boolean isEditorOverlayShown = app.GetOverlayManager().IsShown(touchControlsEditor);

            for (InputTouchControlElement touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                if ( touchControlElement.type == TYPE_ROUNDED_BUTTON ||
                        touchControlElement.type == TYPE_CIRCLE_BUTTON ||
                            touchControlElement.type == TYPE_RECT_BUTTON) {
                    int layout = touchControlElement.type == TYPE_ROUNDED_BUTTON ? R.drawable.fx_tc_rect_rnd_btn :
                            touchControlElement.type == TYPE_CIRCLE_BUTTON ? R.drawable.fx_tc_circle_btn :
                                    R.drawable.fx_tc_rect_rnd_btn;

                    newTouchElement = new TextButton(context, touchControlElement.id, layout);
                    ((TextButton)newTouchElement).SetText(InputCodes.GetCodeName(touchControlElement.buttonCode));
                    if (isEditorOverlayShown) {

                        newTouchElement.onDown.addObserver((observable, o) -> {
                            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, touchControlElement.buttonCode);
                            inputDevice.SendKeyEvent(keyEvent);
                        });
                    }
                } else if ( touchControlElement.type == TYPE_CROSS) {
                    newTouchElement = new CrossButton(context, touchControlElement.id);
                } else if ( touchControlElement.type == TYPE_STICK) {
                    newTouchElement = new StickElement(context, touchControlElement.id);
                }


                if ( newTouchElement != null) {
                    newTouchElement.SetCustomData(touchControlElement);
                    newTouchElement.SetScale(touchControlElement.scale)
                        .SetAlpha(touchControlElement.alpha * cfgData.uiOpacity);

                    newTouchElement.SetPosition(touchControlElement.position.x,touchControlElement.position.y);


                    if ( isEditorOverlayShown) {
                        DragAndDropHandle<TouchableWindowElement> dnd = new DragAndDropHandle<>(newTouchElement);
                        dnd.onPositionChanged.addObserver((obs, o) -> {
                            touchControlElement.SetPosition((Int2) o);
                        });
                        dnd.EnableSnap(25);

                        final int iwe = newTouchElement.GetId();
                        newTouchElement.onDown.addObserver((observable, o) -> {
                            touchControlsEditor.SetSelected(iwe);
                        });
                    }
                    root.addView(newTouchElement);
                    windowElements.add(newTouchElement);
                }
            }
        }

    }

    public void Destroy() {
    }

    @Override
    public ViewGroup GetContainer() {
        return root;
    }

    @Override
    public int GetID() {
        return ID_TOUCH_CONTROLS_OVERLAY;
    }

    @Override
    public void Create(Context context) {
        this.context = context;
        windowElements = new ArrayList<>();
        root = new RelativeLayout(context);
        InflateControls();
    }

    public void OnEditorClosed() {
        InflateControls();
    }
}
