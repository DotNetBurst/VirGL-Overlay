package com.catfixture.virgloverlay.core.input;

import android.content.Context;

import com.catfixture.virgloverlay.core.input.devices.IInputDevice;


public interface IInputProvider {
    IInputDevice ResolveDevice(Context context, Object arg);
}
