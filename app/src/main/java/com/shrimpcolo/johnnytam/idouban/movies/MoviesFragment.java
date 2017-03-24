package com.shrimpcolo.johnnytam.idouban.movies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewAdapter;
import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewHolder;
import com.shrimpcolo.johnnytam.idouban.entity.Movie;
import com.shrimpcolo.johnnytam.idouban.moviedetail.MovieDetailActivity;
import com.shrimpcolo.johnnytam.idouban.utils.AppConstants;
import com.shrimpcolo.johnnytam.idouban.listener.OnEndlessRecyclerViewScrollListener;
import com.shrimpcolo.johnnytam.idouban.ui.ScrollChildSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.shrimpcolo.johnnytam.idouban.utils.AppConstants.MSG_LOADMORE_DATA;
import static com.shrimpcolo.johnnytam.idouban.utils.AppConstants.MSG_LOADMORE_UI_ADD;
import static com.shrimpcolo.johnnytam.idouban.utils.AppConstants.MSG_LOADMORE_UI_DELETE;
import static com.shrimpcolo.johnnytam.idouban.utils.AppConstants.VIEW_TYPE_ITEM;
import static com.shrimpcolo.johnnytam.idouban.utils.AppConstants.VIEW_TYPE_LOADING;
import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewHolder.BuilderItemViewHolder;
import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewHolder.BuilderLoadingViewHolder;

/**
 * 展示一系列电影{@link Movie} 页面， 使用RecycleView 展示
 */
public class MoviesFragment extends Fragment implements MoviesContract.View{

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private MoviesContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private View mNoMoviesView;

    private BaseRecycleViewAdapter mMovieAdapter;

    private MoviesHandle mHandler;

    private List<Movie> mAdapterMoviesData;

    private int mMovieTotal;

    class MoviesHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADMORE_UI_ADD:
                    Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_UI_ADD totalItem: " + msg.arg1);
                    mMovieAdapter.getData().add(null);
                    mMovieAdapter.notifyItemInserted(mMovieAdapter.getData().size() - 1);

                    Message msgLoadMore = mHandler.obtainMessage(MSG_LOADMORE_DATA, msg.arg1, -1);
                    mHandler.sendMessage(msgLoadMore);
                    break;

                case MSG_LOADMORE_UI_DELETE:
                    Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_UI_DELETE : ");
                    mMovieAdapter.getData().remove(mMovieAdapter.getData().size() - 1);
                    mMovieAdapter.notifyItemRemoved(mMovieAdapter.getData().size());
                    break;

                case MSG_LOADMORE_DATA:
                    Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_DATA totalItem: " + msg.arg1);
                    mPresenter.loadMoreMovies(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(){
        //Log.e(HomeActivity.TAG, "MoviesFragment newInstance!");
        return new MoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(HomeActivity.TAG,  TAG + " onCreate()");
        mHandler = new MoviesHandle();
        mAdapterMoviesData = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.e(HomeActivity.TAG,  TAG + " onCreateView()");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_hot_movies);
        mNoMoviesView = view.findViewById(R.id.ll_no_movies);

        //set recycle view
        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        //create movie adapter
        BuilderItemViewHolder builderItemViewHolder = itemView -> new MovieViewHolder(itemView);
        BuilderLoadingViewHolder builderLoadingViewHolder = loadingItemView -> new LoadingViewHolder(loadingItemView);

        mMovieAdapter = new BaseRecycleViewAdapter<>(new ArrayList<>(0),
                R.layout.recyclerview_movies_item,
                R.layout.recyclerview_loading_item,
                builderItemViewHolder,
                builderLoadingViewHolder
        );

        mRecyclerView.setAdapter(mMovieAdapter);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) view.findViewById(R.id.movie_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(mRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.e(HomeActivity.TAG, "\n\n onRefresh loadRefreshedMovies...");
            mPresenter.loadRefreshedMovies(true);
        });

        mRecyclerView.addOnScrollListener(new OnEndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;// -1: 不存在FooterView， 其他数字表示存在FooterView
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(HomeActivity.TAG, " CallBack onLoadMore => page: " + page + ", totalItemsCount = " + totalItemsCount);

                final Message msg = mHandler.obtainMessage(MSG_LOADMORE_UI_ADD, totalItemsCount, -1);
                msg.sendToTarget();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Log.e(HomeActivity.TAG,  TAG + " onViewCreated(): Presenter!  "  + mPresenter);
        if(mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void showRefreshedMovies(List<Movie> movies) {
        /** Of cause, there is a bug:
         *  step1: Load the data firstly, then scroll the recycle view load more data
         *  step2: go to top. refresh the data, at that time the server push some new movie data.
         *  result: the new movie data will append to mAdapterMovieData array, that's error!
         *  But, in simple, I don't care that case.
         */
        //If the refreshed data is a part of mAdapterMovieData, don't operate mMovieAdapter
        if(mAdapterMoviesData.size() != 0 && movies.get(0).getId().equals(mAdapterMoviesData.get(0).getId())) {
            return;
        }

        mAdapterMoviesData.addAll(movies);
        mMovieAdapter.replaceData(mAdapterMoviesData);

//        Log.e(HomeActivity.TAG,  TAG + " showRefreshedMovies: \n" +
//                "mAdapterMoviesData.size() =  " + mAdapterMoviesData.size()
//        + ", getMoviesList.size = " + movies.size() + ", adapter's movies.size = " + mMovieAdapter.mMovies.size());

        mRecyclerView.setVisibility(View.VISIBLE);
        mNoMoviesView.setVisibility(View.GONE);
    }

    @Override
    public void showNoMovies() {
        mRecyclerView.setVisibility(View.GONE);
        mNoMoviesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadedMoreMovies(List<Movie> movies) {
        Log.e(HomeActivity.TAG,  TAG + " showLoadedMoreMovies 111 : \n" +
                "mAdapterMoviesData.size() =  " + mAdapterMoviesData.size()
                + ", LoadMoreList.size = " + movies.size() + ", adapter's movies.size = " + mMovieAdapter.getData().size());

        mMovieAdapter.getData().remove(mMovieAdapter.getData().size() - 1);
        mMovieAdapter.notifyItemRemoved(mMovieAdapter.getData().size());

        mAdapterMoviesData.addAll(movies);
        mMovieAdapter.replaceData(mAdapterMoviesData);

        Log.e(HomeActivity.TAG,  TAG + " showLoadedMoreMovies 222 : \n" +
                "mAdapterMoviesData.size() =  " + mAdapterMoviesData.size()
                + ", LoadMoreList.size = " + movies.size() + ", adapter's movies.size = " + mMovieAdapter.getData().size());
    }

    @Override
    public void setLoadMoreIndicator(boolean active) {

    }

    @Override
    public void showNoLoadedMoreMovies() {
        Log.e(HomeActivity.TAG, "\n\n showNoLoadedMoreMovies");

        mHandler.sendEmptyMessageDelayed(MSG_LOADMORE_UI_DELETE, 500);
    }

    @Override
    public void setMoviesTotal(int total) {
        mMovieTotal = total;
    }

    @Override
    public void showMovieDetailUi(String movieName) {
    }

    @Override
    public void setRefreshedIndicator(boolean active) {
        if(getView() == null) return;

        Log.e(HomeActivity.TAG, TAG + "=> loading indicator: " + active);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.movie_refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(() -> {
            Log.e(HomeActivity.TAG, "swipeRefreshLayout run() active: " + active);
            swipeRefreshLayout.setRefreshing(active);
        });

    }

    @Override
    public void setPresenter(MoviesContract.Presenter presenter) {
        //Log.e(HomeActivity.TAG,  TAG + " setPresenter ");
        mPresenter = presenter;
    }

    //Movie's Adapter and view holder
//    static class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private List<Movie> mMovies;
//
//        @LayoutRes
//        private int layoutItemViewResId;
//        @LayoutRes
//        private int layoutLoadingResId;
//
//        public MovieAdapter(@NonNull List<Movie> movies,
//                            @LayoutRes int layoutItemViewId, @LayoutRes int layoutLoadingResId) {
//            setList(movies);
//            this.layoutItemViewResId = layoutItemViewId;
//            this.layoutLoadingResId = layoutLoadingResId;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return mMovies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//        }
//
//        private void setList(List<Movie> movies) {
//            mMovies =  checkNotNull(movies);
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            RecyclerView.ViewHolder viewHolder;
//
//            if(VIEW_TYPE_ITEM == viewType) {
//                View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutItemViewResId, parent, false);
//                viewHolder = new MovieViewHolder(itemView);
//            }else {
//                View loadingView = LayoutInflater.from(parent.getContext()).inflate(layoutLoadingResId, parent, false);
//                viewHolder = new LoadingViewHolder(loadingView);
//            }
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if(holder == null) return;
//            if(holder instanceof MovieViewHolder) {
//                ((MovieViewHolder)holder).updateMovie(mMovies.get(position));
//
//            }else if(holder instanceof LoadingViewHolder) {
//                ((LoadingViewHolder)holder).updateLoading();
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mMovies == null ? 0 : mMovies.size();
//        }
//
//        public void replaceData(List<Movie> movies) {
//            setList(movies);
//            notifyDataSetChanged();
//        }
//
//    }

    public static class MovieViewHolder extends BaseRecycleViewHolder<Movie> implements View.OnClickListener {

        ImageView mMovieImage;

        TextView mMovieTitle;

        RatingBar mMovieStars;

        TextView mMovieRatingAverage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMovieImage = (ImageView) itemView.findViewById(R.id.movie_cover);
            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMovieStars = (RatingBar) itemView.findViewById(R.id.rating_star);
            mMovieRatingAverage = (TextView) itemView.findViewById(R.id.movie_average);

            itemView.setOnClickListener(this);
        }

        @Override
        protected void onBindItem(Movie movie) {
            Context context = itemView.getContext();
            if(movie == null || context == null) return;

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
            if (itemContent == null || itemView == null) return;

            Context context = itemView.getContext();
            if (context == null) return;

            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(AppConstants.INTENT_EXTRA_MOVIE, itemContent);//itemContent means Movie object

            if (context instanceof Activity) {
                Activity activity = (Activity) context;

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, mMovieImage, "cover").toBundle();
                ActivityCompat.startActivity(activity, intent, bundle);
            }

        }
    }

    static class LoadingViewHolder extends BaseRecycleViewHolder<Movie> {

        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progress_bar);
        }

        @Override
        protected void onBindItem(Movie movie) {
            if(movie == null)
                progressBar.setIndeterminate(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(HomeActivity.TAG, "onPause()!!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(HomeActivity.TAG, "onStop()!!!");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(HomeActivity.TAG, "onDestroyView()!!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(HomeActivity.TAG, "onDestroy()!!!");
    }
}
