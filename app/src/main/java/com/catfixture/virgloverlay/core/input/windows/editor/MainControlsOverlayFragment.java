package com.catfixture.virgloverlay.core.input.windows.editor;

import static android.view.View.MeasureSpec.UNSPECIFIED;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.Devices;
import com.catfixture.virgloverlay.core.input.animations.ResizeWidthAnimation;
import com.catfixture.virgloverlay.core.overlay.IOverlayFragment;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;


public class MainControlsOverlayFragment implements IOverlayFragment {
    public final static int ID_MAIN_CONTROLS_OVERLAY = 10000;

    private int collapsedWidth;
    private int expandedWidth = 660;
    private ImageView keyboardToggle;
    private ImageView touchControlsToggle;
    private int currentInputDevice;
    private ViewGroup root;
    private boolean isExpanded;

    @Override
    public int GetID() {
        return ID_MAIN_CONTROLS_OVERLAY;
    }

    @Override
    public void Create(Context context) {
        root = (ViewGroup) View.inflate(context, R.layout.overlay_settings_panel, null);
        LayoutUtils.SetWrapWrap(root);

        View subPanel = root.findViewById(R.id.subPanel);
        ImageView settingsIco = root.findViewById(R.id.settingsIco);

        settingsIco.setOnClickListener((o) -> {
            isExpanded = !isExpanded;
            SetExpanded(isExpanded);
        });

        keyboardToggle = root.findViewById(R.id.keyboardToggle);
        touchControlsToggle = root.findViewById(R.id.touchControlsToggle);
        ImageView minimize = root.findViewById(R.id.minimize);
        ImageView close = root.findViewById(R.id.close);

        keyboardToggle.setOnClickListener(view -> {
            SetCurrentInputDevice(context, Devices.SCREEN_KEYBOARD_DEVICE);
            app.GetInputConfigData().SetInputDevice(currentInputDevice);
            SetExpanded(false);
        });
        touchControlsToggle.setOnClickListener(view -> {
            SetCurrentInputDevice(context, Devices.TOUCH_CONTROLS_DEVICE);
            app.GetInputConfigData().SetInputDevice(currentInputDevice);
            SetExpanded(false);
        });
        touchControlsToggle.setOnLongClickListener(view -> {
            SetCurrentInputDevice(context, Devices.TOUCH_CONTROLS_DEVICE);
            app.GetInputConfigData().SetInputDevice(currentInputDevice);
            SetExpanded(false);
            return true;
        });
        SetCurrentInputDevice(context, app.GetInputConfigData().inputDevice);

        root.measure(UNSPECIFIED, UNSPECIFIED);
        collapsedWidth = root.getMeasuredWidth();
        SetExpanded(false);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public ViewGroup GetContainer() {
        return root;
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
        root.clearAnimation();
        if ( is) {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(root, expandedWidth);
            anim.setDuration(100);
            //SetAlpha(1);
            root.startAnimation(anim);
        } else {
            ResizeWidthAnimation anim = new ResizeWidthAnimation(root, collapsedWidth);
            anim.setDuration(100);
            root.postDelayed(() -> {
                //SetAlpha(0.5f);
            }, 100);
            root.startAnimation(anim);
        }
    }
}
