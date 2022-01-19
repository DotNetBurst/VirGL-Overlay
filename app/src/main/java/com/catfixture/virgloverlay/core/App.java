package com.catfixture.virgloverlay.core;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.app.Application;
import android.util.Log;

import com.catfixture.virgloverlay.core.debug.logging.GlobalExceptions;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import com.catfixture.virgloverlay.data.ConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.data.GenericConfig;
import com.catfixture.virgloverlay.core.impl.ServerController;

public class App extends Application {
    public static App app;

    private GenericConfig<ConfigData> config;
    private ServerController serverController;

    public ConfigData GetConfigData() {
        return config.GetData();
    }

    public void TryGetProfile(Action<ConfigProfile> onRetrieve) {
        if ( GetConfigData().HasCurrentProfile()) {
            onRetrieve.Invoke(GetConfigData().GetCurrentProfile());
        }
    }

    public void Save() {
        config.Save();
    }

    public ServerController GetServerController() {
        return serverController;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        GlobalExceptions.Init();
        String configPath = getFilesDir().getPath() + "/settings.json";
        Log.i(APP_TAG, "CONF_PATH " + configPath);
        config = new GenericConfig<>(configPath, ConfigData.class);

        serverController = new ServerController(getApplicationContext());
        Log.d(APP_TAG, "Application created");
    }
}
