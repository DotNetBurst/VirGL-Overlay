package com.catfixture.virgloverlay.data;


import static com.catfixture.virgloverlay.core.AppContext.app;

import androidx.annotation.NonNull;

import com.catfixture.virgloverlay.core.debug.Dbg;

@SuppressWarnings("unused")
public class ConfigProfile {
    public String name = "New profile";

    public boolean useSocket = true;

    public void SetUseSocket(Boolean useSocket) {this.useSocket=useSocket; Save();}

    public String socketPath;
    public void SetSocketPath(String socketPath) {this.socketPath=socketPath; Save();}

    public String ringBufferPath;
    public void SetRingBufferPath(String ringBufferPath) {this.ringBufferPath=ringBufferPath; Save();}

    public boolean useMultithreadedEGLAccess;
    public void SetUseMultithreadedEGLAccess(Boolean useMultithreadedEGLAccess) {this.useMultithreadedEGLAccess=useMultithreadedEGLAccess; Save();}

    public int eglAccessMaxThreads = 32;
    public void SetEglAccessMaxThreads(int eglAccessMaxThreads) {this.eglAccessMaxThreads=eglAccessMaxThreads; Save();}

    public boolean useImmersiveMode;
    public void SetUseImmersiveMode(Boolean useImmersiveMode) {this.useImmersiveMode=useImmersiveMode; Save();}

    public String customShrinkResolution = "100x100";
    public void SetCustomShrinkResolution(String customShrinkResolution) {
        this.customShrinkResolution=customShrinkResolution;
        String[] parts = customShrinkResolution.split("x");
        try {
            this.shrinkWidth = Integer.parseInt(parts[0]);
            this.shrinkHeight = Integer.parseInt(parts[1]);
        } catch (Exception x) {
            Dbg.Error("Wrong resolution!");
            Dbg.Error(x);
            this.customShrinkResolution = "100x100";
        }
        Save();
    }
    public int shrinkWidth = 100;
    public int shrinkHeight = 100;

    public int deviceWidth;
    public int deviceHeight;
    public int navBarHeight;


    public boolean useGLES;
    public void SetUseGLES(Boolean useGLES) { this.useGLES=useGLES; Save();}

    public boolean useS3TC;
    public void SetUseS3TC(Boolean useS3TC) { this.useS3TC=useS3TC; Save();}

    public boolean useVertexShaderHack;
    public void SetUseVertexShaderHack(Boolean useVertexShaderHack) { this.useVertexShaderHack=useVertexShaderHack; Save();}

    public boolean useBlendHack;
    public void SetUseBlendHack(Boolean useBlendHack) { this.useBlendHack=useBlendHack; Save();}

    public boolean useStencilMirror;
    public void SetUseStencilMirror(Boolean useStencilMirror) {
        this.useStencilMirror=useStencilMirror; Save();
    }

    public boolean useFragmentShaderHack;
    public void SetUseFragmentShaderHack(Boolean useFragmentShaderHack) { this.useFragmentShaderHack=useFragmentShaderHack; Save();}

    public boolean useViewportShrink;
    public void SetUseViewportShrink(Boolean useViewportShrink) {this.useViewportShrink=useViewportShrink; Save();}

    public int viewportShrinkType;
    public void SetViewportShrinkType(Integer viewportShrinkType) { this.viewportShrinkType=viewportShrinkType; Save(); }

    public boolean centerViewportRect;
    public void SetCenterViewportRect(Boolean centerViewportRect) { this.centerViewportRect=centerViewportRect; Save();}


    public boolean showControlsOnTopOfOverlay;
    public void SetShowControlsOnTopOfOverlay(Boolean showControlsOnTopOfOverlay) { this.showControlsOnTopOfOverlay=showControlsOnTopOfOverlay; Save();}

    public boolean showOverlayByProcess;
    public void SetShowOverlayByProcess(Boolean showOverlayByProcess) { this.showOverlayByProcess=showOverlayByProcess; Save();}

    public String overlayProcessName = "";
    public void SetOverlayProcessName(String overlayProcessName) { this.overlayProcessName=overlayProcessName; Save();}


    public boolean enableToasts;
    public void SetEnableToasts(Boolean enableToasts) { this.enableToasts=enableToasts; Save();}

    public boolean enableNativeInput;
    public void SetEnableNativeInput(Boolean enableNativeInput) { this.enableNativeInput=enableNativeInput; Save();}



    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
        Save();
    }
    private void Save() {

        app.SaveMainConfig();
    }
}

