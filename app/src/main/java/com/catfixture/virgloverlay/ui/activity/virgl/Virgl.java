package com.catfixture.virgloverlay.ui.activity.virgl;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import com.catfixture.virgloverlay.core.utils.android.IActivityLaunchable;
import com.catfixture.virgloverlay.core.utils.android.IPermissionGrantable;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.google.android.material.tabs.TabLayout;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.databinding.ActivityVirglBinding;
import com.catfixture.virgloverlay.ui.activity.virgl.tabs.MainTabsController;
import com.catfixture.virgloverlay.ui.utils.Utils;

public class Virgl extends AppCompatActivity implements IPermissionGrantable, IActivityLaunchable {
    private Action<ActivityResult> onResult;
    private ActivityResultLauncher<Intent> launchSomeActivity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == Const.PERMISSION_REQUEST_CODE) {
            onGranted.notifyObservers(resultCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == Const.PERMISSION_REQUEST_CODE) {
            onGranted.notifyObservers(grantResults[0]);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String stopServerExtra = getIntent().getStringExtra("stopServer");
        if ( stopServerExtra != null) {
            try {
                app.GetServerController().Stop(true);
            } catch (Exception e) {
                Dbg.Error(e);
            }
        }

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> onResult.Invoke(result));

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        com.catfixture.virgloverlay.databinding.ActivityVirglBinding binding = ActivityVirglBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ConfigureStyle();
    }

    private void ConfigureStyle() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        Utils.InitToolbarText(toolbar);

        TabLayout tabs = findViewById(R.id.mainTabs);
        MainTabsController mainTabsController = new MainTabsController(tabs, this);
        mainTabsController.SetTab(app.GetConfigData().lastSelectedMainTab);
        mainTabsController.OnTabsSelectionChanged((i) -> app.GetConfigData().SetLastSelectedMainTab(i.getPosition()));
    }

    @Override
    public void Launch(Intent intent, Action<ActivityResult> onResult) {
        this.onResult = onResult;
        launchSomeActivity.launch(intent);
    }
}