package com.catfixture.virgloverlay.ui.activity.test;

import static android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS;

import static com.catfixture.virgloverlay.core.App.app;
import static com.catfixture.virgloverlay.core.input.overlay.MainControlsOverlayFragment.ID_MAIN_CONTROLS_OVERLAY;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.catfixture.virgloverlay.BuildConfig;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.input.IInputProvider;
import com.catfixture.virgloverlay.core.input.MainInputProvider;
import com.catfixture.virgloverlay.core.input.devices.IInputDevice;
import com.catfixture.virgloverlay.core.input.overlay.MainControlsOverlayFragment;
import com.catfixture.virgloverlay.core.overlay.OverlayManager;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        String allInputMethods = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_INPUT_METHODS);
        Log.e("EEE", allInputMethods);
        if ( !allInputMethods.contains(BuildConfig.IME_ID)) {
            startActivity( new Intent(ACTION_INPUT_METHOD_SETTINGS));
        }

        AndroidUtils.ForceAppToImmersive(BuildConfig.APPLICATION_ID, (e) -> {

        });

        OverlayManager overlayManager = app.GetOverlayManager();
        MainControlsOverlayFragment mainControlsOverlayFragment = new MainControlsOverlayFragment();
        overlayManager.Add(mainControlsOverlayFragment);
        overlayManager.Show(ID_MAIN_CONTROLS_OVERLAY);

        IInputProvider inputProvider = new MainInputProvider();
        IInputDevice device = inputProvider.ResolveDevice(this, app.GetInputConfigData());
        device.Show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void ShowInputMethod(View view) {
        view.requestFocus();

        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        im.showInputMethodPicker();

    }
}