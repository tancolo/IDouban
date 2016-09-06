package com.shrimpcolo.johnnytam.idouban.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.ModelAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.entity.BookInfo;
import com.shrimpcolo.johnnytam.idouban.net.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.net.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.utils.ToastUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends BaseFragment<Book> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_books);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pgb_loading_book);
//        Log.e(HomeActivity.TAG, "===> mRecyclerView = " + mRecyclerView);
        return view;
    }

    @Override
    public void loadData() {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchBooks(getString(R.string.serach_book))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BookInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.e(HomeActivity.TAG, "===>Book onCompleted!!!");

                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(HomeActivity.TAG, "===>Book onFailure, Error: " + e.getMessage());
                        ToastUtil.getInstance().showToast(getContext(), getResources().getString(R.string.msg_error));

                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(BookInfo bookInfo) {
                        mDataList = bookInfo.getBooks();
                        Log.e(HomeActivity.TAG, "===>Book Response, size = " + mDataList.size());
                        mAdapter.setData(mDataList);
                    }
                });
    }

    @Override
    public void initView() {
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);

            mAdapter = new ModelAdapter<>(mDataList, R.layout.recyclerview_book_item);

            mRecyclerView.setAdapter(mAdapter);
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

}
