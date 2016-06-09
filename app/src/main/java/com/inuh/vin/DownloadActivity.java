package com.inuh.vin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by artimus on 31.05.16.
 */
public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.download_fragment_container);

        if(fragment == null){
            fragment = new CatalogFragment();
            fm.beginTransaction()
                    .add(R.id.download_fragment_container, fragment)
                    .commit();
        }

    }

}
