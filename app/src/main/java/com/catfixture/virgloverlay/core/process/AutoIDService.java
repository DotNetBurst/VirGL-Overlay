package com.catfixture.virgloverlay.core.process;

import android.app.Service;

public abstract class AutoIDService extends Service {
    protected int serviceID;

    public AutoIDService() {
        if ( serviceID == -1) serviceID = ProcUtils.GetProcessIDByClassName(getClass());
    }
}
