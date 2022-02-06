package com.catfixture.virgloverlay.ui.activity.virgl;

import static com.catfixture.virgloverlay.core.AppContext.app;
import static com.catfixture.virgloverlay.core.utils.android.Installer.CopyInstallerToDownload;

import android.Manifest;
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

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.CommonContext;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.android.AndroidUtils;
import com.catfixture.virgloverlay.core.utils.android.FileUtils;
import com.catfixture.virgloverlay.core.utils.android.IActivityLaunchable;
import com.catfixture.virgloverlay.core.utils.android.IPermissionGrantable;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.databinding.ActivityVirglBinding;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.tabs.MainTabsController;
import com.catfixture.virgloverlay.ui.utils.Utils;
import com.google.android.material.tabs.TabLayout;

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
            if ( grantResults.length > 0)
                onGranted.notifyObservers(grantResults[0]);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonContext.comCtx.Create(this);

        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> onResult.Invoke(result));

        AndroidUtils.OnPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE, () -> {}, () -> {});
        AndroidUtils.OnPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, () -> {}, () -> {});

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        com.catfixture.virgloverlay.databinding.ActivityVirglBinding binding = ActivityVirglBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ConfigureStyle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonContext.comCtx.Destroy();
    }

    private void ConfigureStyle() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        Utils.InitToolbarText(toolbar);

        TabLayout tabs = findViewById(R.id.mainTabs);
        MainTabsController mainTabsController = new MainTabsController(tabs, this);
        mainTabsController.SetTab(app.GetMainConfigData().lastSelectedMainTab);
        mainTabsController.OnTabsSelectionChanged((i) -> app.GetMainConfigData().SetLastSelectedMainTab(i.getPosition()));
    }

    @Override
    public void Launch(Intent intent, Action<ActivityResult> onResult) {
        this.onResult = onResult;
        launchSomeActivity.launch(intent);
    }
}