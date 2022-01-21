package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.View.MeasureSpec.UNSPECIFIED;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.Devices;
import com.catfixture.virgloverlay.core.input.animations.ResizeWidthAnimation;
import com.catfixture.virgloverlay.core.input.data.InputConfig;
import com.catfixture.virgloverlay.core.input.windows.touchControls.elements.TouchableWindowElement;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;


public class OverlaySettingsPanel extends TouchableWindowElement {
    private final int collapsedWidth;
    private final int expandedWidth = 660;
    private final ImageView keyboardToggle;
    private final ImageView touchControlsToggle;
    private int currentInputDevice;
    private ViewGroup panel;
    private boolean isExpanded;
    private IWindow window;

    public OverlaySettingsPanel(Context context, InputConfig inputConfig) {
        super(context);

        window = new AndroidWindow(context);
        window.CreateRelativeLayoutContainer()
                .EnableEvents()
                .SetTranlucent()
                .SetOverlay()
                .SetPosition(0,0)
                .SetSize(expandedWidth, 100)
                .SetAlpha(1f)
                .Attach();

        panel = (ViewGroup) View.inflate(context, R.layout.overlay_settings_panel, null);
        panel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(panel);
        View subPanel = panel.findViewById(R.id.subPanel);

        SetAlpha(0.5f);

        ImageView settingsIco = panel.findViewById(R.id.settingsIco);

        settingsIco.setOnClickListener((o) -> {
            isExpanded = !isExpanded;
            SetExpanded(isExpanded);
        });

        keyboardToggle = panel.findViewById(R.id.keyboardToggle);
        touchControlsToggle = panel.findViewById(R.id.touchControlsToggle);
        ImageView minimize = panel.findViewById(R.id.minimize);
        ImageView close = panel.findViewById(R.id.close);

        keyboardToggle.setOnClickListener(view -> {
            SetCurrentInputDevice(context, Devices.SCREEN_KEYBOARD_DEVICE);
            app.GetInputController().SetInputDevice(currentInputDevice);
            SetExpanded(false);
        });
        touchControlsToggle.setOnClickListener(view -> {
            SetCurrentInputDevice(context, Devices.TOUCH_CONTROLS_DEVICE);
            app.GetInputController().SetInputDevice(currentInputDevice);
            SetExpanded(false);
        });
        touchControlsToggle.setOnLongClickListener(view -> {
            SetCurrentInputDevice(context, Devices.TOUCH_CONTROLS_DEVICE);
            app.GetInputController().SetInputDevice(currentInputDevice);
            SetExpanded(false);
            return true;
        });
        SetCurrentInputDevice(context, inputConfig.inputDevice);

        //new DragAndDropHandle(this);
        ((ViewGroup)window.GetContainer()).addView(this);

        panel.measure(UNSPECIFIED, UNSPECIFIED);
        collapsedWidth = panel.getMeasuredWidth();
    }

    private void SetCurrentInputDevice(Context context, int currentInputDevice) {
        this.currentInputDevice = currentInputDevice;
        UpdateButtonsColors(context, currentInputDevice);
    }

    private void UpdateButtonsColors(Context context, int currentInputDevice) {
        int selectedColor = context.getColor(R.color.yellow);
        int normalColor = context.getColor(R.color.white);
        keyboardToggle.setColorFilter(currentInputDevice == Devices.SCREEN_KEYBOARD_DEVICE ? selectedColor : normalColor);
        touchControlsToggle.setColorFilter(currentInputDevice == Devices.TOUCH_CONTROLS_DEVICE ? selectedColor : normalColor);
    }

    private void SetExpanded(boolean is) {
        panel.clearAnimation();
        if ( is) {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(panel, expandedWidth);
            anim.setDuration(100);
            SetAlpha(1);
            panel.startAnimation(anim);
        } else {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(panel, collapsedWidth);
            anim.setDuration(100);
            postDelayed(() -> SetAlpha(0.5f), 100);
            panel.startAnimation(anim);
        }
    }
}
