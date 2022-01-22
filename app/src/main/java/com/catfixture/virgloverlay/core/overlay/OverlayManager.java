package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.view.ViewGroup;

import com.catfixture.virgloverlay.core.App;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.HashMap;

public class OverlayManager {
    private final IWindow window;
    private final Context context;
    private HashMap<Integer, IOverlayFragment> fragmentHashMap = new HashMap<>();

    public OverlayManager(Context context) {
        this.context = context;
        window = new AndroidWindow(context);
        window.CreateRelativeLayoutContainer()
                .EnableEvents()
                .SetTranlucent()
                .SetOverlay()
                .SetPosition(0,0)
                .SetFullscreen()
                .SetAlpha(1f)
                .Attach();
    }

    public void Add(IOverlayFragment fragment) {
        fragmentHashMap.put(fragment.GetID(), fragment);
        fragment.Create(context);
    }
    public void AddLazy(IOverlayFragment fragment) {
        fragmentHashMap.put(fragment.GetID(), fragment);
    }

    public IOverlayFragment Get(int id) {
        if ( fragmentHashMap.containsKey(id)) {
            return fragmentHashMap.get(id);
        } else return null;
    }

    public void Show(IOverlayFragment frag) { Show(frag.GetID());}
    public void Show(int id) {
        IOverlayFragment fragment = Get(id);
        ViewGroup container = fragment.GetContainer();
        if ( container == null) fragment.Create(context);

        window.GetContainer().addView(fragment.GetContainer());
    }
    public void Hide(IOverlayFragment frag) { Hide(frag.GetID());}
    public void Hide(int id) {
        IOverlayFragment fragment = Get(id);
        window.GetContainer().removeView(fragment.GetContainer());
    }
}
