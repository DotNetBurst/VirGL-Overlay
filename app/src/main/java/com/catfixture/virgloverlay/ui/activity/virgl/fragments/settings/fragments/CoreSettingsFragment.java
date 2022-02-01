package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments;

import static com.catfixture.virgloverlay.core.AppContext.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;
import com.catfixture.virgloverlay.ui.utils.GenericSettingsList;

public abstract class CoreSettingsFragment extends Fragment {
    private View view;

    private GenericMultiViewListAdapter<SettingItem> settingsViewAdapter;
    private final int fragmentLayout;
    private boolean isInitialized;

    public CoreSettingsFragment(int fragmentLayout) {
        this.fragmentLayout = fragmentLayout;

    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        view = inflater.inflate(fragmentLayout, container, false);
        BindGenericSettingsList();

        return view;
    }

    private void BindGenericSettingsList() {
        isInitialized = false;
        if (view == null || !app.GetMainConfigData().HasCurrentProfile()) return;

        RecyclerView settingsView = view.findViewById(R.id.settingsView);
        settingsViewAdapter = GenericSettingsList.InitGenericSettingsList(getContext(), settingsView);
        InitSettings(settingsViewAdapter);
        isInitialized = true;
    }

    protected abstract void InitSettings(GenericMultiViewListAdapter<SettingItem> settingsViewAdapter);
    public void UpdateAll() {
        if (!isInitialized) BindGenericSettingsList();
        if (settingsViewAdapter != null) {
            if (!app.GetMainConfigData().HasCurrentProfile()) {
                settingsViewAdapter.Flush();
                isInitialized = false;
            } else settingsViewAdapter.notifyDataSetChanged();
        }
    }
}
