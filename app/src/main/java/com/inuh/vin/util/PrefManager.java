package com.inuh.vin.util;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.inuh.vin.models.Source;
import com.inuh.vin.sync.SyncAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by artimus on 30.05.16.
 */
public class PrefManager {

    public final static String PREF_LAST_UPDATE_DATE = "com.inuh.vin.prefmanager.pref_last_update_date";
    public final static String PREF_SELECTED_SOURCE = "com.inuh.vin.prefmanager.pref_selected_source";
    public final static String PREF_CHANGE_NOTIFICATION = "com.inuh.vin.prefmanager.pref_change_notification";

    private static PrefManager sInstance;

    private Context mContext;

    public static PrefManager getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new PrefManager(context);
        }

        return sInstance;

    }

    private PrefManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void setLastUpdate(long lastUpdate){
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(PREF_LAST_UPDATE_DATE, lastUpdate)
                .commit();
    }

    public void setCurrentDateAsLastUpdate(){
        long date = System.currentTimeMillis();

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putLong(PREF_LAST_UPDATE_DATE, date)
                .commit();
    }

    public long getLastUpdate(){
        long lastUpdate = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getLong(PREF_LAST_UPDATE_DATE, 0);

        return lastUpdate;
    }

    public void setSourceSelected(Source source, boolean select){

        if(select) {
            Set<String> selectedSources = PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getStringSet(PREF_SELECTED_SOURCE, new HashSet<String>());

            selectedSources.add(source.getObjectId());
            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putStringSet(PREF_SELECTED_SOURCE, selectedSources)
                    .commit();

            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putBoolean(source.getObjectId(), true)
                    .commit();

        }else{
            Set<String> selectedSources = PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getStringSet(PREF_SELECTED_SOURCE, new HashSet<String>());

            if(selectedSources.contains(source.getObjectId())){
                selectedSources.remove(source.getObjectId());
            }

            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putStringSet(PREF_SELECTED_SOURCE, selectedSources)
                    .commit();

            PreferenceManager.getDefaultSharedPreferences(mContext)
                    .edit()
                    .putBoolean(source.getObjectId(), false)
                    .commit();

        }

        mContext.sendBroadcast(new Intent(PREF_CHANGE_NOTIFICATION));
    }

    public String getSelectedSource(){

        Set<String> selectedSources = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getStringSet(PREF_SELECTED_SOURCE, new HashSet<String>());

        StringBuilder stringBuilder = new StringBuilder();
        for (String sourceId: selectedSources) {
            stringBuilder.append("'" + sourceId +"',");
        }
        if(stringBuilder.length() != 0){
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    public Set<String> getSlectedSourceSet(){
        return PreferenceManager.getDefaultSharedPreferences(mContext).getStringSet(PREF_SELECTED_SOURCE, new HashSet<String>());
    }

}
