package com.catfixture.virgloverlay.core;

import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.app.Application;
import android.util.Log;

import com.catfixture.virgloverlay.core.debug.logging.GlobalExceptions;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.data.GenericConfig;
import com.catfixture.virgloverlay.core.impl.ServerController;

public class AppContext extends Application {
    public static AppContext app;

    private GenericConfig<MainConfigData> mainConfig;
    private GenericConfig<InputConfigData> inputConfig;

    private OverlayManager overlayManager;

    public MainConfigData GetMainConfigData() {
        return mainConfig.GetData();
    }
    public InputConfigData GetInputConfigData() {
        return inputConfig.GetData();
    }

    public void SaveMainConfig() {
        mainConfig.Save();
    }
    public void SaveInputConfig() {
        inputConfig.Save();
    }

    public OverlayManager GetOverlayManager() { return overlayManager;}

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        GlobalExceptions.Init();

        String mainConfigPath = getFilesDir().getPath() + "/settings.json";
        mainConfig = new GenericConfig<>(mainConfigPath, MainConfigData.class);
        String inputConfigPath = getFilesDir()+"/input.json";
        inputConfig = new GenericConfig<>( inputConfigPath, InputConfigData.class);
        overlayManager = new OverlayManager(this);

        Log.d(APP_TAG, "Application created");
    }


    public void TryGetProfile(Action<ConfigProfile> onRetrieve) {
        if ( GetMainConfigData().HasCurrentProfile()) {
            onRetrieve.Invoke(GetMainConfigData().GetCurrentProfile());
        }
    }

}
