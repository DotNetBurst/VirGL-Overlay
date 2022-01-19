package com.catfixture.virgloverlay.core.objProvider;

import com.catfixture.virgloverlay.core.types.delegates.Functions;

public class SimpleObjectProvider implements IObjectProvider {
    private final Functions.Function0<Object> o;

    public SimpleObjectProvider(Functions.Function0<Object> o) {
        this.o = o;
    }

    @Override
    public Object get() {
        return o.Invoke();
    }
}
