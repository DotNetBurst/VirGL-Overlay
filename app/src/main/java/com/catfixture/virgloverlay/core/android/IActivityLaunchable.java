package com.catfixture.virgloverlay.core.android;

import android.content.Intent;

import androidx.activity.result.ActivityResult;

import com.catfixture.virgloverlay.core.types.delegates.Action;

public interface IActivityLaunchable {
    void Launch(Intent intent, Action<ActivityResult> result);
}
