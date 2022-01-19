package com.catfixture.virgloverlay.ui.common.tabController.pager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<IPagerFragment> fragments = new ArrayList<>();

    public TabsFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    public void AddPagerFragment(IPagerFragment fragment) {
        fragments.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).GetTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).GetFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
