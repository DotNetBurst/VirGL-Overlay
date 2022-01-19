package com.catfixture.virgloverlay.core.android;

import com.catfixture.virgloverlay.core.types.AutoResetEvent;

public interface IPermissionGrantable {
    AutoResetEvent onGranted = new AutoResetEvent();
}
