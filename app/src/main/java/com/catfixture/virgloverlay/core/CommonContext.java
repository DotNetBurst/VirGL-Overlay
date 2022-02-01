package com.catfixture.virgloverlay.core;

import static com.catfixture.virgloverlay.core.AppContext.app;

import android.content.Context;

import com.catfixture.virgloverlay.core.impl.ServerController;
import com.catfixture.virgloverlay.ui.activity.virgl.Virgl;

public class CommonContext {
    public static final CommonContext comCtx = new CommonContext();

    private ServerController serverController;
    public ServerController GetServerController() {
        return serverController;
    }
    public void Create(Context context) {
        serverController = new ServerController(context);
    }

    public void Destroy() {
        serverController.Destroy();
    }
}
