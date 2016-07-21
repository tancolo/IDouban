package com.shrimpcolo.johnnytam.idouban.movie;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.api.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.api.IDoubanService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private List<Movies> mMoviesList = new ArrayList<>();

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(HomeActivity.TAG, "===> onAttach: " + context.getClass().getName());

        //get the movies data from the douban
        loadMovies(new Callback<HotMoviesInfo>() {
            @Override
            public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
                Log.d(HomeActivity.TAG, "===> onResponse: Thread.Id = " + Thread.currentThread().getId());
                mMoviesList = response.body().getMovies();

                //debug
                Log.e(HomeActivity.TAG, "===> Response, size = " + mMoviesList.size());
                for (Movies movies : mMoviesList) {
                    Log.e(HomeActivity.TAG, "title = " + movies.getTitle());
                }
                //end
            }

            @Override
            public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    private void loadMovies(Callback<HotMoviesInfo> callback) {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchHotMovies().enqueue(callback);
    }

}
