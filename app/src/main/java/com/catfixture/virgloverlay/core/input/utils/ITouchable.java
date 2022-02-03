package com.catfixture.virgloverlay.core.input.utils;

import com.catfixture.virgloverlay.core.utils.types.Event;

public interface ITouchable {
    Event OnDown();
    Event OnMove();
    Event OnUp();
    Event OnClick();
}
