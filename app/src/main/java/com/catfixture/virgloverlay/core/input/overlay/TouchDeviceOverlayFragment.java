package com.catfixture.virgloverlay.core.input.overlay;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_MOUSE_ZONE;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_RECT_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;
import static com.catfixture.virgloverlay.core.input.overlay.touchControls.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.codes.KeyCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.MouseZoneElement;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.StickElement;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TextButton;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.overlay.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.input.overlay.touchControls.elements.CrossButton;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;

import java.util.ArrayList;
import java.util.List;


public class TouchDeviceOverlayFragment implements IOverlayFragment {
    public final static int ID_TOUCH_CONTROLS_OVERLAY = 10001;
    private TouchDeviceEditorOverlayFragment touchControlsEditor;

    private List<IInputWindowElement> windowElements;
    private IInputDevice inputDevice;
    private ViewGroup root;
    private Context context;

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

    public void InflateControls() {
        root.removeAllViews();
        windowElements.clear();

        InputConfigData cfgData = app.GetInputConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();

            boolean isEditorOverlayShown = app.GetOverlayManager().IsShown(touchControlsEditor);

            for (InputTouchControlElement touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                if ( touchControlElement.type == TYPE_ROUNDED_BUTTON || touchControlElement.type == TYPE_CIRCLE_BUTTON ||
                            touchControlElement.type == TYPE_RECT_BUTTON) {
                    int layout = touchControlElement.type == TYPE_ROUNDED_BUTTON ? R.drawable.fx_tc_rect_rnd_btn :
                            touchControlElement.type == TYPE_CIRCLE_BUTTON ? R.drawable.fx_tc_circle_btn : R.drawable.fx_tc_rect_rnd_btn;

                    newTouchElement = new TextButton(context, touchControlElement.id, layout);
                    ((TextButton)newTouchElement).SetText(KeyCodes.GetCodeName(touchControlElement.buttonCode));
                    if (!isEditorOverlayShown) {
                        newTouchElement.onDown.addObserver((observable, o) -> {
                            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, touchControlElement.buttonCode);
                            inputDevice.SendKeyDown(keyEvent.getKeyCode());
                        });
                        newTouchElement.onUp.addObserver((observable, o) -> {
                            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP, touchControlElement.buttonCode);
                            inputDevice.SendKeyUp(keyEvent.getKeyCode());
                        });
                    }

                    if (touchControlElement.type == TYPE_RECT_BUTTON) newTouchElement.SetInitialSize(200, 100);
                } else if ( touchControlElement.type == TYPE_CROSS) {
                    newTouchElement = new CrossButton(context, touchControlElement.id);
                } else if ( touchControlElement.type == TYPE_STICK) {
                    newTouchElement = new StickElement(context, touchControlElement.id);
                } else if ( touchControlElement.type == TYPE_MOUSE_ZONE) {
                    newTouchElement = new MouseZoneElement(context, touchControlElement.id);
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
                            touchControlsEditor.SetSelected(ntl.GetId());
                        });
                    }

                    final Int2 elSize = newTouchElement.GetSize();
                    if (!isEditorOverlayShown) {
                        if (touchControlElement.type == TYPE_CROSS) {

                            final Int2 startAxis = new Int2(0,0);
                            final Int2 currentAxis = new Int2(-1,-1);

                            final float dzMult = 0.5f;
                            final float elScale = (touchControlElement.scale / 100.0f);
                            final float deadZoneX = elScale * elSize.x * dzMult * 0.5f;
                            final float deadZoneY = elScale * elSize.y * dzMult * 0.5f;

                            newTouchElement.onDown.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 dt = new Int2((int) motionEvent.getX() - elSize.x / 2,
                                        (int) motionEvent.getY() - elSize.y / 2);

                                Dbg.Msg("Click XXXX " + dt.x + " _ " + dt.y);

                                boolean isInYAxisZone = (dt.x > -deadZoneY && dt.x < deadZoneY);
                                boolean isInUpperZone = (dt.y > 0 && isInYAxisZone || dt.y > deadZoneY);
                                boolean isInLowerZone = (dt.y < 0 && isInYAxisZone || dt.y < -deadZoneY);

                                boolean isInXAxisZone = (dt.y > -deadZoneX && dt.y < deadZoneX);
                                boolean isInRightZone = (dt.x > 0 && isInXAxisZone || dt.x > deadZoneX);
                                boolean isInLeftZone = (dt.x < 0 && isInXAxisZone || dt.x < -deadZoneX);

                                startAxis.Set(isInUpperZone ? 83 : isInLowerZone ? 87 : -1,
                                        isInLeftZone ? 65 : isInRightZone ? 68 : -1);

                                if ( startAxis.x != -1) {
                                    inputDevice.SendKeyDown(startAxis.x);
                                    currentAxis.x = startAxis.x;
                                } else currentAxis.x = -1;
                                if ( startAxis.y != -1) {
                                    inputDevice.SendKeyDown(startAxis.y);
                                    currentAxis.y = startAxis.y;
                                } else currentAxis.y = -1;
                                Dbg.Msg("Click poos " + startAxis.x + " _ " + startAxis.y);
                            });
                            newTouchElement.onMove.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 dt = new Int2((int) motionEvent.getX() - elSize.x / 2,
                                        (int) motionEvent.getY() - elSize.y / 2);

                                Dbg.Msg("Click XXXX " + dt.x + " _ " + dt.y);


                                boolean isInYAxisZone = (dt.x > -deadZoneY && dt.x < deadZoneY);
                                boolean isInUpperZone = (dt.y > 0 && isInYAxisZone || dt.y > deadZoneY);
                                boolean isInLowerZone = (dt.y < 0 && isInYAxisZone || dt.y < -deadZoneY);

                                boolean isInXAxisZone = (dt.y > -deadZoneX && dt.y < deadZoneX);
                                boolean isInRightZone = (dt.x > 0 && isInXAxisZone || dt.x > deadZoneX);
                                boolean isInLeftZone = (dt.x < 0 && isInXAxisZone || dt.x < -deadZoneX);

                                startAxis.Set(isInUpperZone ? 83 : isInLowerZone ? 87 : -1,
                                        isInLeftZone ? 65 : isInRightZone ? 68 : -1);


                                if ( startAxis.x != -1 && startAxis.x != currentAxis.x) {
                                    inputDevice.SendKeyUp(currentAxis.x);
                                    currentAxis.x = startAxis.x;
                                    inputDevice.SendKeyDown(currentAxis.x);
                                }
                                if ( startAxis.y != -1 && startAxis.y != currentAxis.y) {
                                    inputDevice.SendKeyUp(currentAxis.y);
                                    currentAxis.y = startAxis.y;
                                    inputDevice.SendKeyDown(currentAxis.y);
                                }
                                Dbg.Msg("Click poos " + startAxis.x + " _ " + startAxis.y);
                            });
                            newTouchElement.onUp.addObserver((observable, o) -> {
                                if ( currentAxis.x != -1)
                                    inputDevice.SendKeyUp(currentAxis.x);
                                if ( currentAxis.y != -1)
                                    inputDevice.SendKeyUp(currentAxis.y);
                            });

                        } else if (touchControlElement.type == TYPE_STICK) {
                            final Int2 startClickPos = new Int2(0,0);
                            newTouchElement.onDown.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                startClickPos.Set((int) (motionEvent.getX() - elSize.x / 2.0),
                                        (int) (motionEvent.getY() - elSize.y / 2.0));
                            });
                            newTouchElement.onMove.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                                        (int) (motionEvent.getY() - elSize.y / 2.0));

                                final Int2 diff = clickPos.Sub(startClickPos)
                                        .Div(1.0f / (touchControlElement.sensivity));

                                inputDevice.SendMouseShift(diff.x, diff.y);
                            });
                            newTouchElement.onUp.addObserver((observable, o) -> {
                                inputDevice.SendMouseShift(0,0);
                            });
                        } else if (touchControlElement.type == TYPE_MOUSE_ZONE) {
                            final Int2 startClickPos = new Int2(0,0);
                            newTouchElement.onDown.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                                        (int) (motionEvent.getY() - elSize.y / 2.0));
                                startClickPos.Set(clickPos.x, clickPos.y);
                            });
                            newTouchElement.onMove.addObserver((observable, o) -> {
                                MotionEvent motionEvent = (MotionEvent) o;
                                final Int2 clickPos = new Int2((int) (motionEvent.getX() - elSize.x / 2.0),
                                        (int) (motionEvent.getY() - elSize.y / 2.0));

                                final Int2 diff = clickPos.Sub(startClickPos)
                                        .Div(1.0f / (touchControlElement.sensivity));

                                float slope = 0.5f;
                                diff.x += (int)(Math.max(Math.pow(Math.abs(diff.x), slope),0f));
                                diff.y += (int)(Math.max(Math.pow(Math.abs(diff.y), slope),0f));

                                inputDevice.SendMouseShift(diff.x, diff.y);
                                startClickPos.Set(clickPos.x, clickPos.y);
                            });

                            newTouchElement.onClick.addObserver((observable, o) -> {
                                inputDevice.SendMouseClick(0);
                            });
                            newTouchElement.onUp.addObserver((observable, o) -> {
                                inputDevice.SendMouseShift(0,0);
                            });


                            /*newTouchElement.setOnHoverListener((view, motionEvent) -> {
                                Dbg.Msg("EVT " + motionEvent.getAction() + " " + motionEvent.getRawX() + " " + motionEvent.getRawY());
                                return false;
                            });*/
                        }
                    }
                    root.addView(newTouchElement);
                    windowElements.add(newTouchElement);
                }
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
}
