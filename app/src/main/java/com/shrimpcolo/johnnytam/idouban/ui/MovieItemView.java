package com.shrimpcolo.johnnytam.idouban.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.interfaces.IModelView;
import com.shrimpcolo.johnnytam.idouban.activity.detail.MovieDetailActivity;
import com.shrimpcolo.johnnytam.idouban.entity.Movies;
import com.squareup.picasso.Picasso;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by Johnny Tam on 2016/7/28.
 */
public class MovieItemView extends CardView implements View.OnClickListener, View.OnLongClickListener, IModelView<Movies> {
    private ImageView mMovieImage;
    private TextView mMovieTitle;
    private RatingBar mMovieStars;
    private TextView mMovieRatingAverage;

    private Movies mMovie;

    public MovieItemView(Context context) {
        super(context);
    }

    public MovieItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mMovieImage = (ImageView) findViewById(R.id.movie_cover);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMovieStars = (RatingBar) findViewById(R.id.rating_star);
        mMovieRatingAverage = (TextView) findViewById(R.id.movie_average);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.e(HomeActivity.TAG, "==> onClick....Movie Item");
        if (mMovie == null) return;

        Context context = getContext();
        if (context == null) return;

        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("movie", mMovie);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, mMovieImage, "cover").toBundle();
            ActivityCompat.startActivity(activity, intent, bundle);
        }
    }

    @Override
    public void onBindItem(Movies movie) {
        mMovie = movie;
        Context context = getContext();
        if (context == null) return;

        Picasso.with(context)
                .load(movie.getImages().getLarge())
                .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .into(mMovieImage);

        mMovieTitle.setText(movie.getTitle());
        final double average = movie.getRating().getAverage();
        if (average == 0.0) {
            mMovieStars.setVisibility(View.GONE);
            mMovieRatingAverage.setText(context.getResources().getString(R.string.string_no_note));
        } else {
            mMovieStars.setVisibility(View.VISIBLE);
            mMovieRatingAverage.setText(String.valueOf(average));
            mMovieStars.setStepSize(0.5f);
            mMovieStars.setRating((float) (movie.getRating().getAverage() / 2));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        showShare(false);
        return true;
    }

    /**
     *
     * @param isSilent 是否直接分享， false： 不是；true: 是
     */
    private void showShare(Boolean isSilent) {
        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();

//        Log.e(HomeActivity.TAG, "Title: " + mMovie.getTitle()
//                + "\n Text: " + getMovieInfoForShare(mMovie));
//        Log.e(HomeActivity.TAG, "Url: " + mMovie.getAlt());
//        Log.e(HomeActivity.TAG, "ImageUrl: " + mMovie.getImages().getSmall());

        oks.setTitle(mMovie.getTitle());
        oks.setText(getMovieInfoForShare(mMovie));
        oks.setUrl(mMovie.getAlt());
        oks.setImageUrl(mMovie.getImages().getSmall());

        //close the sso authorize
        oks.disableSSOWhenAuthorize();
        oks.setSilent(isSilent);

        oks.show(getContext());
    }

    private String getMovieInfoForShare(Movies movie) {
        //拼接影片信息, 导演， 主演，又名， 上映时间， 类型， 片长，地区， 语言，IMDB
        StringBuilder movieBuilder = new StringBuilder();
        Resources resources = getContext().getResources();

        movieBuilder.append(resources.getString(R.string.movie_directors));
        for (Movies.DirectorsBean director : movie.getDirectors()) {
            movieBuilder.append(director.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //主演
        movieBuilder.append(resources.getString(R.string.movie_casts));
        for (Movies.CastsBean cast : movie.getCasts()) {
            movieBuilder.append(cast.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //又名
        movieBuilder.append(resources.getString(R.string.movie_aka));
        movieBuilder.append(movie.getOriginal_title());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_year));
        movieBuilder.append(movie.getYear());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_genres));
        for (int index = 0; index < movie.getGenres().size(); index++) {
            movieBuilder.append(movie.getGenres().get(index));
            movieBuilder.append(" / ");
        }
        movieBuilder.append("\n");

        return movieBuilder.toString();
    }
//    @Override
//    public View getView() {
//        return this;
//    }
}
