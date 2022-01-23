package com.catfixture.virgloverlay.core.input.overlay.touchControls.elements;

import static com.catfixture.virgloverlay.core.input.overlay.touchControls.TouchControlHelper.CreateButtonLabel;

import android.content.Context;
import android.widget.TextView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.overlay.IInputWindowElement;
import com.catfixture.virgloverlay.core.input.overlay.utils.IDraggable;
import com.catfixture.virgloverlay.core.input.overlay.utils.ITouchable;
import com.catfixture.virgloverlay.core.input.overlay.utils.ITransformable;
import com.catfixture.virgloverlay.core.utils.math.Int2;
import com.catfixture.virgloverlay.core.utils.types.Event;

public class StickMapperElement extends TouchableWindowElement {

    public StickMapperElement(Context context, int id) {
        super(context, id);

        initialSize.Set(400,400);

        setBackgroundResource(R.drawable.fx_tc_rect_rnd_btn);
    }

}


