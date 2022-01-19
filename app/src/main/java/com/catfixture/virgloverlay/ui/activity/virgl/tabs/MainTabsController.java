package com.catfixture.virgloverlay.ui.activity.virgl.tabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.about.AboutFragment;
import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.services.ServicesFragment;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.SettingsFragment;
import com.catfixture.virgloverlay.ui.common.tabController.pager.TabPagerFragment;
import com.catfixture.virgloverlay.ui.common.tabController.TabsController;
import com.catfixture.virgloverlay.ui.common.tabController.pager.TabsFragmentPagerAdapter;

@SuppressWarnings("deprecation")
public class MainTabsController extends TabsController {
    private final TabLayout tabs;

    public MainTabsController(TabLayout tabs, AppCompatActivity activity) {
        this.tabs = tabs;

        ServicesFragment servicesFragment = new ServicesFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        settingsFragment.OnChanged(servicesFragment::UpdateMainView);
        servicesFragment.OnChanged(settingsFragment::UpdateMainView);

        TabsFragmentPagerAdapter mainTabsPagerAdapter = new TabsFragmentPagerAdapter(activity.getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mainTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.servicesTab), servicesFragment));

        mainTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.settingsTab), settingsFragment));

        AboutFragment aboutFragment = new AboutFragment();
        mainTabsPagerAdapter.AddPagerFragment(new TabPagerFragment(
                activity.getResources().getString(R.string.aboutTab), aboutFragment));

        ViewPager tabsPager = activity.findViewById(R.id.mainTabsPager);
        tabsPager.setAdapter(mainTabsPagerAdapter);

        tabs.setupWithViewPager(tabsPager);
        activity.runOnUiThread(() -> InitTabs(tabs));
    }


    private void InitTabs(TabLayout tabs) {
        TabLayout.Tab servicesTab = tabs.getTabAt(0);
        TabLayout.Tab settingsTab = tabs.getTabAt(1);
        TabLayout.Tab aboutTab = tabs.getTabAt(2);

        if (servicesTab != null)
            InitTab(servicesTab, R.layout.tab_item, R.string.servicesTab, R.drawable.services_ico);
        if (settingsTab != null)
            InitTab(settingsTab, R.layout.tab_item,  R.string.settingsTab, R.drawable.settings_ico);
        if (aboutTab != null)
            InitTab(aboutTab, R.layout.tab_item,  R.string.aboutTab, R.drawable.about_ico);
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

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
