package com.catfixture.virgloverlay.core.android;

public class UpdateInfo {
    public final String versionName;
    public final int versionCode;
    public final String changelog;

    public UpdateInfo(String versionName, int versionCode, String changelog) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.changelog = changelog;
    }
}
