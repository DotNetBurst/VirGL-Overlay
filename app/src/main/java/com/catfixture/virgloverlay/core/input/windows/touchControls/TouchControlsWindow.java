package com.catfixture.virgloverlay.core.input.windows.touchControls;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.windows.BasicInputWindow;
import com.catfixture.virgloverlay.core.input.windows.editor.OverlaySettingsPanel;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;


public class TouchControlsWindow extends BasicInputWindow {
    @Override
    public IWindow Init() {
        IWindow window = new AndroidWindow(context);
        window.CreateRelativeLayoutContainer()
                .EnableEvents()
                .SetTranlucent()
                .SetOverlay()
                .SetPosition(0,0)
                .SetFullscreen()
                .SetAlpha(1f)
                .Attach();

        /*RoundButton roundButton = new RoundButton(context);
        roundButton.SetAlpha(0.5f)
                .SetPosition(new Int2(100,100))
                .SetSize(new Int2(100,100));
        roundButton.SetText("A");
        new DragAndDropHandle(roundButton);
        window.GetContainer().addView(roundButton);

        CircleButton circleButton = new CircleButton(context);
        circleButton.SetAlpha(0.5f)
                .SetPosition(new Int2(200,200))
                .SetSize(new Int2(100,100));
        circleButton.SetText("X");
        new DragAndDropHandle(circleButton);
        window.GetContainer().addView(circleButton);

        CrossButton crossButton = new CrossButton(context);
        crossButton.SetAlpha(0.5f)
                .SetPosition(new Int2(200,200))
                .SetSize(new Int2(300,300));
        new DragAndDropHandle(crossButton);
        window.GetContainer().addView(crossButton);*/

        OverlaySettingsPanel overlaySettingsPanel = new OverlaySettingsPanel(context, window.GetContainer());
        overlaySettingsPanel.SetAlpha(0.5f);

        //InputWindowElementEditor inputWindowElementEditor = new InputWindowElementEditor(context, window.GetContainer());
        //inputWindowElementEditor.SetSize(new Int2(600, WRAP_CONTENT));


        return window;
    }

    public TouchControlsWindow(Context context) {
        super(context);
    }
}
