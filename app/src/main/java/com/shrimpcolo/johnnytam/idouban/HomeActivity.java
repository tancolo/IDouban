package com.shrimpcolo.johnnytam.idouban;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.shrimpcolo.johnnytam.idouban.api.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.books.BooksFragment;
import com.shrimpcolo.johnnytam.idouban.movies.MoviesContract;
import com.shrimpcolo.johnnytam.idouban.movies.MoviesFragment;
import com.shrimpcolo.johnnytam.idouban.movies.MoviesPresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "COLO";
    private static final String SUB = HomeActivity.class.getSimpleName();

    private ViewPager mViewPager;

    private MoviesPresenter mMoviesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.e(TAG, SUB + "onCreate!");
        //初始化控件
        mViewPager = (ViewPager) findViewById(R.id.douban_view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.douban_sliding_tabs);
        if(tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(mViewPager);
        }

    }

    private void setupViewPager(ViewPager viewPager){
        DoubanPagerAdapter pagerAdapter = new DoubanPagerAdapter(getSupportFragmentManager());
        MoviesFragment moviesFragment = MoviesFragment.newInstance();

        Log.e(TAG, SUB + "setupViewPager, moviesFragment = " + moviesFragment);

        pagerAdapter.addFragment(moviesFragment
                , getApplicationContext().getResources().getString(R.string.tab_movies_fragment));

        pagerAdapter.addFragment(new BooksFragment(),
                getApplicationContext().getResources().getString(R.string.tab_books_fragment));
        viewPager.setAdapter(pagerAdapter);

        createPresenter(moviesFragment);

    }

    private void createPresenter(MoviesContract.View fragmentView){
        Log.e(TAG, SUB + "createPresenter, fragmentView = " + fragmentView);
        //Create the movies presenter
        mMoviesPresenter = new MoviesPresenter(DoubanManager.createDoubanService(), fragmentView);
    }

    static class DoubanPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFrgmentTitles = new ArrayList<>();

        public DoubanPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFrgmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFrgmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
