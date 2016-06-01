package com.inuh.vin.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;
import com.inuh.vin.api.response.NovelResponse;

import com.inuh.vin.api.response.SourceResponse;
import com.inuh.vin.api.rest.VinRest;
import com.inuh.vin.models.Novel;

import com.inuh.vin.models.Source;
import com.inuh.vin.provider.TableContracts;
import com.inuh.vin.util.PrefManager;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Set;

import retrofit.RestAdapter;
import retrofit.RetrofitError;


public class SyncAdapter extends AbstractThreadedSyncAdapter {

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

        try {
            switch (syncKey) {
                case SYNC_ALL:
                    syncAll(provider);
                    break;

                case SYNC_SOURCE:
                    String sourceId = extras.getString(EXTRA_SYNC_SOURCE_ID);
                    syncSource(sourceId, provider);
                    break;

                case SYNC_SOURCE_LIST:
                    syncSourceList(provider, syncResult);
                    break;

                default:
                    syncAll(provider);
            }

            PrefManager.getInstance(getContext()).setCurrentDateAsLastUpdate();

        }catch (Exception e){
           int a = 1;
        }finally {
            getContext().sendBroadcast(new Intent(SYNC_FINISH_BROADCAST));
        }
    }

    private void syncAll(ContentProviderClient provider) throws Exception{


        long lastUpdate = PrefManager.getInstance(getContext()).getLastUpdate();

        try {

            Collection<Source> sourceList = getSources(lastUpdate);
            Set<String> selectedSource = PrefManager.getInstance(getContext()).getSlectedSourceSet();

            for (Source source : sourceList) {
                updateSource(source, lastUpdate, provider);
                if (selectedSource.contains(source)) {
                    syncSource(source.getObjectId(), provider);
                }
            }

        } catch (Exception e) {
            //Прервать синхронизацию
            throw e;
        }

    }

    private void syncSourceList(ContentProviderClient provider, SyncResult syncResult) {

        long lastUpdate = PrefManager.getInstance(getContext()).getLastUpdate();
        Collection<Source> sourceList = getSources(lastUpdate);

        try {
            for (Source source : sourceList) {
                updateSource(source, lastUpdate, provider);
            }
        } catch (Exception e) {

        }

    }

    private void syncSource(String sourceId, ContentProviderClient provider) {

        long lastUpdate = PrefManager.getInstance(getContext()).getLastUpdate();
        String whereClause = "Sources[novels].objectId = " + sourceId +
                "AND (created > " + lastUpdate +
                "OR updated > " + lastUpdate + ")";

        int offset = 0;
        int totalPageCount;
        try {
            do {
                NovelResponse response = mService.getNovels(offset, 50, whereClause, null);
                totalPageCount = response.getTotalObjects();

                for (Novel novel : response.getData()) {
                    updateNovel(novel, lastUpdate, provider);
                }
                offset += 50;

            } while (offset < totalPageCount);
        } catch (Exception e) {

        }

    }

    private Collection<Source> getSources(long lastUpdate) throws RetrofitError{

        Collection<Source> sourceList = new ArrayList<>();

        String whereClause = "created>" + lastUpdate + " OR updated>" + lastUpdate;

        int offset = 0;
        int totalPageCount;
        String nextPage;
        try {
            do {
                SourceResponse response = mService.getSources(offset, whereClause);
                totalPageCount = response.getTotalObjects();

                sourceList.addAll(response.getData());
                nextPage = response.getNextPage();

                if (nextPage == null) {
                    offset = totalPageCount;
                }else {
                    offset = Integer.parseInt(nextPage);
                }

            } while (offset < totalPageCount);

            return sourceList;
        }catch (RetrofitError error){
            throw error;
        }
    }

    private void updateSource(Source source, long lastUpdate, ContentProviderClient provider) throws RemoteException {

        if (source.getCreated() > lastUpdate) {
            // INSERT
            ContentValues cv = source.toContentValues();
            provider.insert(TableContracts.TableSource.CONTENT_URI, cv);
        } else if (source.getUpdated() > lastUpdate) {
            //UPDATE
            ContentValues cv = source.toContentValues();
            provider.update(TableContracts.TableSource.CONTENT_URI, cv, TableContracts.TableSource.OBJECT_ID,
                    new String[]{source.getObjectId()});
        }

    }

    private void updateNovel(Novel novel, long lastUpdate, ContentProviderClient provider) throws RemoteException {

        if (novel.getCreated() > lastUpdate) {
            ContentValues cv = novel.toContentValues();
            provider.insert(TableContracts.TableNovel.CONTENT_URI, cv);
        } else if (novel.getUpdated() > lastUpdate) {
            ContentValues cv = novel.toContentValues();
            provider.update(TableContracts.TableNovel.CONTENT_URI, cv, TableContracts.TableNovel.OBJECT_ID,
                    new String[]{novel.getObjectId()});
        }

    }



}
