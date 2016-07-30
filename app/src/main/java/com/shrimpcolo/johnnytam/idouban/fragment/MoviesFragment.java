package com.shrimpcolo.johnnytam.idouban.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.ModelAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;
import com.shrimpcolo.johnnytam.idouban.entity.Movies;
import com.shrimpcolo.johnnytam.idouban.net.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.net.IDoubanService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends BaseFragment<Movies> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_hot_movies);
        return view;
    }

    private void loadMovies(Callback<HotMoviesInfo> callback) {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchHotMovies().enqueue(callback);
    }

    @Override
    public void loadData() {
        //get the movies data from the douban
        loadMovies(new Callback<HotMoviesInfo>() {
            @Override
            public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
                Log.d(HomeActivity.TAG, "===> onResponse: Thread.Id = " + Thread.currentThread().getId());
                mDataList = response.body().getMovies();

                //debug
                Log.e(HomeActivity.TAG, "===> Response, size = " + mDataList.size());
                mAdapter.setData(mDataList);
            }

            @Override
            public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void initView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
            mRecyclerView.setLayoutManager(layoutManager);

            mAdapter = new ModelAdapter<>(mDataList, R.layout.recyclerview_movies_item);

            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
