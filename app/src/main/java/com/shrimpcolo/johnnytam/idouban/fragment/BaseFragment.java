package com.shrimpcolo.johnnytam.idouban.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.BaseAdapter;
import com.shrimpcolo.johnnytam.idouban.adapter.ModelAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Johnny Tam on 2016/7/30.
 */
public abstract class BaseFragment<T> extends Fragment{

    protected List<T> mDataList = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected ModelAdapter<T> mAdapter;

    protected ProgressBar mProgressBar;

    public BaseFragment() {
        // Required empty public constructor
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(HomeActivity.TAG, "===> onAttach: " + context.getClass().getName());

        //loadData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    public void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public abstract void loadData();
    public abstract void initView();
}