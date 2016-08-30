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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        loadBooks(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                mDataList = response.body().getBooks();

                Log.e(HomeActivity.TAG, "===>Book Response, size = " + mDataList.size());
                mAdapter.setData(mDataList);
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<BookInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===>Book onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressBar != null) {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
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
        if(mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void loadBooks(Callback<BookInfo> callback) {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchBooks("黑客与画家").enqueue(callback);
    }

}
