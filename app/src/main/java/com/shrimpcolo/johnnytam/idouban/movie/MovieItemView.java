package com.shrimpcolo.johnnytam.idouban.movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.common.IModelView;
import com.squareup.picasso.Picasso;

/**
 * Created by Johnny Tam on 2016/7/28.
 */
public class MovieItemView extends CardView implements View.OnClickListener, IModelView<Movies> {
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

//    @Override
//    public View getView() {
//        return this;
//    }
}
