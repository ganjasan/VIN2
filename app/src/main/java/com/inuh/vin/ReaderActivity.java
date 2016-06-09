package com.inuh.vin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.inuh.vin.api.RestService;
import com.inuh.vin.api.requests.PageRequest;
import com.inuh.vin.api.response.PageResponse;
import com.inuh.vin.models.Page;
import com.inuh.vin.util.OkPicasso;
import com.inuh.vin.util.PrefManager;
import com.inuh.vin.view.FixViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.LinkedList;

public class ReaderActivity extends AppCompatActivity {

    public static final String  EXTRA_NOVELS_OBJECTID = "com.inuh.vinproject.readeractivity.extra_novels_objectid";
    public static final String  EXTRA_TOTAL_PAGE_COUNT = "com.inuh.vinproject.readeractivity.extra_total_page_count";

    public static final String  EXTRA_CURRENT_PAGE = "com.inuh.vinpoject.readeractivity.extra_current_page";
    public static final String  EXTRA_CURRENT_PAGE_LIST = "com.inuh.vinpoject.readeractivity.extra_current_page_list";

    private static final int    OFFSCREEN_PAGE_LIMIT = 1;
    private static final int    PRELOAD_PAGE_COUNT = 15;

    private FixViewPager mViewPager;
    private SpiceManager        mSpiceManager = new SpiceManager(RestService.class);

    private String              mNovelsObjectId;
    private int                 mCurrentPageNumber;
    private LinkedList<Page>    mPages;
    private String              mWhereClause;
    private int                 mTotalPageCount;
    private int                 mAdapterOffset;

    private boolean             isFullScreanMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNovelsObjectId = getIntent().getStringExtra(EXTRA_NOVELS_OBJECTID);
        mTotalPageCount = getIntent().getIntExtra(EXTRA_TOTAL_PAGE_COUNT, 0);
        mCurrentPageNumber = PrefManager.getInstance(this).getNovelsLastPage(mNovelsObjectId);
        mPages = new LinkedList<>();
        mWhereClause = getWhereClause();

        isFullScreanMode = false;


        mViewPager = new FixViewPager(this);
        mViewPager.setId(R.id.view_pager);


        mViewPager.setAdapter(new ReaderPageAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPageNumber = position+1;
            }
        });

        setContentView(mViewPager);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.setCurrentItem(mCurrentPageNumber - 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        int offset = mCurrentPageNumber == 1? 0 : mCurrentPageNumber-2;
        mSpiceManager.execute(new PageRequest(offset, 16, mWhereClause), new NextPageRequestListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
        PrefManager.getInstance(this).setNovelsLastPage(mCurrentPageNumber, mNovelsObjectId);
    }



    private class NextPageRequestListener implements RequestListener<PageResponse> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(ReaderActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(PageResponse pageResponse) {
            mTotalPageCount = pageResponse.getTotalObjects();
            mPages.addAll(pageResponse.getData());
            mAdapterOffset = mPages.getFirst().getNumber();

            mViewPager.getAdapter().notifyDataSetChanged();

            imagePreCache(pageResponse.getData());
        }
    }

    private class ReaderPageAdapter extends FragmentStatePagerAdapter{

        public ReaderPageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PageFragment pf;
            if(mPages.isEmpty() || position < mPages.getFirst().getNumber()-1 || position > mPages.getLast().getNumber()-1){
                pf = PageFragment.getInstance(mNovelsObjectId, position);
            }else {
                if (position == mPages.getLast().getNumber() - 6 || position > mPages.getLast().getNumber()-1)
                {
                    mSpiceManager.execute(new PageRequest(mPages.getLast().getNumber(), mWhereClause),new NextPageRequestListener());
                }
                Page currentPage = mPages.get(position - (mAdapterOffset-1));
                pf = PageFragment.getInstance(currentPage);
            }
            return pf;
        }

        @Override
        public int getCount() {
            return mTotalPageCount;
        }

    }

    private void imagePreCache(Collection<Page> pages){
        for (Page page: pages) {
            String imgHref = "http://"  + page.getImgHref();
            OkPicasso.getPicassoInstance(getApplicationContext())
                    .load(imgHref)
                    .priority(Picasso.Priority.LOW)
                    .fetch();
        }
    }


    private String getWhereClause(int firstPageNum, int pageCount){

        String selectNovelWhereClause = "Novels[pages].objectId='" + mNovelsObjectId +"'";
        String pageBetweenWhereClause = "number>=" + firstPageNum + " AND number<=" + (firstPageNum+pageCount);

        String whereClause = selectNovelWhereClause + "&" + pageBetweenWhereClause;

        return  whereClause;
    }

    private String getWhereClause(){
        String selectNovelWhereClause = "Novels[pages].objectId='" + mNovelsObjectId +"'";

        return selectNovelWhereClause;
    }

    private void loadNext(){

    }
}

