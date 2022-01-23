package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import android.content.Context;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.data.InputTouchControlElement;
import com.catfixture.virgloverlay.core.input.overlay.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.overlay.utils.DragAndDropHandle;
import com.catfixture.virgloverlay.core.utils.android.LayoutUtils;
import com.catfixture.virgloverlay.core.utils.math.Int2;


public class StickElement extends TouchableWindowElement {
    private StickMapperElement mapperElement;

    public StickElement(Context context, int id) {
        super(context, id);
        initialSize.Set(300,300);
        setBackgroundResource(R.drawable.fx_tc_circle_btn);
    }

    public TouchableWindowElement CreateHandle(Context context) {
        InputTouchControlElement data = (InputTouchControlElement) GetData();
        mapperElement = new StickMapperElement(context, id);
        mapperElement.SetPosition(data.handlePosition.x,data.handlePosition.y);
        mapperElement.SetSize(initialSize.x, initialSize.y);
        mapperElement.initialSize.Set(initialSize.x, initialSize.y);
        mapperElement.SetScale(data.handleScale);

        DragAndDropHandle<StickMapperElement> dnd = new DragAndDropHandle<>(mapperElement);
        dnd.onPositionChanged.addObserver((observable, o) -> {
            InputTouchControlElement data1 = (InputTouchControlElement) GetData();
            data1.SetHandlePosition((Int2)o);
        });
        return mapperElement;
    }
}
