package com.catfixture.virgloverlay.core.input.windows.touchControls;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.InputController;
import com.catfixture.virgloverlay.core.input.codes.InputCodes;
import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.data.InputConfigProfile;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.windows.BasicInputWindow;
import com.catfixture.virgloverlay.core.input.windows.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.windows.editor.TouchControlsEditor;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CircleButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CrossButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.RoundButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.ArrayList;
import java.util.List;


public class TouchControlsWindow extends BasicInputWindow {
    private TouchControlsEditor touchControlsEditor;
    private List<IInputWindowElement> windowElements;

    public TouchControlsWindow(Context context) {
        super(context);
    }

    @Override
    public IWindow Init() {
        windowElements = new ArrayList<>();
        window = new AndroidWindow(context);
        window.CreateRelativeLayoutContainer()
                .EnableEvents()
                .SetTranlucent()
                .SetOverlay()
                .SetPosition(0,0)
                .SetFullscreen()
                .SetAlpha(1f)
                .Attach();

        window.GetContainer().setOnClickListener(view -> {
            if (touchControlsEditor != null) {
                touchControlsEditor.SetSelected(-1);
            }
        });

        InflateControls();
        OpenTouchControlsEditor();
        return window;
    }

    public void TryGetSelectedItem(int selectedItemId, Action<IInputWindowElement> onFind) {
        for (IInputWindowElement windowElement : windowElements) {
            if ( windowElement.GetId() == selectedItemId) {
                onFind.Invoke(windowElement);
                return;
            }
        }
    }

    public void OpenTouchControlsEditor() {
        touchControlsEditor = new TouchControlsEditor(context, this, app.GetInputController().GetConfigData());
        touchControlsEditor.onSetChanged.addObserver((observable, o) -> {
            InflateControls();
        });
    }
    private void InflateControls() {
        for (IInputWindowElement windowElement : windowElements) {

            window.GetContainer().removeView((TouchableWindowElement)windowElement);
        }
        windowElements.clear();

        InputController ic = app.GetInputController();
        InputConfig cfgData = ic.GetConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();
            for (InputTouchControlElement touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                switch (touchControlElement.type) {
                    case TYPE_ROUNDED_BUTTON: {
                        newTouchElement = new RoundButton(context, touchControlElement.id);
                        ((RoundButton)newTouchElement).SetText(InputCodes.GetCodeName(touchControlElement.code));
                        break;
                    }
                    case TYPE_CIRCLE_BUTTON: {
                        newTouchElement = new CircleButton(context, touchControlElement.id);
                        ((CircleButton)newTouchElement).SetText(InputCodes.GetCodeName(touchControlElement.code));
                        break;
                    }
                    case TYPE_CROSS: {
                        newTouchElement = new CrossButton(context, touchControlElement.id);
                        break;
                    }
                    case TYPE_STICK: {
                        break;
                    }
                    default: {
                        break;
                    }
                }
                if ( newTouchElement != null) {
                    newTouchElement.SetCustomData(touchControlElement);
                    newTouchElement.SetScale(touchControlElement.scale)
                        .SetAlpha(touchControlElement.alpha)
                        .SetPosition(touchControlElement.position.x,touchControlElement.position.y);

                    DragAndDropHandle dnd = new DragAndDropHandle(newTouchElement);
                    dnd.onPositionChanged.addObserver((obs, o) -> {
                        touchControlElement.SetPosition((Int2) o);
                    });
                    dnd.EnableSnap(25);
                    final int iwe = newTouchElement.GetId();
                    newTouchElement.onDown.addObserver((observable, o) -> {
                        touchControlsEditor.SetSelected(iwe);
                    });
                    window.GetContainer().addView(newTouchElement);
                    windowElements.add(newTouchElement);
                }
            }
        }

    }

    @Override
    public void Destroy() {
        super.Destroy();
        touchControlsEditor.Destroy();
    }
}
