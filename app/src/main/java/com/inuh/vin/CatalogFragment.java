package com.inuh.vin;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuh.vin.models.Novel;
import com.inuh.vin.provider.DataProvider;
import com.inuh.vin.provider.TableContracts;
import com.inuh.vin.sqlite.NovelProvider;
import com.inuh.vin.util.CursorRecyclerViewAdapter;
import com.inuh.vin.util.PrefManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by artimus on 31.05.16.
 */
public class CatalogFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private DataProvider mDataProvider;

    private NovelRecyclerCursorAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalog_fragment_layout, container, false);

        mDataProvider = new DataProvider();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.catalog_list);
        mAdapter = new NovelRecyclerCursorAdapter(getActivity(), null);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        getActivity().getSupportLoaderManager().initLoader(0,null,this);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private class NovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Novel mNovel;
        private TextView mNameTextView;
        private ImageView mImageView;
        private ImageButton mFavoriteButton;
        private ImageButton mDownloadButton;

        public NovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.novel_image);

            mFavoriteButton = (ImageButton)itemView.findViewById(R.id.favorite_button);
            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            mDownloadButton = (ImageButton)itemView.findViewById(R.id.download_button);
            mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


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
            View view = layoutInflater.inflate(R.layout.novel_item_layout, parent, false);
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
        return new NovelCursorLoader(getContext(), mDataProvider);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class NovelCursorLoader extends CursorLoader {

        DataProvider provider;

        public NovelCursorLoader(Context context, DataProvider provider) {
            super(context);
            this.provider = provider;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = getContext().getContentResolver().query(NovelProvider.URI,null,null,null,null);
            return cursor;
        }

    }
}
