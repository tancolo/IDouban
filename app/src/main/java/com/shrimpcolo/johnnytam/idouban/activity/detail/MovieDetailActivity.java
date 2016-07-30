package com.shrimpcolo.johnnytam.idouban.activity.detail;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.BasePagerAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.Movies;
import com.shrimpcolo.johnnytam.idouban.fragment.MovieDetailFragment;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends BaseActivity {

    private Movies movies;
    private static final int TYPE_MOVIE_INFO = 0;
    private static final int TYPE_MOVIE_WEBSITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initVariables() {
        movies = (Movies) getIntent().getSerializableExtra("movie");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_movie_detail);

        //find the UI view
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.movie_collapsing_toolbar);
        ImageView movieImage = (ImageView) findViewById(R.id.movie_image);
        ViewPager viewPager = (ViewPager) findViewById(R.id.movie_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.movie_sliding_tabs);

        //init view
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(movies.getTitle());
        }

        Picasso.with(movieImage.getContext())
                .load(movies.getImages().getLarge())
                .into(movieImage);

        setupViewPager(viewPager);

        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(MovieDetailFragment.newInstance(getMovieInfo(), TYPE_MOVIE_INFO), getString(R.string.movie_info));
        adapter.addFragment(MovieDetailFragment.newInstance(movies.getAlt(), TYPE_MOVIE_WEBSITE), getString(R.string.movie_description));
        viewPager.setAdapter(adapter);
    }

    private String getMovieInfo() {
        //拼接影片信息, 导演， 主演，又名， 上映时间， 类型， 片长，地区， 语言，IMDB
        StringBuilder movieBuilder = new StringBuilder();
        Resources resources = getApplicationContext().getResources();

        movieBuilder.append(resources.getString(R.string.movie_directors));
        for (Movies.DirectorsBean director : movies.getDirectors()) {
            movieBuilder.append(director.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //主演
        movieBuilder.append(resources.getString(R.string.movie_casts));
        for (Movies.CastsBean cast : movies.getCasts()) {
            movieBuilder.append(cast.getName());
            movieBuilder.append(" ");
        }
        movieBuilder.append("\n");

        //又名
        movieBuilder.append(resources.getString(R.string.movie_aka));
        movieBuilder.append(movies.getOriginal_title());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_year));
        movieBuilder.append(movies.getYear());
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_genres));
        for (int index = 0; index < movies.getGenres().size(); index++) {
            movieBuilder.append(movies.getGenres().get(index));
            movieBuilder.append(" / ");
        }
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_during));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_countries));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_languages));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        movieBuilder.append(resources.getString(R.string.movie_imdb));
        movieBuilder.append(resources.getString(R.string.movie_not_find));
        movieBuilder.append("\n");

        return movieBuilder.toString();
    }

    static class MoviePagerAdapter extends BasePagerAdapter {

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFrgmentTitles.add(title);
        }
    }
}
