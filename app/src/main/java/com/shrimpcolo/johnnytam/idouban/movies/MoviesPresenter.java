package com.shrimpcolo.johnnytam.idouban.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.mobileapi.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;
import com.shrimpcolo.johnnytam.idouban.entity.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Johnny Tam on 2017/3/19.
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    private final static String TAG = MoviesPresenter.class.getSimpleName();

    private final MoviesContract.View mMoviesView;

    private final IDoubanService mIDuobanService;

    private boolean mFirstLoad = true;

    private int mMovieTotal;

    public MoviesPresenter(@NonNull IDoubanService moviesService, @NonNull MoviesContract.View moviesView) {
        mIDuobanService = checkNotNull(moviesService, "IDoubanServie cannot be null!");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        Log.d(HomeActivity.TAG, TAG + ", MoviesPresenter: create!");
        mMoviesView.setPresenter(this);
    }

    @Override
    public void loadRefreshedMovies(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadMovies(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void loadMoreMovies(int movieStartIndex) {
        loadMoreMovies(movieStartIndex, true);
    }

    @Override
    public void openMovieDetails(Movie clickedMovie) {
    }

    @Override
    public void start() {
        Log.e(HomeActivity.TAG, TAG + ", start() ");
        loadRefreshedMovies(false);
    }

    private void loadMovies(boolean forceUpdate, final boolean showLoadingUI){
        if(showLoadingUI){
            //MoviesFragment需要显示Loading 界面
            mMoviesView.setRefreshedIndicator(true);
        }

        if(forceUpdate) {
            mIDuobanService.searchHotMovies(0).enqueue(new Callback<HotMoviesInfo>() {
                @Override
                public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
                    Log.d(HomeActivity.TAG, "===> onResponse: Thread.Id = " + Thread.currentThread().getId());
                    List<Movie> moviesList = response.body().getMovies();
                    mMovieTotal = response.body().getTotal();
                    //debug
                    Log.e(HomeActivity.TAG, "===>Search Movies: Response, size = " + moviesList.size()
                            + " showLoadingUI: " + showLoadingUI + ", total = " + mMovieTotal);

                    mMoviesView.setMoviesTotal(mMovieTotal);

                    //获取数据成功，Loading UI消失
                    if (showLoadingUI) {
                        mMoviesView.setRefreshedIndicator(false);
                    }

                    processMovies(moviesList);
                }

                @Override
                public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
                    Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                            + Thread.currentThread().getId() + ", Error: " + t.getMessage());

                    //获取数据成功，Loading UI消失
                    if (showLoadingUI) {
                        mMoviesView.setRefreshedIndicator(false);
                    }
                    processEmptyMovies();
                }
            });
        }
    }

    private void loadMoreMovies(int movieStartIndex, final boolean showLoadingMoreUI){
//        if(showLoadingMoreUI){
//            //RecycleView需要显示LoadingMore 界面
//            mMoviesView.setLoadMoreIndicator(true);
//        }
        Log.e(HomeActivity.TAG, "movieStartIndex: " + movieStartIndex + ", mMovieTotal: " + mMovieTotal);
        if(movieStartIndex >= mMovieTotal) {
            processLoadMoreEmptyMovies();
            return;
        }

        mIDuobanService.searchHotMovies(movieStartIndex).enqueue(new Callback<HotMoviesInfo>() {
            @Override
            public void onResponse(Call<HotMoviesInfo> call, Response<HotMoviesInfo> response) {
                List<Movie> moreMoviesList = response.body().getMovies();
                //debug
                Log.e(HomeActivity.TAG, "===>Search moreMoviesList: Response, size = " + moreMoviesList.size()
//                        + " showLoadingUI: " + showLoadingMoreUI
                );

                //LoadingMore 获取数据成功，Loading More UI消失
//                if (showLoadingMoreUI) {
//                    mMoviesView.setLoadMoreIndicator(false);
//                }

                processLoadMoreMovies(moreMoviesList);
            }

            @Override
            public void onFailure(Call<HotMoviesInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());

                //获取数据成功，Loading UI消失
//                if (showLoadingMoreUI) {
//                    mMoviesView.setLoadMoreIndicator(false);
//                }
                processLoadMoreEmptyMovies();
            }
        });
    }

    private void processMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            // Show a message indicating there are no movies for users
            processEmptyMovies();
        } else {
            // Show the list of movies
            mMoviesView.showRefreshedMovies(movies);
        }
    }

    private void processEmptyMovies() {
        //MoviesFragment需要给出提示
        mMoviesView.showNoMovies();
    }

    private void processLoadMoreMovies(List<Movie> movies) {
        if(movies.isEmpty()) {
            processLoadMoreEmptyMovies();
        }else {
            mMoviesView.showLoadedMoreMovies(movies);
        }
    }

    private void processLoadMoreEmptyMovies() {
        mMoviesView.showNoLoadedMoreMovies();
    }

}
