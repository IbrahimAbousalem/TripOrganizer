package com.iti.mobile.triporganizer.alarm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

           // add all the alarms after rebooting so that the alarms survives the reboot process
        }else {
            context.startService(new Intent(context,AlarmService.class));
            context.startActivity(new Intent(context, SetAlarmDummyActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK));
        }

    }



}
