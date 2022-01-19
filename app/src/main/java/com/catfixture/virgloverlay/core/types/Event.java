package com.catfixture.virgloverlay.core.types;

import java.util.Observable;

public class Event extends Observable {
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
