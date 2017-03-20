package com.shrimpcolo.johnnytam.idouban.movies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.beans.Movie;
import com.shrimpcolo.johnnytam.idouban.moviesdetail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 展示一系列电影{@link Movie} 页面， 使用RecycleView 展示
 */
public class MoviesFragment extends Fragment implements MoviesContract.View{

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private MoviesContract.Presenter mPresenter;

    private List<Movie> mMoviesList = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(){
        Log.e(HomeActivity.TAG, "MoviesFragment newInstance!");
        return new MoviesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e(HomeActivity.TAG, "MoviesFragment onAttach, presenter: " + mPresenter);
        if(mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieAdapter = new MovieAdapter(getContext(), mMoviesList, R.layout.recyclerview_movies_item);
        Log.e(HomeActivity.TAG,  TAG + " onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(HomeActivity.TAG,  TAG + " onCreateView()");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_hot_movies);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(HomeActivity.TAG,  TAG + " onActivityCreated()");

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(HomeActivity.TAG,  TAG + " onResume, Presenter.start(): " + mPresenter
//        +"\n" + Log.getStackTraceString(new Throwable()));
    }

    @Override
    public void showMovies(List<Movie> movies) {
        Log.e(HomeActivity.TAG,  TAG + " showMovies ");
        mMovieAdapter.replaceData(movies);

    }

    @Override
    public void showNoMovies() {

    }

    @Override
    public void showMovieDetailUi() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(getView() == null) return;

        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.pgb_loading);

        Log.e(HomeActivity.TAG, "\n\n setLoadingIndicator: active " + active);

        if(active) {
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        Log.e(HomeActivity.TAG,  TAG + " setPresenter ");
        mPresenter = presenter;
    }

    //Movie's Adapter and view holder
    static class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

        private List<Movie> mMovies;
        private Context context;

        @LayoutRes
        private int layoutResId;

        public MovieAdapter(Context context, @NonNull List<Movie> movies, @LayoutRes int layoutResId) {
            setList(movies);
            this.layoutResId = layoutResId;
            this.context = context;
        }

        private void setList(List<Movie> movies) {
            mMovies =  checkNotNull(movies);
        }

        @Override
        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            return new MovieViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MovieViewHolder holder, int position) {
            if (holder == null) return;

            holder.updateMovie(mMovies.get(position));
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        public void replaceData(List<Movie> movies) {
            setList(movies);
            notifyDataSetChanged();
        }
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieImage;

        TextView mMovieTitle;

        RatingBar mMovieStars;

        TextView mMovieRatingAverage;

        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMovieImage = (ImageView) itemView.findViewById(R.id.movie_cover);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMovieStars = (RatingBar) itemView.findViewById(R.id.rating_star);
            mMovieRatingAverage = (TextView) itemView.findViewById(R.id.movie_average);

            itemView.setOnClickListener(this);
        }

        public void updateMovie(Movie movie) {
            if (movie == null) return;
            this.movie = movie;

            Context context = itemView.getContext();

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
        public void onClick(View v) {
            Log.e(HomeActivity.TAG, "==> onClick....Item");

            if (movie == null) return;
            if (itemView == null) return;

            Context context = itemView.getContext();
            if (context == null) return;

            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movie", movie);

            if (context instanceof Activity) {
                Activity activity = (Activity) context;

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, mMovieImage, "cover").toBundle();
                ActivityCompat.startActivity(activity, intent, bundle);
            }

        }
    }

}
