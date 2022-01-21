package com.catfixture.virgloverlay.core.utils.types.delegates;

@SuppressWarnings("unused")
public class Functions {
    public interface Function0<T> {
        T Invoke();
    }
    public interface Function1<T,X1> {
        T Invoke(X1 arg1);
    }
    public interface Function2<T,X1,X2> {
        T Invoke(X1 arg, X2 arg2);
    }
}
