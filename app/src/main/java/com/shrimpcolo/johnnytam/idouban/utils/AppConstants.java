package com.shrimpcolo.johnnytam.idouban.utils;

/**
 * Created by Johnny Tam on 2017/3/21.
 */

public final class AppConstants {

    public static final String INTENT_EXTRA_MOVIE = "movie"; //MoviesFragment

    public static final String INTENT_EXTRA_BOOK = "book"; //BooksFragment

    public static final int TYPE_MOVIE_INFO = 0;//MovieDetailFragment

    public static final int TYPE_MOVIE_WEBSITE = 1; //MovieDetailFragment

    public static final String INTENT_EXTRA_FRAGMENT_INFO = "info"; //

    public static final String INTENT_EXTRA_FRAGMENT_TYPE = "type"; //

    public static final String INTENT_EXTRA_WEBSITE_URL =  "website";

    public static final int MSG_LOADMORE_UI_ADD = 0x1000;

    public static final int MSG_LOADMORE_UI_DELETE = 0x1001;

    public static final int MSG_LOADMORE_DATA = 0x1002;

    public static final int VIEW_TYPE_ITEM = 0;

    public static final int VIEW_TYPE_LOADING = 1;

    public static final int MSG_LOADMORE_UI_DELETE_DELAYED = 500; //ms
}
