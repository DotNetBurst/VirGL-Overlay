package com.catfixture.virgloverlay.ui.common.tabController;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.catfixture.virgloverlay.R;

public class TabsController {
    protected void InitTab(TabLayout.Tab tab, int customView, int text, int image) {
        tab.setCustomView(customView);
        View servicesTabCustomView = tab.getCustomView();
        assert servicesTabCustomView != null;

        ImageView iconView = servicesTabCustomView.findViewById(R.id.icon);
        iconView.setImageResource(image);
        TextView textView = servicesTabCustomView.findViewById(R.id.text);
        textView.setText(text);
    }
}
