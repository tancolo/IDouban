package com.shrimpcolo.johnnytam.idouban.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;


public class OnEndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private LoadMoreFilter mLoadMoreFilter;

    public OnEndlessRecyclerViewScrollListener(LoadMoreFilter loadMoreFilter){
        this.mLoadMoreFilter = loadMoreFilter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mLoadMoreFilter.loadMore(recyclerView, dy);
    }
}
