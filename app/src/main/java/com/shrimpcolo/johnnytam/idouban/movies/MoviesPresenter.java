package com.shrimpcolo.johnnytam.idouban.movies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.mobileapi.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.entity.HotMoviesInfo;
import com.shrimpcolo.johnnytam.idouban.entity.Movie;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

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

    @NonNull
    private CompositeSubscription mSubscriptions;

    public MoviesPresenter(@NonNull IDoubanService moviesService, @NonNull MoviesContract.View moviesView) {
        mIDuobanService = checkNotNull(moviesService, "IDoubanServie cannot be null!");
        mMoviesView = checkNotNull(moviesView, "moviesView cannot be null!");

        Log.d(HomeActivity.TAG, TAG + ", MoviesPresenter: create!");
        mSubscriptions = new CompositeSubscription();
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
    public void subscribe() {
        Log.e(HomeActivity.TAG, TAG + ", subscribe() ");
        loadRefreshedMovies(false);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    private void loadMovies(boolean forceUpdate, final boolean showLoadingUI){
        if(showLoadingUI){
            //MoviesFragment需要显示Loading 界面
            mMoviesView.setRefreshedIndicator(true);
        }

        mSubscriptions.clear();

        if(forceUpdate) {
            Subscription subscription = mIDuobanService
                    .searchHotMovies(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HotMoviesInfo>() {
                        @Override
                        public void onCompleted() {
                            if (showLoadingUI) mMoviesView.setRefreshedIndicator(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(HomeActivity.TAG, "===>Error: " + e.getMessage());
                            //获取数据成功，Loading UI消失
                            if (showLoadingUI) mMoviesView.setRefreshedIndicator(false);
                            processEmptyMovies();
                        }

                        @Override
                        public void onNext(HotMoviesInfo hotMoviesInfo) {
                            List<Movie> moviesList = hotMoviesInfo.getMovies();
                            mMovieTotal = hotMoviesInfo.getTotal();
                            //debug
                            Log.e(HomeActivity.TAG, "===>Search Movies: Response, size = " + moviesList.size()
                                    + " showLoadingUI: " + showLoadingUI + ", total = " + mMovieTotal);

                            mMoviesView.setMoviesTotal(mMovieTotal);
                            processMovies(moviesList);
                        }
                    });

            mSubscriptions.add(subscription);
        }
    }

    private void loadMoreMovies(int movieStartIndex, final boolean showLoadingMoreUI){
        Log.e(HomeActivity.TAG, "movieStartIndex: " + movieStartIndex + ", mMovieTotal: " + mMovieTotal);

        if(movieStartIndex >= mMovieTotal) {
            processLoadMoreEmptyMovies();
            return;
        }

        mSubscriptions.clear();

        Subscription subscription = mIDuobanService
                .searchHotMovies(movieStartIndex)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotMoviesInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.d(HomeActivity.TAG, "===>onCompleted ！！！");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(HomeActivity.TAG, "===>Error: " + e.getMessage());
                        processLoadMoreEmptyMovies();
                    }

                    @Override
                    public void onNext(HotMoviesInfo hotMoviesInfo) {
                        Log.e(HomeActivity.TAG, "===>Search moreMoviesList: Response, size = " + hotMoviesInfo.getMovies().size());
                        processLoadMoreMovies(hotMoviesInfo.getMovies());
                    }
                });
        mSubscriptions.add(subscription);
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
