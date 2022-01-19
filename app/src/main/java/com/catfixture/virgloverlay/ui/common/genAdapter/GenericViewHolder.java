package com.catfixture.virgloverlay.ui.common.genAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenericViewHolder extends RecyclerView.ViewHolder {
    private final ViewGroup.LayoutParams shownParams;
    private final ViewGroup.LayoutParams hiddenParams;

    public GenericViewHolder(@NonNull View itemView) {
        super(itemView);
        shownParams = itemView.getLayoutParams();
        hiddenParams = new LinearLayout.LayoutParams(0,0);
    }

    public void Show() {
        itemView.setLayoutParams(shownParams);
    }
    public void Hide() {
        itemView.setLayoutParams(hiddenParams);
    }

    public void SetMarginTop(int getSpacing) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemView.getLayoutParams().width,
                itemView.getLayoutParams().height);
        lp.setMargins(0,getSpacing,0,0);
        itemView.setLayoutParams(lp);
    }
}
