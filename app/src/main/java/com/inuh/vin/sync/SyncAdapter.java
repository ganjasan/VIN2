package com.inuh.vin.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.inuh.vin.api.response.NovelResponse;

import com.inuh.vin.api.response.SourceResponse;
import com.inuh.vin.api.rest.VinRest;
import com.inuh.vin.models.Novel;

import com.inuh.vin.models.Source;
import com.inuh.vin.sqlite.NovelProvider;
import com.inuh.vin.sqlite.SQLiteContentProvider;
import com.inuh.vin.sqlite.SQLiteTableProvider;
import com.inuh.vin.sqlite.SourceProvider;
import com.inuh.vin.util.PrefManager;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Set;

import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    public static final String SYNC_FINISH_BROADCAST = "com.inuh.vin.sync.sync_finish_broadcast";

    public static final String EXTRA_SYNC_KEY = "com.inuh.vin.sync.extra_sync_key";
    public static final String EXTRA_SYNC_SOURCE_ID = "com.inuh.sync.extra_sync_source_id";

    public static final int SYNC_ALL = 1;
    public static final int SYNC_SOURCE_LIST = 2;
    public static final int SYNC_SOURCE = 3;

    private VinRest mService;

    private ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();


    SyncAdapter(Context context) {
        super(context, true);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.backendless.com/")
                .build();
        mService = restAdapter.create(VinRest.class);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider,
                              SyncResult syncResult) {


        final int syncKey = extras.getInt(EXTRA_SYNC_KEY);

        switch (syncKey) {

                case SYNC_ALL:
                    syncAll(provider);
                    break;

                case SYNC_SOURCE:
                    String sourceId = extras.getString(EXTRA_SYNC_SOURCE_ID);
                    syncSource(sourceId, provider);
                    break;

                case SYNC_SOURCE_LIST:
                    syncSourceList(provider);
                    break;

                default:
                    syncAll(provider);
        }

        getContext().sendBroadcast(new Intent(SYNC_FINISH_BROADCAST));

    }

    private void syncAll(ContentProviderClient provider){

        syncSourceList(provider);
        Set<String> sourceIdList = PrefManager.getInstance(getContext()).getSlectedSourceSet();

        for (String sourceId : sourceIdList){
            syncSource(sourceId, provider);
        }
    }

    private void syncSourceList(ContentProviderClient provider) {

        long lastUpdate = PrefManager.getInstance(getContext()).getLastUpdate();
        String whereClause = "created>" + lastUpdate + " OR " + "updated>" + lastUpdate;

        int offset = 0;
        int totalCount;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        try {
            do {
                SourceResponse response = mService.getSources(offset, whereClause);
                totalCount = response.getTotalObjects();

                for (Source source : response.getData()) {

                    if (lastUpdate < source.getCreated()) {
                        // INSERT
                        ContentValues cv = source.toContentValues();
                        ops.add(ContentProviderOperation.newInsert(SourceProvider.URI)
                                .withValues(cv)
                                .build());

                    } else if (lastUpdate < source.getUpdated()) {
                        //UPDATE
                        ContentValues cv = source.toContentValues();
                        ops.add(ContentProviderOperation.newUpdate(SourceProvider.getUriWithId(source.getObjectId()))
                                .build());
                    }

                    offset++;
                }

            } while (offset < totalCount);

            provider.applyBatch(ops);

            PrefManager.getInstance(getContext()).setCurrentDateAsLastUpdate();

        }catch (RemoteException rex){
            Log.e(TAG, "source list update error", rex);

        }catch (OperationApplicationException oaex){
            Log.e(TAG, "source list update error", oaex);

        }catch (RetrofitError retrofitError){
            Log.e(TAG, "source  list update error", retrofitError);
        }
    }

    private void syncSource(String sourceId, ContentProviderClient provider) {

        long lastUpdate = PrefManager.getInstance(getContext()).getLastSourceUpdate(sourceId);

        String whereClause = "Sources[novels].objectId = '" + sourceId + "'" +
                " AND (created>" + lastUpdate +
                " OR updated>" + lastUpdate + " )";

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        int offset = 0;
        int totalPageCount;
        try {
            do {
                NovelResponse response = mService.getNovels(offset, 50, whereClause, null);
                totalPageCount = response.getTotalObjects();


                for (Novel novel: response.getData()){
                    if(lastUpdate < novel.getCreated()){
                        //INSERT
                        novel.setSourceId(sourceId);
                        ContentValues cv = novel.toContentValues();
                        ops.add(ContentProviderOperation.newInsert(NovelProvider.URI)
                                    .withValues(cv)
                                    .build());

                    }else if(lastUpdate < novel.getUpdated()){
                        //UPDATE
                        novel.setSourceId(sourceId);
                        ContentValues cv = novel.toContentValues();
                        ops.add(ContentProviderOperation.newUpdate(NovelProvider.getUriWithId(novel.getObjectId()))
                                .withValues(cv)
                                .build());
                    }

                    offset++;
                }

            } while (offset < totalPageCount);

            provider.applyBatch(ops);

            PrefManager.getInstance(getContext()).selLastSourceUpdate(sourceId, System.currentTimeMillis());

        }catch (RemoteException rex){
            Log.e(TAG, "source list update error", rex);

        }catch (OperationApplicationException oaex){
            Log.e(TAG, "source list update error", oaex);

        }catch (RetrofitError retrofitError){
            Log.e(TAG, "source  list update error", retrofitError);
        }

    }

}
