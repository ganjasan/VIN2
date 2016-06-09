package com.inuh.vin;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.inuh.vin.api.RestService;
import com.inuh.vin.api.requests.PageRequest;
import com.inuh.vin.api.response.PageResponse;
import com.inuh.vin.models.Page;
import com.inuh.vin.util.OkPicasso;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Collection;

import uk.co.senab.photoview.PhotoView;


public class PageFragment extends Fragment {

    private static final String CURRENT_PAGE = "com.inuh.vinproject.pagefragment.current_page";
    private static final String CURRENT_NOVEL_OBJECTID = "com.inuh.vinproject.pagefragment.current_novel_objectid";
    private static final String CURRENT_PAGE_NUMBER = "com.inuh.vinproject.pagefragment.current_page_number";


    private PhotoView mImageView;


    private Page mCurrentPage;
    private int     mCurrentPageNumber;
    private String  mNovelsObjectId;

    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page, container, false);

        mImageView = (PhotoView)v.findViewById(R.id.page_image);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return v;

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey(CURRENT_PAGE)) {
            mCurrentPage = (Page) getArguments().getSerializable(CURRENT_PAGE);

            String imgHref = "http://" + mCurrentPage.getImgHref();
            loadPage(imgHref);

        }else{
            mCurrentPageNumber = getArguments().getInt(CURRENT_PAGE_NUMBER);
            mNovelsObjectId = getArguments().getString(CURRENT_NOVEL_OBJECTID);
            mSpiceManager.start(getActivity());
            mSpiceManager.execute(new PageRequest(mCurrentPageNumber, 1, getWhereClause()), new PageRequestListener());
        }

    }

    private class PageRequestListener implements RequestListener<PageResponse> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(PageResponse pageResponse) {
            Collection<Page> pages = pageResponse.getData();
            if (pages.iterator().hasNext()) {
                mCurrentPage = pages.iterator().next();
                String imgHref = "http://" + mCurrentPage.getImgHref();
                loadPage(imgHref);

                mSpiceManager.shouldStop();
            }
        }
    }

    public static PageFragment getInstance(Page page){
        Bundle bundle = new Bundle();
        PageFragment pf = new PageFragment();
        bundle.putSerializable(CURRENT_PAGE, page);
        pf.setArguments(bundle);
        return pf;
    }

    public static PageFragment getInstance(String novelsObjectId, int pageNumber){
        Bundle bundle = new Bundle();
        PageFragment pf = new PageFragment();
        bundle.putString(CURRENT_NOVEL_OBJECTID, novelsObjectId);
        bundle.putInt(CURRENT_PAGE_NUMBER, pageNumber);
        pf.setArguments(bundle);
        return pf;
    }

    private String getWhereClause(){
        String selectNovelWhereClause = "Novels[pages].objectId='" + mNovelsObjectId +"'";

        return selectNovelWhereClause;
    }

    private void loadPage(String imgHref) {
        OkPicasso.getPicassoInstance(getActivity())
                .load(imgHref)
                .into(mImageView);
    }

}
