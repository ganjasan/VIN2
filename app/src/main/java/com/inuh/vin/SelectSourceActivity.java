package com.inuh.vin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vin.api.RestService;
import com.inuh.vin.api.requests.SourceRequest;
import com.inuh.vin.api.response.SourceResponse;
import com.inuh.vin.models.Source;
import com.inuh.vin.sqlite.SQLiteTableProvider;
import com.inuh.vin.sync.SyncAdapter;
import com.inuh.vin.util.PrefManager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Collection;

public class SelectSourceActivity extends AppCompatActivity {

    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    private RecyclerView mRecyclerView;
    private SourceAdapter mAdapter;
    private SourceRequest mSourceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_source);

        setSyncAutomatically();

        mRecyclerView = (RecyclerView) findViewById(R.id.source_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSourceRequest = new SourceRequest(0, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSource = PrefManager.getInstance(getApplicationContext()).getSelectedSource();
                if (selectedSource.equals("")){
                    Toast.makeText(SelectSourceActivity.this, R.string.text_no_selected_sources, Toast.LENGTH_SHORT);
                }else{
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        super.onStart();
        mSpiceManager.execute(mSourceRequest, new ListSourceRequestListener());
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }


    public final class ListSourceRequestListener implements RequestListener<SourceResponse> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SelectSourceActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SourceResponse sources) {

            Collection<Source> sourceList = sources.getData();
            mAdapter = new SourceAdapter(sourceList);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    private class SourceHolder extends RecyclerView.ViewHolder {

        private Source mSource;

        private TextView mTitleTextView;
        private CheckBox mSelectChecbox;


        public SourceHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.source_name);
            mSelectChecbox = (CheckBox) itemView.findViewById(R.id.source_select_checbox);
            mSelectChecbox.setOnClickListener(new CheckBoxClickListener());
        }

        public void bindSource(Source source){
            mSource = source;
            mTitleTextView.setText(source.getName());
            mSelectChecbox.setChecked(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                    getBoolean(source.getObjectId(), false));
        }

        private class CheckBoxClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v) {
                PrefManager.getInstance(getApplicationContext()).setSourceSelected(mSource, mSelectChecbox.isChecked());
            }
        }

    }

    private class SourceAdapter extends RecyclerView.Adapter<SourceHolder>{

        private ArrayList<Source> mSources;

        public SourceAdapter(Collection<Source> sourceList){
            mSources = new ArrayList<Source>(sourceList);
        }

        @Override
        public void onBindViewHolder(SourceHolder holder, int position) {

            Source source = mSources.get(position);
            holder.bindSource(source);
        }

        @Override
        public SourceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SelectSourceActivity.this);
            View view = layoutInflater.inflate(R.layout.source_item_layout, parent, false);
            return new SourceHolder(view);
        }

        @Override
        public int getItemCount() {
            return mSources.size();
        }
    }

    private void setSyncAutomatically(){
        AccountManager am = AccountManager.get(this);
        Account account = am.getAccountsByType("com.inuh.vin.account")[0];
        getContentResolver().setSyncAutomatically(account, SQLiteTableProvider.AUTHORITY, true);
    }



}
