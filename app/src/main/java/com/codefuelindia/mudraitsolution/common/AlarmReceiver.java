package com.codefuelindia.mudraitsolution.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.codefuelindia.mudraitsolution.cons.Constants;


public class AlarmReceiver extends BroadcastReceiver {

        public AlarmReceiver() {


        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("triggered","alarm.....fast");

            SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_PREF, Context.MODE_PRIVATE).edit();
            editor.putString(Constants.REG_IN_PROGRESS, "");
            editor.putString("ok", "");
            editor.apply();

            // Your code to execute when the alarm triggers
            // and the broadcast is received.

        }
    }
