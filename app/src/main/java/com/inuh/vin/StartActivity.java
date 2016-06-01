package com.inuh.vin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.inuh.vin.provider.TableContracts;
import com.inuh.vin.sqlite.SQLiteTableProvider;
import com.inuh.vin.sync.SyncAdapter;
import com.inuh.vin.util.PrefManager;

import retrofit.RestAdapter;


public class StartActivity extends Activity {

    public static final String AUTHORITY = TableContracts.AUTHORITY;
    public static final String ACCOUNT_TYPE = "com.inuh.vin.account";
    public static final String ACCOUNT = "base_account";

    public static final int SOURCE_SELECT_REQUEST_CODE = 1;

    Account mAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAccount = createSyncAccount(getApplicationContext());

        if (PrefManager.getInstance(this).getSlectedSourceSet().isEmpty()) {
            Intent intent = new Intent(this, SelectSourceActivity.class);
            startActivityForResult(intent, SOURCE_SELECT_REQUEST_CODE);
        }else {

            if (ContentResolver.getSyncAutomatically(mAccount, SQLiteTableProvider.AUTHORITY) == false ||
                    ContentResolver.getMasterSyncAutomatically() == false) {

                Intent catalogActivityStartIntent = new Intent(StartActivity.this, CatalogActivity.class);
                startActivity(catalogActivityStartIntent);
                finish();
            } else {

                long lastUpdate = PrefManager.getInstance(this).getLastUpdate();


                final Bundle syncExtras = new Bundle();
                syncExtras.putInt(SyncAdapter.EXTRA_SYNC_KEY, SyncAdapter.SYNC_ALL);

                ContentResolver.requestSync(mAccount, AUTHORITY, syncExtras);

            }
        }

        setContentView(R.layout.activity_start_layout);

    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(SyncAdapter.SYNC_FINISH_BROADCAST);
        registerReceiver(mOnSyncFinish, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnSyncFinish);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final  Bundle syncExtras = new Bundle();
        syncExtras.putInt(SyncAdapter.EXTRA_SYNC_KEY, SyncAdapter.SYNC_ALL);

        ContentResolver.requestSync(mAccount,AUTHORITY, syncExtras);
    }

    public static Account createSyncAccount(Context context){

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        accountManager.addAccountExplicitly(newAccount, null, null);

        return newAccount;

    }

    private BroadcastReceiver mOnSyncFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent catalogActivityStartIntent = new Intent(StartActivity.this, CatalogActivity.class);
            startActivity(catalogActivityStartIntent);
            finish();
        }
    };

}
