package com.catfixture.virgloverlay.core.utils.types;

public class AutoResetEvent extends Event {
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
        deleteObservers();
    }
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
        deleteObservers();
    }
}
