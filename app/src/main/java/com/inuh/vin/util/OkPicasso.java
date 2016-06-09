package com.inuh.vin.util;

import android.content.Context;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by artimus on 20.05.16.
 */
public class OkPicasso {

    private static Picasso picassoInstance = null;


    private OkPicasso(Context context) {

        Downloader downloader   = new OkHttpDownloader(context, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(downloader);

        picassoInstance = builder.build();
    }


    public static Picasso getPicassoInstance (Context context) {

        if (picassoInstance == null) {

            new OkPicasso(context);
            return picassoInstance;
        }

        return picassoInstance;
    }

}

