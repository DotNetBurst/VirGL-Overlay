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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.codes.InputCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
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
    private TouchableWindowElement handle;

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
                    if (!isEditorOverlayShown) {
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

                        final TouchableWindowElement ntl = newTouchElement;
                        newTouchElement.onDown.addObserver((observable, o) -> {
                            if ( handle != null) root.removeView(handle);
                            touchControlsEditor.SetSelected(ntl.GetId());

                            if ( touchControlElement.type == TYPE_STICK) {
                                handle = ((StickElement)ntl).CreateHandle(context);
                                ntl.SetHandle(handle);
                                root.addView(handle);
                            }
                        });
                    }

                    final Int2 elSize = newTouchElement.GetSize();
                    if (!isEditorOverlayShown) {
                        if (touchControlElement.type == TYPE_CROSS) {
                            newTouchElement.onDown.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 clickPos = new Int2((int) motionEvent.getX() - elSize.x / 2,
                                        (int) motionEvent.getY() - elSize.y / 2);


                                final Int2 verticalAxis = new Int2(1, 0);
                                final Int2 horizontalAxis = new Int2(0, 1);

                                float vericalCos = verticalAxis.Dot(clickPos);
                                float horizontalCos = horizontalAxis.Dot(clickPos);

                                boolean analogVert = Math.abs(vericalCos) > 0;
                                boolean analogHoriz = Math.abs(horizontalCos) > 0;

                                if ( analogVert) {
                                    Dbg.Msg("ANALOG VERT = " + vericalCos);
                                    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                                            vericalCos < -0.5 && vericalCos > 0.5 ? KeyEvent.KEYCODE_DPAD_UP : KeyEvent.KEYCODE_DPAD_DOWN);
                                    inputDevice.SendKeyEvent(keyEvent);
                                }

                                if ( analogHoriz) {
                                    Dbg.Msg("ANALOG HORIZ = " + horizontalCos);
                                    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                                            horizontalCos < -0.5 && horizontalCos > 0.5 ? KeyEvent.KEYCODE_DPAD_LEFT : KeyEvent.KEYCODE_DPAD_RIGHT);
                                    inputDevice.SendKeyEvent(keyEvent);
                                }
                            });
                        } else if (touchControlElement.type == TYPE_STICK) {
                            newTouchElement.onDown.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 clickPos = new Int2((int) motionEvent.getX() - elSize.x / 2,
                                        (int) motionEvent.getY() - elSize.y / 2);

                                final Int2 verticalAxis = new Int2(1, 0);
                                final Int2 horizontalAxis = new Int2(0, 1);

                            });
                        }
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

    public void DestroyMisc() {
        if (handle != null) root.removeView(handle);
    }
}
