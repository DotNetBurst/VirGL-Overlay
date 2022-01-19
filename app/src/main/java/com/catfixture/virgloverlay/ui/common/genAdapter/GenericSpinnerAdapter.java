package com.catfixture.virgloverlay.ui.common.genAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import com.catfixture.virgloverlay.core.types.delegates.Action2;

import java.util.List;

public class GenericSpinnerAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private final int textViewResourceId;
    private final Action<Integer> onRemove;
    private Action2<TextView, Integer> customTitleAction;


    private enum DisplayType {
        Normal, Dropdown
    }

    public GenericSpinnerAdapter(@NonNull Context context, int resource, List<T> items, Action<Integer> onRemove) {
        super(context, R.layout.support_simple_spinner_dropdown_item, items);
        this.context = context;
        this.textViewResourceId = resource;
        this.onRemove = onRemove;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return prepareView(position, convertView, parent, DisplayType.Normal);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return prepareView(position, convertView, parent, DisplayType.Dropdown);
    }

    public void EnableCustomTitleAction(Action2<TextView, Integer> customTitleAction) {
        this.customTitleAction = customTitleAction;
    }

    private View prepareView (int position, @Nullable View convertView, @NonNull ViewGroup parent, DisplayType dType) {
        if (convertView == null) {
            convertView = View.inflate(context, textViewResourceId, null);
        }

        TextView tv = convertView.findViewById(R.id.text);
        tv.setText(getItem(position).toString());


        ImageView removeBtn = convertView.findViewById(R.id.removeBtn);
        if ( dType == DisplayType.Dropdown) {
            if (customTitleAction != null) customTitleAction.Invoke(tv, position);
            removeBtn.setOnClickListener(view -> {
                if (onRemove != null) onRemove.Invoke(position);
                notifyDataSetChanged();
            });
        } else {
            tv.setTypeface(null, Typeface.BOLD);
            removeBtn.setVisibility(View.GONE);
        }

        return convertView;
    }
}
