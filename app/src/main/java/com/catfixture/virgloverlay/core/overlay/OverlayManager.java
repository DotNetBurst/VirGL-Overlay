package com.catfixture.virgloverlay.core.overlay;

import android.content.Context;
import android.view.View;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.input.utils.EventUtils;
import com.catfixture.virgloverlay.core.input.utils.ITouchable;
import com.catfixture.virgloverlay.core.utils.types.Event;
import com.catfixture.virgloverlay.core.utils.windows.AndroidWindow;
import com.catfixture.virgloverlay.core.utils.windows.IWindow;

import java.util.HashMap;

public class OverlayManager implements ITouchable {
    private IWindow window;
    private final Context context;
    private HashMap<Integer, IOverlayFragment> fragmentHashMap = new HashMap<>();
    public Event onDown = new Event();
    public Event onMove = new Event();
    public Event onUp = new Event();
    public Event onClick = new Event();

    public OverlayManager(Context context) {
        this.context = context;
    }

    public void InitializeWindow() {
        if ( window != null) return;

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
        if ( window == null) InitializeWindow();
        window.GetContainer().addView(fragment.GetContainer());

        if ( fragmentHashMap.containsKey(fragment.GetID())) {
            throw new RuntimeException("Error duplicated ID! Check code");
        } else {
            fragmentHashMap.put(fragment.GetID(), fragment);
        }
    }

    public void Remove(IOverlayFragment fg) {
        if ( fragmentHashMap.containsKey(fg.GetID())) {
            Hide(fg);
            fragmentHashMap.remove(fg.GetID());
        } else Dbg.Error("No such key!");
    }

    public IOverlayFragment GetFragment(int id) {
        if ( fragmentHashMap.containsKey(id)) {
            return fragmentHashMap.get(id);
        } else return null;
    }

    public void Show(IOverlayFragment frag) { Show(frag.GetID());}
    public void Show(int id) {
        IOverlayFragment fragment = GetFragment(id);
        fragment.GetContainer().setVisibility(View.VISIBLE);
        fragment.OnFragmentShown();
    }
    public void Hide(IOverlayFragment frag) { Hide(frag.GetID());}
    public void Hide(int id) {
        IOverlayFragment fragment = GetFragment(id);
        fragment.GetContainer().setVisibility(View.GONE);
        fragment.OnFragmentHidden();
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
        if ( overlayFragment != null) {
            return overlayFragment.GetContainer().getVisibility() == View.VISIBLE;
        }
        return false;
    }

    public void Destroy() {
        window.Detach();
        window = null;
    }
}
