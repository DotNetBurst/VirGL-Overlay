package com.catfixture.virgloverlay.core.input.windows.touchControls;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.windows.BasicInputWindow;
import com.catfixture.virgloverlay.core.input.windows.editor.TouchControlsEditor;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CircleButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.CrossButton;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.RoundButton;
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

        OpenTouchControlsEditor();

        return window;
    }

    public void OpenTouchControlsEditor() {
        touchControlsEditor = new TouchControlsEditor(context, window.GetContainer());
    }

    @Override
    public void Destroy() {
        super.Destroy();
        touchControlsEditor.Destroy();
    }
}
