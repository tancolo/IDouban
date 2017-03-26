package com.shrimpcolo.johnnytam.idouban.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;

/**
 * Created by Johnny Tam on 2017/3/26.
 */

public abstract class LoadMoreFilter {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 2;//default is 5
    // The current offset index of data you have loaded
    private int currentPage = 0;
    private int mTotalItemCount = 0;

    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    // Sets the  footerViewType
    private int defaultNoFooterViewType = -1;
    private int footerViewType = -1;

    private String mTag = HomeActivity.TAG;

    RecyclerView.LayoutManager mLayoutManager;

    public LoadMoreFilter(RecyclerView.LayoutManager layoutManager) {
        init();
        this.mLayoutManager = layoutManager;
        if (layoutManager == null) return;

        int spanCount = 1;
        if (layoutManager instanceof GridLayoutManager){
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if(layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }

        visibleThreshold = visibleThreshold * spanCount;
    }

    //init from  self-define
    private void init() {
        footerViewType = getFooterViewType(defaultNoFooterViewType);
        startingPageIndex = getStartingPageIndex();

        int threshold = getVisibleThreshold();
        if (threshold > visibleThreshold) {
            visibleThreshold = threshold;
        }
    }


    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
//    @Override
//    public void onScrolled(final RecyclerView view, int dx, int dy) {
//        loadMore(view, dy);
//    }

    public void loadMore(RecyclerView view, int dy) {
        if (isEndOfList(view, dy)) onLoadMore(currentPage, mTotalItemCount);
    }

    public int getTotalItemCount() {
        return mTotalItemCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isEndOfList(RecyclerView view, int dy) {
        if (dy <= 0) return false;

        RecyclerView.Adapter adapter = view.getAdapter();
        mTotalItemCount = adapter.getItemCount();

        int lastVisibleItemPosition = getLastVisibleItemPosition();

        boolean isAllowLoadMore = (lastVisibleItemPosition + visibleThreshold) > mTotalItemCount;

        if (!isAllowLoadMore) return false;

        if (isUseFooterView()) {
            if (!isFooterView(adapter)) {

                if (mTotalItemCount < previousTotalItemCount) {
                    this.currentPage = this.startingPageIndex;
                } else if (mTotalItemCount == previousTotalItemCount) {
                    currentPage = currentPage == startingPageIndex ? startingPageIndex : --currentPage;
                }

                if(!isLoading())
                    loading = false;
            }
        } else {
            if (mTotalItemCount > previousTotalItemCount) loading = false;
        }

        if (loading) return false;

        previousTotalItemCount = mTotalItemCount;
        currentPage++;
        loading = true;

        return true;
    }

//    private boolean isEndOfList(RecyclerView view, int dy) {
//
//    }

//    @Override
//    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//        super.onScrollStateChanged(recyclerView, newState);
//
//    }


    public boolean isUseFooterView() {
        boolean isUse = footerViewType != defaultNoFooterViewType;
//        Log.i(mTag, "isUseFooterView:" + isUse);
        return isUse;
    }


    public boolean isFooterView(RecyclerView.Adapter adapter) {

        boolean isFooterView = false;
        int totalItemCount = adapter.getItemCount();

        int lastPosition = -1;
        int lastViewType = -1;

        if (totalItemCount > 0) {

            lastPosition = totalItemCount - 1;
            lastViewType = adapter.getItemViewType(lastPosition);

            //  check the last view is foot view
            isFooterView = lastViewType == footerViewType;
        }
        Log.i(mTag, "===> totalItemCount: " + totalItemCount
                + ", lastPosition: " + lastPosition
                + ", lastViewType: " + lastViewType
                + ", isFooterView:" + isFooterView);

        return isFooterView;
    }

    private int getLastVisibleItemPosition() {
        int lastVisibleItemPosition = 0;

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);

        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        return lastVisibleItemPosition;
    }


    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }


    // set FooterView type
    // if don't use footview loadmore  default: -1
    public abstract int getFooterViewType(int defaultNoFooterViewType);

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);

    public abstract boolean isLoading();//Loading & Loaded

    //set visibleThreshold   default: 5
    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    //set startingPageIndex   default: 0
    public int getStartingPageIndex() {
        return startingPageIndex;
    }

}
