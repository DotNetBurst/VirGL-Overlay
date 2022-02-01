package com.catfixture.virgloverlay.core.input.android;

import static com.catfixture.virgloverlay.core.CommonContext.comCtx;
import static com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const.*;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.catfixture.virgloverlay.core.debug.Dbg;
import com.catfixture.virgloverlay.core.utils.process.ThreadUtils;
import com.catfixture.virgloverlay.ui.activity.virgl.fragments.settings.Const;

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ( intent.getAction().equals(BCAST_ACTION_NAME)) {
            Dbg.Msg("RECVED BCAST" + intent.getAction());
            switch (intent.getIntExtra(BCAST_ACTION_TYPE, BCAST_ERR_CODE)) {
                case BCAST_ACTION_RESTART_SERVER:
                    Dbg.Msg("RECVED RESET");
                    comCtx.GetServerController().Restart();
                    break;
                case BCAST_ACTION_STOP_SERVER:
                    Dbg.Msg("RECVED HALT");
                    comCtx.GetServerController().TryStop();
                    break;

            }
        }
    }

    public static Intent PrepareMessage(Context context, int code) {
        Intent notificationIntent = new Intent(context, MessageReceiver.class);
        notificationIntent.setAction(Const.BCAST_ACTION_NAME)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(Const.BCAST_ACTION_TYPE, code);
        return notificationIntent;
    }
}
