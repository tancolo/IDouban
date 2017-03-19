package com.shrimpcolo.johnnytam.idouban.movies;

import com.shrimpcolo.johnnytam.idouban.BasePresenter;
import com.shrimpcolo.johnnytam.idouban.BaseView;
import com.shrimpcolo.johnnytam.idouban.beans.Movie;

import java.util.List;

/**
 * Created by Johnny Tam on 2017/3/18.
 * 用于指定 Movies View(界面) 和 Presenter之间的 契约接口， 统一管理view 和 presenter接口
 */

public interface MoviesContract {
    interface View extends BaseView<Presenter> {

        void showMovies(List<Movie> movies);

        void showNoMovies();

        void showMovieDetailUi();
    }

    interface Presenter extends BasePresenter {
        void loadMovies(boolean forceUpdate);
    }
}
