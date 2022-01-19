package com.catfixture.virgloverlay.ui.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.catfixture.virgloverlay.R;
import com.catfixture.virgloverlay.core.types.delegates.Action;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.MultiGroupEntry;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.SettingItem;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.common.settingItem.MultiGroupSettingItem;
import com.catfixture.virgloverlay.ui.common.genAdapter.GenericMultiViewListAdapter;

public class GenericSettingsList {


    public static GenericMultiViewListAdapter<SettingItem> InitGenericSettingsList(Context context, RecyclerView settingsView) {
        GenericMultiViewListAdapter<SettingItem> settingsViewAdapter = new GenericMultiViewListAdapter<>(new int[] {
                R.layout.bool_setting_item,
                R.layout.text_setting_item,
                R.layout.int_setting_item,
                R.layout.int_setting_item,
                R.layout.multigroup_setting_item,
                R.layout.int_setting_item,
                R.layout.int_setting_item,
                R.layout.int_setting_item,
        }, (item, itemView) -> {
            TextView name = itemView.findViewById(R.id.name);
            name.setText(item.GetName());
            TextView description = itemView.findViewById(R.id.description);
            description.setText(item.GetDescription());

            Action<Integer> processViewType = (i) -> {
                switch (i) {
                    case Const.SETTING_DISPLAY_TYPE_BOOL: {
                        SwitchCompat handle = itemView.findViewById(R.id.handle);
                        handle.setOnCheckedChangeListener(null);
                        Boolean val = (Boolean) item.GetValue();
                        if ( val != null) handle.setChecked(val);
                        handle.setOnCheckedChangeListener((compoundButton, b) -> item.NotifyChanged(b));
                        break;
                    }
                    case Const.SETTING_DISPLAY_TYPE_TEXT: {
                        EditText handle = itemView.findViewById(R.id.handle);
                        InitDefaultTextHandle(handle, item, item::NotifyChanged);
                        break;
                    }
                    case Const.SETTING_DISPLAY_TYPE_INT: {
                        EditText handle = itemView.findViewById(R.id.handle);
                        InitDefaultTextHandle(handle, item, obj -> {
                            if (!obj.equals(""))
                                item.NotifyChanged(Integer.parseInt(obj));
                        });
                        break;
                    }
                    case Const.SETTING_DISPLAY_TYPE_MULTI_GROUP: {
                        LinearLayout multiGroupCont = itemView.findViewById(R.id.multiGroupCont);

                        MultiGroupSettingItem mgsi = (MultiGroupSettingItem) item;
                        MultiGroupEntry[] entries = mgsi.GetEntries();

                        int selectedId = -1;
                        Object val = item.GetValue();
                        if ( val != null) selectedId = (Integer)val;

                        multiGroupCont.removeAllViews();
                        for (MultiGroupEntry entry : entries) {
                            Button eBtn = new Button(multiGroupCont.getContext());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    50, 1.0f);
                            eBtn.setTextSize(10);
                            lp.setMargins(15,0,0,0);
                            eBtn.setPadding(0,0,0,0);
                            eBtn.setLayoutParams(lp);
                            eBtn.setBackgroundResource(entry.id == selectedId ? R.color.selectedRedButton : R.color.lightestGray);
                            eBtn.setText(entry.name);
                            eBtn.setOnClickListener(view -> item.NotifyChanged(entry.id));

                            multiGroupCont.addView(eBtn);
                            if (entry.id == selectedId) description.setText(entry.hint);
                        }

                        break;
                    }
                }
            };
            processViewType.Invoke(item.GetViewType());


        });
        settingsView.setLayoutManager(new LinearLayoutManager(context));
        settingsView.setAdapter(settingsViewAdapter);


        return settingsViewAdapter;
    }

    private static void InitDefaultTextHandle(EditText handle, SettingItem item, Action<String> textChanged) {
        if ( item.GetCustomData() != null)
        handle.removeTextChangedListener((TextWatcher) item.GetCustomData());

        Object val = item.GetValue();
        if ( val != null) handle.setText(val.toString());

        TextWatcher tw = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textChanged.Invoke(charSequence.toString());
            }
            @Override public void afterTextChanged(Editable editable) { }
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        };
        item.SetCustomData(tw);
        handle.addTextChangedListener(tw);
    }
}
