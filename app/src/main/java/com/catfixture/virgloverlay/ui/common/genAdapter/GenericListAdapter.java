package com.catfixture.virgloverlay.ui.common.genAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.catfixture.virgloverlay.core.types.delegates.Action2;

import java.util.ArrayList;
import java.util.List;

public class GenericListAdapter<T extends IAdapterItem> extends RecyclerView.Adapter<GenericViewHolder> {
    private final int layout;
    protected final List<T> items = new ArrayList<>();
    private final Action2<T, View> onBind;

    public GenericListAdapter(int layout, Action2<T, View> onBind) {
        this.layout = layout;
        this.onBind = onBind;
    }


    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new GenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        T currItem = items.get(position);
        if ( !currItem.IsVisible()) holder.Hide();
        else holder.Show();
        holder.SetMarginTop(currItem.GetSpacing());
        onBind.Invoke(currItem, holder.itemView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void AddItem(T i) {
        items.add(i);
        int last = items.size()-1;
        notifyItemInserted(last);
    }
    public void RemoveItem(int pos) {
        items.remove(pos);
        notifyItemRemoved(pos);
    }

    public List<T> GetItems() { return items;}

    public void Flush() {
        items.clear();
        notifyDataSetChanged();
    }
}
