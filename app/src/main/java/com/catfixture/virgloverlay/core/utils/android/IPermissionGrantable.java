package com.catfixture.virgloverlay.core.utils.android;

import com.catfixture.virgloverlay.core.utils.types.AutoResetEvent;

public interface IPermissionGrantable {
    AutoResetEvent onGranted = new AutoResetEvent();
}
