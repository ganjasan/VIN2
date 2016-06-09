package com.inuh.vin;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuh.vin.models.Novel;
import com.inuh.vin.sqlite.NovelProvider;
import com.inuh.vin.sqlite.SQLiteContentProvider;
import com.inuh.vin.sync.SyncAdapter;
import com.inuh.vin.util.CursorRecyclerViewAdapter;
import com.inuh.vin.util.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by artimus on 31.05.16.
 */
public class CatalogFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_CATALOG_FRAGMENT = "com.inuh.vin.extra_catalog_fragment";
    public static final String EXTRA_DOWNLOAD_FRAGMENT = "com.inuh.vin.extra_download_fragment";
    public static final String EXTRA_FAVORITE_FRAGMENT = "com.inuh.vin.extra_favorite_fragment";

    private NovelRecyclerCursorAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mSelection;
    private String[] mSelectionArgs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalog_fragment_layout, container, false);

        setSelection();


        mRecyclerView = (RecyclerView)view.findViewById(R.id.catalog_list);

        mAdapter = new NovelRecyclerCursorAdapter(getContext(), null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        getActivity().getSupportLoaderManager().initLoader(0, null,this);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class NovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Novel mNovel;
        private TextView mNameTextView;
        private ImageView mImageView;
        private ImageButton mActionButton;

        public NovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.novel_image);

            mActionButton = (ImageButton) itemView.findViewById(R.id.novel_action_button);

            mActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity().getLocalClassName() == "DownloadActivity"){
                        //delete action
                    }else if(!mNovel.isFavorite()){
                        mNovel.setIsFavorite(true);
                        mActionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.download));
                        updateNovel(mNovel);
                    }else if(mNovel.isFavorite() && !mNovel.isDownloaded()){
                        mNovel.setIsDownloaded(true);
                        updateNovel(mNovel);
                        mActionButton.setVisibility(View.INVISIBLE);
                    }

                }
            });

        }

        public void bindHolder(Novel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());
            Picasso.with(getActivity())
                    .load(novel.getImgHref())
                    .placeholder(mImageView.getDrawable())
                    .into(mImageView);

            mActionButton.setVisibility(View.VISIBLE);

            if (getActivity().getLocalClassName().equals("DownloadActivity")){
                mActionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.close));

            }else if(!mNovel.isFavorite()){
                mActionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.star_outline));

            }else if(mNovel.isFavorite() && !mNovel.isDownloaded()){
                mActionButton.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.download));

            }else if(mNovel.isFavorite() && mNovel.isDownloaded()){
                mActionButton.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ReaderActivity.class);
            intent.putExtra(ReaderActivity.EXTRA_NOVELS_OBJECTID, mNovel.getObjectId());
            intent.putExtra(ReaderActivity.EXTRA_TOTAL_PAGE_COUNT, mNovel.getPageTotal());
            startActivity(intent);
        }
    }

    private class NovelRecyclerCursorAdapter extends CursorRecyclerViewAdapter<NovelHolder>{

        public NovelRecyclerCursorAdapter(Context context, Cursor cursor){
            super(context, cursor);
        }

        @Override
        public NovelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.novel_card_layout, parent, false);
            return new NovelHolder(view);
        }

        @Override
        public void onBindViewHolder(NovelHolder holder, Cursor cursor) {
            Novel novel =  Novel.fromCursor(cursor);
            holder.bindHolder(novel);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(), NovelProvider.URI, null, mSelection, mSelectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    private void updateNovel(Novel novel){
        ContentValues cv = novel.toContentValues();
        Uri uri = NovelProvider.getUriWithId(novel.getObjectId());
        getContext().getContentResolver().update(uri, cv, null, null);
    }

    private void setSelection(){

        String activityName = getActivity().getLocalClassName();

        switch (activityName){
            case "CatalogActivity":
                mSelection = null;
                mSelectionArgs = null;
                break;

            case "DownloadActivity":
                mSelection = NovelProvider.Columns.IS_DOWNLOADED + "=?";
                mSelectionArgs = new String[]{"1"};

                break;

            case "FavoritesActivity":

                mSelection = NovelProvider.Columns.IS_FAVORITE + "=?";
                mSelectionArgs = new String[]{"1"};

                break;

            default:
                mSelection = null;
                mSelectionArgs = null;

                break;
        }

    }
}
