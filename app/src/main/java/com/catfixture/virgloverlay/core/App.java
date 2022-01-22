package com.catfixture.virgloverlay.core;

import static com.catfixture.virgloverlay.core.input.windows.editor.MainControlsOverlayFragment.ID_MAIN_CONTROLS_OVERLAY;
import static com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsOverlayFragment.ID_TOUCH_CONTROLS_OVERLAY;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.APP_TAG;

import android.app.Application;
import android.util.Log;

import com.catfixture.virgloverlay.core.debug.logging.GlobalExceptions;
import com.catfixture.virgloverlay.core.input.data.InputConfigData;
import com.catfixture.virgloverlay.core.input.windows.editor.MainControlsOverlayFragment;
import com.catfixture.virgloverlay.core.input.windows.touchControls.TouchControlsOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.data.GenericConfig;
import com.catfixture.virgloverlay.core.impl.ServerController;

public class App extends Application {
    public static App app;

    private GenericConfig<MainConfigData> mainConfig;
    private GenericConfig<InputConfigData> inputConfig;

    private ServerController serverController;
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

    public ServerController GetServerController() {
        return serverController;
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

        serverController = new ServerController(this);
        overlayManager = new OverlayManager(this);


        TouchControlsOverlayFragment touchControlsFragment = new TouchControlsOverlayFragment();
        overlayManager.Add(touchControlsFragment);
        app.GetOverlayManager().Show(ID_TOUCH_CONTROLS_OVERLAY);

        MainControlsOverlayFragment mainControlsOverlayFragment = new MainControlsOverlayFragment();
        overlayManager.Add(mainControlsOverlayFragment);
        app.GetOverlayManager().Show(ID_MAIN_CONTROLS_OVERLAY);

        Log.d(APP_TAG, "Application created");
    }


    public void TryGetProfile(Action<ConfigProfile> onRetrieve) {
        if ( GetMainConfigData().HasCurrentProfile()) {
            onRetrieve.Invoke(GetMainConfigData().GetCurrentProfile());
        }
    }
}
