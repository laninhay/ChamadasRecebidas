package com.example.chamadasrecebidas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

import com.example.chamadasrecebidas.DbContract;
import com.example.chamadasrecebidas.DbHelper;

public class NumberReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            if (number != null) {
                dbHelper.saveNumber(number, database);
            }
            dbHelper.close();

            Intent intent1 = new Intent(DbContract.UPDATE_UI_FILTER);
            context.sendBroadcast(intent1);
        }
    }
}