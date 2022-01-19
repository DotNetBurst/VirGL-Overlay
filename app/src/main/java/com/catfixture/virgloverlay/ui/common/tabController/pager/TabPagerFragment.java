package com.catfixture.virgloverlay.ui.common.tabController.pager;

import androidx.fragment.app.Fragment;

public class TabPagerFragment implements IPagerFragment {
    private final String text;
    private final Fragment fragment;

    public TabPagerFragment(String text, Fragment fragment) {
        this.text = text;
        this.fragment = fragment;
    }

    @Override
    public Fragment GetFragment() {
        return fragment;
    }

    @Override
    public String GetTitle() {
        return text;
    }
}
