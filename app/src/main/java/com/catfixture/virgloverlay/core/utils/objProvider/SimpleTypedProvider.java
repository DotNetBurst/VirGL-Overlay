package com.catfixture.virgloverlay.core.utils.objProvider;

import com.catfixture.virgloverlay.core.utils.types.delegates.Functions;
public class SimpleTypedProvider<T> implements ITypedProvider<T> {
    private final Functions.Function0<T> o;

    public SimpleTypedProvider(Functions.Function0<T> o) {
        this.o = o;
    }

    @Override
    public T get() {
        return o.Invoke();
    }
}
