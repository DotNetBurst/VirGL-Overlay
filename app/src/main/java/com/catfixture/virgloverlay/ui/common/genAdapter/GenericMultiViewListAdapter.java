package com.catfixture.virgloverlay.ui.common.genAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.core.types.delegates.Action2;

public class GenericMultiViewListAdapter<T extends IMultiViewAdapterItem> extends GenericListAdapter<T> {
    private final int[] layouts;

    public GenericMultiViewListAdapter(int[] layouts, Action2<T, View> onBind) {
        super(layouts[0], onBind);
        this.layouts = layouts;
    }



    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int lay = layouts[viewType];
        View view = LayoutInflater.from(parent.getContext()).inflate(lay, parent, false);

        return new GenericViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).GetViewType();
    }
}
