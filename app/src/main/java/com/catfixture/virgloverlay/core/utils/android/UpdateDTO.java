package com.catfixture.virgloverlay.core.utils.android;

public class UpdateDTO {
    public final int vcode;
    public final String vname;
    public final String changelog;

    public UpdateDTO(int vcode, String vname, String changelog) {
        this.vcode = vcode;
        this.vname = vname;
        this.changelog = changelog;
    }
}
