package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings;

import static com.catfixture.virgloverlay.core.App.app;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.custom.WarningComponent;
import com.google.android.material.tabs.TabLayout;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.data.MainConfigData;
import com.catfixture.virgloverlay.data.ConfigProfile;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.tabs.SettingsTabsController;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericSpinnerAdapter;
import com.catfixture.virgloverlay.ui.common.interactions.ConfirmDialog;
import com.catfixture.virgloverlay.ui.common.interactions.InputDialog;

public class SettingsFragment extends Fragment {
    private View view;
    private GenericSpinnerAdapter<ConfigProfile> configProfilesAdapter;
    private SettingsTabsController settingsTabsController;
    private View editProfileName;
    private View removeProfile;
    private Runnable onChanged;
    private WarningComponent warningComponent;
    private View profilesPanel;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        Context context = getContext();
        MainConfigData cfgData = app.GetMainConfigData();
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;

        warningComponent = view.findViewById(R.id.serverLaunchedNotif);
        profilesPanel = view.findViewById(R.id.profilesPanel);

        TabLayout tabs = view.findViewById(R.id.settingsTabs);
        settingsTabsController = new SettingsTabsController(view, tabs, activity);
        settingsTabsController.SetTab(app.GetMainConfigData().lastSelectedSettingsTab);
        settingsTabsController.OnTabsSelectionChanged((i) -> {
            cfgData.SetLastSelectedSettingsTab(i.getPosition());
            UpdateAll();
        });

        Spinner spinner = view.findViewById(R.id.configProfileSelector);
        configProfilesAdapter = new GenericSpinnerAdapter<>(activity, R.layout.spinner_item, cfgData.profiles, (i) -> {
        });
        configProfilesAdapter.EnableCustomTitleAction((textView, pos) -> {
            if (cfgData.currentProfile == pos) {
                textView.setTypeface(null, Typeface.BOLD);
            }
        });
        spinner.setAdapter(configProfilesAdapter);
        if ( cfgData.HasCurrentProfile())
            spinner.setSelection(cfgData.currentProfile);
        else spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cfgData.SetCurrentProfile(i);
                UpdateAll();
                SettingItem.OnAppWideSettingsChanged.notifyObservers();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        ImageView createNewConfigProfile = view.findViewById(R.id.createNewConfigProfile);
        createNewConfigProfile.setOnClickListener(view1 -> ConfirmDialog.Show(context, "Create configuration profile",
                "Do you want to create new configuration profile?", "Create", () -> {
                    ConfigProfile newCfgProf = new ConfigProfile();
                    cfgData.AddProfile(newCfgProf);
                    ReselectProfileAndUpdate(spinner, cfgData);
                    settingsTabsController.UpdateAll();
                    SettingItem.OnAppWideSettingsChanged.notifyObservers();
                }, "Cancel", null));

        editProfileName = view.findViewById(R.id.editProfileName);
        editProfileName.setOnClickListener(view1 -> {
            ConfigProfile cfgProf = app.GetMainConfigData().GetCurrentProfile();
            InputDialog.Show(getContext(), "Edit name", cfgProf.name, "Save", (newName) -> {
                app.GetMainConfigData().GetCurrentProfile().SetName(newName);
                UpdateAll();
                SettingItem.OnAppWideSettingsChanged.notifyObservers();
            }, "Cancel", null);
        });

        removeProfile = view.findViewById(R.id.removeProfile);
        removeProfile.setOnClickListener(view1 -> {
            int currProfile = app.GetMainConfigData().currentProfile;
            ConfirmDialog.Show(context, "Remove " + cfgData.FindProfileByID(currProfile).name +" configuration profile",
                    "Do you really want to remove this profile?", "Remove", () -> {
                        cfgData.RemoveProfile(currProfile);
                        ReselectProfileAndUpdate(spinner, cfgData);
                        settingsTabsController.UpdateAll();
                        SettingItem.OnAppWideSettingsChanged.notifyObservers();
                    }, "Cancel", null);
        });
        UpdateProfileControls();

        return view;
    }

    private void ReselectProfileAndUpdate(Spinner spinner, MainConfigData cfgData) {
        configProfilesAdapter.notifyDataSetChanged();
        spinner.post(() -> {
            int last = cfgData.profiles.size()-1;
            if ( last >= 0) {
                cfgData.SetCurrentProfile(last);
                spinner.setSelection(last);
            }
            UpdateAll();
            spinner.postInvalidate();
        });
    }

    private void UpdateAll() {
        UpdateProfileControls();

        configProfilesAdapter.notifyDataSetChanged();
        settingsTabsController.UpdateAll();

        if ( onChanged != null)
            onChanged.run();
    }

    private void UpdateProfileControls() {
        boolean profileControlsVisible = app.GetMainConfigData().HasCurrentProfile();

        int vis = profileControlsVisible ? View.VISIBLE : View.GONE;
        editProfileName.setVisibility(vis);
        removeProfile.setVisibility(vis);
        view.findViewById(R.id.noProfilesLabel).setVisibility(profileControlsVisible ? View.GONE : View.VISIBLE);
        settingsTabsController.ToggleViewportVisibility(profileControlsVisible);
    }

    public void OnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    public void UpdateMainView() {
        view.post(() -> {
            try {
                boolean serverRunning = app.GetServerController().IsStarted();
                warningComponent.setVisibility(serverRunning ? View.VISIBLE : View.GONE);
                profilesPanel.setVisibility(serverRunning ? View.GONE : View.VISIBLE);
            } catch (Exception x) {
                Dbg.Error(x);
            }
        });
    }
}
