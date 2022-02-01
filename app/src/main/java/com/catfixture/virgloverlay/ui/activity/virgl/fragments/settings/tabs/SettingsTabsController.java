package com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.tabs;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.utils.types.delegates.Action;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments.InputFragment;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments.OverlayFragment;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments.RenderingFragment;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.fragments.SystemFragment;
import com.catfixture.virgloverlay.ui.common.tabController.pager.TabPagerFragment;
import com.catfixture.virgloverlay.ui.common.tabController.pager.TabsFragmentPagerAdapter;
import com.catfixture.virgloverlay.ui.common.tabController.TabsController;

public class SettingsTabsController extends TabsController {
    private final TabLayout tabs;
    private final ViewPager tabsPager;

    private final SystemFragment systemFragment = new SystemFragment();
    private final RenderingFragment renderingFragment = new RenderingFragment();
    private final OverlayFragment overlayFragment = new OverlayFragment();
    private final InputFragment inputFragment = new InputFragment();


    public SettingsTabsController(View parent, TabLayout tabs, AppCompatActivity activity) {
        this.tabs = tabs;

        @SuppressWarnings("deprecation") TabsFragmentPagerAdapter settingsTabsPagerAdapter = new TabsFragmentPagerAdapter(activity.getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        settingsTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.systemSettingsTab), systemFragment));

        settingsTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.renderingSettingsTab), renderingFragment));

        settingsTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.overlaySettingsTab), overlayFragment));

        settingsTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.inputSettingsTab),inputFragment));


        tabsPager = parent.findViewById(R.id.settingsTabsPager);
        tabsPager.setAdapter(settingsTabsPagerAdapter);
        tabsPager.setOffscreenPageLimit(4);

        tabs.setupWithViewPager(tabsPager);
        activity.runOnUiThread(() -> InitTabs(tabs));
    }


    private void InitTabs(TabLayout tabs) {
        TabLayout.Tab systemSettingsTab = tabs.getTabAt(0);
        assert systemSettingsTab != null;
        InitTab(systemSettingsTab, R.layout.tab_item_small, R.string.systemSettingsTab, R.drawable.system_ico);

        TabLayout.Tab renderingSettingsTab = tabs.getTabAt(1);
        assert renderingSettingsTab != null;
        InitTab(renderingSettingsTab, R.layout.tab_item_small,  R.string.renderingSettingsTab, R.drawable.rend_ico);

        TabLayout.Tab overlaySettingsTab = tabs.getTabAt(2);
        assert overlaySettingsTab != null;
        InitTab(overlaySettingsTab, R.layout.tab_item_small,  R.string.overlaySettingsTab, R.drawable.overlay_ico);

        TabLayout.Tab inputSettingsTab = tabs.getTabAt(3);
        assert inputSettingsTab != null;
        InitTab(inputSettingsTab, R.layout.tab_item_small,  R.string.inputSettingsTab, R.drawable.input_ico);
    }

    public void SetTab(int i) {
        tabs.selectTab(tabs.getTabAt(i));
    }

    public void OnTabsSelectionChanged(Action<TabLayout.Tab> onChanged) {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onChanged.Invoke(tab);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
    public void InvalidateAll() {
        systemFragment.Invalidate();
        renderingFragment.Invalidate();
        overlayFragment.Invalidate();
        inputFragment.Invalidate();
    }

    public void UpdateAll() {
        systemFragment.UpdateAll();
        renderingFragment.UpdateAll();
        overlayFragment.UpdateAll();
        inputFragment.UpdateAll();
    }

    public void ToggleViewportVisibility(boolean profileControlsVisible) {
        tabs.setVisibility(profileControlsVisible ? View.VISIBLE : View.GONE);
        tabsPager.setVisibility(profileControlsVisible ? View.VISIBLE : View.GONE);
    }
}