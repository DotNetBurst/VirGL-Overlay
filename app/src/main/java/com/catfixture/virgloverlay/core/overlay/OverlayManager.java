package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.view.ViewGroup;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.overlay.TouchControlsEditorOverlayFragment;
import com.catfixture.virgloverlay.core.input.overlay.utils.EventUtils;
import com.catfixture.virgloverlay.core.input.overlay.utils.ITouchable;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.HashMap;

public class OverlayManager implements ITouchable {
    private final IWindow window;
    private final Context context;
    private HashMap<Integer, IOverlayFragment> fragmentHashMap = new HashMap<>();
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();

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

        //EVENTS
        EventUtils.InitializeITouchableEvents( window.GetContainer(), this);
        //EVENTS
    }

    public void Add(IOverlayFragment fragment) {
        AddLazy(fragment);
        fragment.Create(context);
    }

    public void Remove(IOverlayFragment fg) {
        if ( fragmentHashMap.containsKey(fg.GetID())) {
            Hide(fg);
            fragmentHashMap.remove(fg.GetID());
        } else Dbg.Error("No such key!");
    }

    public void AddLazy(IOverlayFragment fragment) {
        if ( fragmentHashMap.containsKey(fragment.GetID())) {
            throw new RuntimeException("Error duplicated ID! Check code");
        } else {
            fragmentHashMap.put(fragment.GetID(), fragment);
        }
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

    @Override
    public Event OnDown() {
        return onDown;
    }

    @Override
    public Event OnMove() {
        return onMove;
    }

    @Override
    public Event OnUp() {
        return onUp;
    }

    @Override
    public Event OnClick() {
        return onClick;
    }

    public boolean IsShown(IOverlayFragment overlayFragment) {
        if ( overlayFragment != null && overlayFragment.GetContainer() != null) {
            return window.GetContainer().indexOfChild(overlayFragment.GetContainer()) != -1;
        }
        return false;
    }
}
