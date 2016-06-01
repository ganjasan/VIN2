package com.inuh.vin.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        if(sSyncAdapter == null){
            sSyncAdapter = new SyncAdapter(getApplicationContext());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }


}
