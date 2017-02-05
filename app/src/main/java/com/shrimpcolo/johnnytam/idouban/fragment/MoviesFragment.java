package com.shrimpcolo.johnnytam.idouban.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.ModelAdapter;
import com.shrimpcolo.johnnytam.idouban.adapter.RxModelAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;
import com.shrimpcolo.johnnytam.idouban.entity.Movies;
import com.shrimpcolo.johnnytam.idouban.net.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.net.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.utils.ToastUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

import static com.shrimpcolo.johnnytam.idouban.net.DoubanManager.createDoubanService;

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
        mProgressBar = (ProgressBar)view.findViewById(R.id.pgb_loading);
        return view;
    }

    public Observable<List<Movies>> loadRxData() {
        return createDoubanService().searchHotMovies()
                .map(hotMoviesInfo -> hotMoviesInfo.getMovies())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(() -> hideProgress());
    }

    @Override
    public void loadData() {
        //get the movies data from the douban
        createDoubanService()
                .searchHotMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotMoviesInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.e(HomeActivity.TAG, "===> movieService onCompleted !!!");
                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(HomeActivity.TAG, "===> movieService onError: Thread.Id = "
                                + Thread.currentThread().getId() + ", Error: " + e.getMessage());
                        ToastUtil.getInstance().showToast(getContext(), getResources().getString(R.string.msg_error));

                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(HotMoviesInfo hotMoviesInfo) {
                        mDataList = hotMoviesInfo.getMovies();
                        Log.e(HomeActivity.TAG, "===> movieService Response, size = " + mDataList.size());

                        mAdapter.setData(mDataList);

                    }
                });
    }

    @Override
    public void initView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
            mRecyclerView.setLayoutManager(layoutManager);

//            mAdapter = new ModelAdapter<>(mDataList, R.layout.recyclerview_movies_item);
            mAdapter = new RxModelAdapter<>(loadRxData(), R.layout.recyclerview_movies_item);

            mRecyclerView.setAdapter(mAdapter);
        }
        if(mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
