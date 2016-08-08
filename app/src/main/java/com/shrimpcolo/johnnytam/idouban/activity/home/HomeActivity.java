package com.shrimpcolo.johnnytam.idouban.activity.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.adapter.BasePagerAdapter;
import com.shrimpcolo.johnnytam.idouban.fragment.BooksFragment;
import com.shrimpcolo.johnnytam.idouban.fragment.MoviesFragment;

public class HomeActivity extends BaseActivity {
    public static final String TAG = "COLO";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
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

        //init DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupDrawerNavigation(mNavigationView);

        //set profile Image
        setUpUserProfile();

        //初始化控件Home界面
        ViewPager viewPager = (ViewPager) findViewById(R.id.douban_view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.douban_sliding_tabs);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setUpUserProfile(){
        View headView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        View profileView = headView.findViewById(R.id.profile_image);
        if(profileView != null) {
            profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "===> onClick...!");
                    mDrawerLayout.closeDrawers();
                    mNavigationView.getMenu().getItem(0).setChecked(true);
                }
            });
        }
    }
    private void setupDrawerNavigation(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_movies:
                        Log.e(TAG, "===> ITEM BOOK");
                        break;
                    case R.id.navigation_item_book:
                        Log.e(TAG, "===> ITEM EXAMPLE");
                        break;
                    case R.id.navigation_item_about:
                        Log.e(TAG, "===> ITEM ABOUT");
                        break;
                    case R.id.navigation_item_blog:
                        Log.e(TAG, "===> ITEM BLOG");
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        DoubanPagerAdapter pagerAdapter = new DoubanPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new MoviesFragment(), getApplicationContext().getResources().getString(R.string.tab_movies_fragment));
        pagerAdapter.addFragment(new BooksFragment(), getApplicationContext().getResources().getString(R.string.tab_books_fragment));
        viewPager.setAdapter(pagerAdapter);
    }

    static class DoubanPagerAdapter extends BasePagerAdapter {
        public DoubanPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFrgmentTitles.add(title);
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
