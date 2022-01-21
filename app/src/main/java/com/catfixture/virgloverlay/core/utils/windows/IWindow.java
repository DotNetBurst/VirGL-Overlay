package com.catfixture.virgloverlay.core.utils.windows;

import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public interface IWindow {
    IWindow EnableEvents();
    IWindow SetOverlay();
    IWindow SetVisibility (boolean vis);

    IWindow Attach();
    IWindow Detach();


    View GetView ();
    ViewGroup GetContainer ();
    IWindow SetContainer(ViewGroup container);

    IWindow SetFullscreen();
    IWindow SetShirkWidthOnBottomSide();
    IWindow SetShirkWidthOnTopSide();
    IWindow SetShirkHeightOnLeftSide();
    IWindow SetShirkHeightOnRightSide();

    IWindow CreateSurfaceViewContainer();
    IWindow CreateLinearLayoutContainer();
    IWindow CreateRelativeLayoutContainer();
    IWindow SetTranlucent();


    IWindow SetPosition (int x, int y);
    IWindow SetAlpha (float a);
    IWindow SetSize (int width, int height);

    IWindow SetSizeByContainer();
    IWindow SetHeightByContainer();
}
