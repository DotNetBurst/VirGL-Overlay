package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.overlay.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.overlay.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;
import com.catfixture.virgloverlay.core.utils.math.Int2;


public class StickElement extends TouchableWindowElement {

    public StickElement(Context context, int id) {
        super(context, id);
        initialSize.Set(300,300);
        setBackgroundResource(R.drawable.fx_tc_circle_btn);
    }
}
