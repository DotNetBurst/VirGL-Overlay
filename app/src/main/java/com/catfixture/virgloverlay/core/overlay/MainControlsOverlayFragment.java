package com.catfixture.virgloverlay.core.overlay;

import static android.view.View.MeasureSpec.UNSPECIFIED;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.input.android.MessageReceiver.PrepareMessage;
import static com.catfixture.virgloverlay.core.input.devices.Devices.DEVICE_NONE;
import static com.catfixture.virgloverlay.core.input.devices.Devices.TOUCH_DEVICE;
import static com.catfixture.virgloverlay.core.input.devices.touch.interaction.editor.TouchDeviceEditorOverlayFragment.ID_TOUCH_CONTROLS_EDITOR_OVERLAY;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.BCAST_ACTION_STOP_SERVER;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.devices.Devices;
import com.catfixture.virgloverlay.core.input.animations.ResizeWidthAnimation;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;


public class MainControlsOverlayFragment implements IOverlayFragment {
    public final static int ID_MAIN_CONTROLS_OVERLAY = 10000;

    private int collapsedWidth;
    private int expandedWidth = 660;
    private ImageView keyboardToggle;
    private ImageView touchControlsToggle;
    private ImageView miceKeyboardToggle;
    private int currentInputDevice = TOUCH_DEVICE;
    private boolean isExpanded;
    private final ViewGroup root;
    private final ViewGroup mainCont;

    @Override
    public int GetID() {
        return ID_MAIN_CONTROLS_OVERLAY;
    }

    public MainControlsOverlayFragment(Context context) {
        //SEMI USELESS ROOT FOR STYLE
        root = (ViewGroup) View.inflate(context, R.layout.overlay_settings_panel, null);
        LayoutUtils.SetMatchWrap(root);

        mainCont = root.findViewById(R.id.mainCont);
        ViewGroup controlsCont = root.findViewById(R.id.controlsContainer);

        mainCont.measure(UNSPECIFIED, UNSPECIFIED);
        collapsedWidth = mainCont.getMeasuredWidth();
        controlsCont.setVisibility(View.VISIBLE);

        mainCont.measure(UNSPECIFIED, UNSPECIFIED);
        expandedWidth = mainCont.getMeasuredWidth();

        mainCont.getLayoutParams().width = collapsedWidth;
        mainCont.requestLayout();

        ImageView settingsIco = root.findViewById(R.id.settingsIco);

        settingsIco.setOnClickListener((o) -> {
            isExpanded = !isExpanded;
            SetExpanded(isExpanded);
        });

        keyboardToggle = root.findViewById(R.id.keyboardToggle);
        touchControlsToggle = root.findViewById(R.id.touchControlsToggle);
        miceKeyboardToggle = root.findViewById(R.id.miceKeyboard);
        ImageView close = root.findViewById(R.id.close);

        close.setOnClickListener(view -> {
            Intent msg = PrepareMessage(context, BCAST_ACTION_STOP_SERVER);
            context.sendBroadcast(msg);
        });

        keyboardToggle.setOnClickListener(view -> {
            //SetCurrentInputDevice(context, Devices.KEYBOARD_DEVICE);
            //app.GetInputConfigData().SetInputDevice(currentInputDevice);
            SetExpanded(false);
        });
        touchControlsToggle.setOnClickListener(view -> {
            //SetCurrentInputDevice(context, TOUCH_DEVICE);
            //app.GetInputConfigData().SetInputDevice(currentInputDevice);
            //SetExpanded(false);
            //app.GetOverlayManager().Show(ID_TOUCH_CONTROLS_OVERLAY);
        });
        touchControlsToggle.setOnLongClickListener(view -> {
            SetCurrentInputDevice(context, TOUCH_DEVICE);
            app.GetInputConfigData().SetInputDevice(currentInputDevice);
            SetExpanded(false);
            app.GetOverlayManager().Show(ID_TOUCH_CONTROLS_EDITOR_OVERLAY);
            return true;
        });
        int lastInputDevice = app.GetInputConfigData().inputDevice;
        if (lastInputDevice != DEVICE_NONE)
            SetCurrentInputDevice(context, lastInputDevice);
        else
            UpdateButtonsColors(context, currentInputDevice);

    }
  
    @Override
    public ViewGroup GetContainer() {
        return root;
    }

    @Override
    public void OnFragmentShown() {

    }

    @Override
    public void OnFragmentHidden() {

    }

    private void SetCurrentInputDevice(Context context, int currentInputDevice) {
        this.currentInputDevice = currentInputDevice;
        UpdateButtonsColors(context, currentInputDevice);
    }

    private void UpdateButtonsColors(Context context, int currentInputDevice) {
        int selectedColor = context.getColor(R.color.yellow);
        int normalColor = context.getColor(R.color.white);
        keyboardToggle.setColorFilter(currentInputDevice == Devices.KEYBOARD_DEVICE ? selectedColor : normalColor);
        touchControlsToggle.setColorFilter(currentInputDevice == TOUCH_DEVICE ? selectedColor : normalColor);
    }

    private void SetExpanded(boolean is) {
        mainCont.clearAnimation();
        ResizeWidthAnimation anim;
        if ( is) {
            anim = new ResizeWidthAnimation(mainCont, expandedWidth);
            anim.setDuration(100);
            mainCont.setAlpha(1);
        } else {
            anim = new ResizeWidthAnimation(mainCont, collapsedWidth);
            anim.setDuration(100);
            mainCont.postDelayed(() -> {
                mainCont.setAlpha(0.5f);
            }, 100);
        }
        mainCont.startAnimation(anim);
    }
}
