package com.catfixture.virgloverlay.data;

import com.catfixture.virgloverlay.core.android.AndroidUtils;
import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.objProvider.ITypedProvider;
import com.catfixture.virgloverlay.core.types.Event;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenericConfig<T extends ITypedProvider<Event>> {
    private final String path;
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private T config;

    public GenericConfig(String path, Class<T> clazz) {
        this.path = path;
        this.clazz = clazz;
        Load(path);
    }

    private void Load(String path) {
        //LOAD
        try {
            BufferedReader fw = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String lineBuff;
            while((lineBuff = fw.readLine())!=null)
                sb.append(lineBuff);
            fw.close();

            String json = sb.toString();
            config = gson.fromJson(json, clazz);

            config.get().addObserver((observable, o) -> Save());
        } catch (IOException e) {
            Dbg.Error(e);
            Save();
        }
    }
    public void Save() {
        //SAVE
        if (config == null) {
            try {
                config = clazz.newInstance();
                config.get().addObserver((observable, o) -> Save());
            } catch (IllegalAccessException | InstantiationException e) {
                Dbg.Error(e);
            }
        }

        String jsonRes = gson.toJson(config);
        AndroidUtils.WriteFile(path, jsonRes);
    }

    public T GetData() {
        return config;
    }
}
