package com.catfixture.virgloverlay.core.input.windows.touchControls;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.RelativeLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.codes.InputCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CircleButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CrossButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.RoundButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.StickElement;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;

import java.util.ArrayList;
import java.util.List;


public class TouchControlsOverlayFragment implements IOverlayFragment {
    public final static int ID_TOUCH_CONTROLS_OVERLAY = 10001;

    private List<IInputWindowElement> windowElements;
    private InputConnection inputConnection;
    private ViewGroup root;
    private Context context;


    public void TryGetWindowElementById(int selectedItemId, Action<IInputWindowElement> onFind) {
        for (IInputWindowElement windowElement : windowElements) {
            if ( windowElement.GetId() == selectedItemId) {
                onFind.Invoke(windowElement);
                return;
            }
        }
    }

    private void InflateControls() {
        root.removeAllViews();
        windowElements.clear();

        InputConfigData cfgData = app.GetInputConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();
            for (InputTouchControlElement touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                switch (touchControlElement.type) {
                    case TYPE_ROUNDED_BUTTON: {
                        newTouchElement = new RoundButton(context, touchControlElement.id);
                        ((RoundButton)newTouchElement).SetText(InputCodes.GetCodeName(touchControlElement.buttonCode));
                        break;
                    }
                    case TYPE_CIRCLE_BUTTON: {
                        newTouchElement = new CircleButton(context, touchControlElement.id);
                        ((CircleButton)newTouchElement).SetText(InputCodes.GetCodeName(touchControlElement.buttonCode));
                        break;
                    }
                    case TYPE_CROSS: {
                        newTouchElement = new CrossButton(context, touchControlElement.id);
                        break;
                    }
                    case TYPE_STICK: {
                        newTouchElement = new StickElement(context, touchControlElement.id);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                if ( newTouchElement != null) {
                    newTouchElement.SetCustomData(touchControlElement);
                    newTouchElement.SetScale(touchControlElement.scale)
                        .SetAlpha(touchControlElement.alpha * cfgData.uiOpacity)
                        .SetPosition(touchControlElement.position.x,touchControlElement.position.y);

                    /*if ( touchControlsEditor != null) {
                        DragAndDropHandle dnd = new DragAndDropHandle(newTouchElement);
                        dnd.onPositionChanged.addObserver((obs, o) -> {
                            touchControlElement.SetPosition((Int2) o);
                        });
                        dnd.EnableSnap(25);

                        final int iwe = newTouchElement.GetId();
                        newTouchElement.onDown.addObserver((observable, o) -> {
                            touchControlsEditor.SetSelected(iwe);
                        });
                    } else {
                        newTouchElement.onDown.addObserver((observable, o) -> {
                            KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, touchControlElement.buttonCode);
                            inputConnection.sendKeyEvent(keyEvent);
                        });
                    }*/
                    root.addView(newTouchElement);
                    windowElements.add(newTouchElement);
                }
            }
        }

    }

    public void Destroy() {
        //touchControlsEditor.Destroy();
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

        //controlsContainer = root.findViewById(R.id.controlsContainer);

        //controlsContainer.setOnClickListener(view -> {
            //if (touchControlsEditor != null) {
            //    touchControlsEditor.SetSelected(-1);
            //}
        //});

        /*touchControlsEditor = new TouchControlsEditor(context, this, editorContainer, app.GetInputConfigData());
        touchControlsEditor.onSetChanged.addObserver((observable, o) -> {
            InflateControls();
            touchControlsEditor.PutOnTop();
        });
        touchControlsEditor.onClosed.addObserver((observable, o) -> {
            touchControlsEditor = null;
            InflateControls();
        });*/

        InflateControls();
    }
}
