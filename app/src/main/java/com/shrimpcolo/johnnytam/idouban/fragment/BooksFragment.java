package com.shrimpcolo.johnnytam.idouban.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.entity.BookInfo;
import com.shrimpcolo.johnnytam.idouban.net.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.net.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.adapter.BaseAdapter;
import com.shrimpcolo.johnnytam.idouban.adapter.ModelAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {
    //private String mBookName = "黑客与画家";
    private List<Book> mBookList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BaseAdapter mBookAdapter;

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(HomeActivity.TAG, "===>Book onAttach:  " + context.getClass().getName());

        loadBooks(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                mBookList = response.body().getBooks();

                Log.e(HomeActivity.TAG, "===>Book Response, size = " + mBookList.size());
                mBookAdapter.setData(mBookList);
            }

            @Override
            public void onFailure(Call<BookInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===>Book onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_books);
        Log.e(HomeActivity.TAG, "===> mRecyclerView = " + mRecyclerView);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);

            mBookAdapter = new ModelAdapter<>(mBookList, R.layout.recyclerview_book_item);

            mRecyclerView.setAdapter(mBookAdapter);
        }
    }

    private void loadBooks(Callback<BookInfo> callback) {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchBooks("黑客与画家").enqueue(callback);
    }

}
