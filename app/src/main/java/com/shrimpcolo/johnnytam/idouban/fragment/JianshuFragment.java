package com.shrimpcolo.johnnytam.idouban.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class JianshuFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public JianshuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jianshu, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_blog);
        Log.e(HomeActivity.TAG, "===> onCreateView = " + this.getClass().getSimpleName());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
