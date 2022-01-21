package com.catfixture.virgloverlay.core.input.windows.touchControls;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CIRCLE_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_CROSS;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_ROUNDED_BUTTON;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType.TYPE_STICK;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.InputController;
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
import com.catfixture.virgloverlay.core.input.windows.touchControls.types.TouchableWindowElementType;
import com.catfixture.virgloverlay.core.input.windows.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;


public class TouchControlsWindow extends BasicInputWindow {
    private TouchControlsEditor touchControlsEditor;

    public TouchControlsWindow(Context context) {
        super(context);
    }

    @Override
    public IWindow Init() {
        window = new AndroidWindow(context);
        window.CreateRelativeLayoutContainer()
                .EnableEvents()
                .SetTranlucent()
                .SetOverlay()
                .SetPosition(0,0)
                .SetFullscreen()
                .SetAlpha(1f)
                .Attach();

        InflateControls();
        OpenTouchControlsEditor();
        return window;
    }

    public void OpenTouchControlsEditor() {
        touchControlsEditor = new TouchControlsEditor(context, window);
        touchControlsEditor.onSetChanged.addObserver((observable, o) -> {
            InflateControls();
            OpenTouchControlsEditor();
        });
    }
    private void InflateControls() {
        window.GetContainer().removeAllViews();

        InputController ic = app.GetInputController();
        InputConfig cfgData = ic.GetConfigData();

        if (cfgData.HasCurrentProfile()) {
            InputConfigProfile cfgProfile = cfgData.GetCurrentProfile();
            for (InputTouchControlElement touchControlElement : cfgProfile.touchControlElements) {
                TouchableWindowElement newTouchElement = null;

                switch (touchControlElement.type) {
                    case TYPE_ROUNDED_BUTTON: {
                        newTouchElement = new RoundButton(context);
                        break;
                    }
                    case TYPE_CIRCLE_BUTTON: {
                        newTouchElement = new CircleButton(context);
                        break;
                    }
                    case TYPE_CROSS: {
                        newTouchElement = new CrossButton(context);
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
                    final IInputWindowElement iwe = newTouchElement;
                    newTouchElement.onDown.addObserver((observable, o) -> {
                        touchControlsEditor.SetSelected(iwe);
                    });
                    window.GetContainer().addView(newTouchElement);
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
