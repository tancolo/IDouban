package com.shrimpcolo.johnnytam.idouban.activity.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.adapter.BasePagerAdapter;
import com.shrimpcolo.johnnytam.idouban.fragment.BooksFragment;
import com.shrimpcolo.johnnytam.idouban.fragment.MoviesFragment;

public class HomeActivity extends BaseActivity {
    public static final String TAG = "COLO";
//    private ViewPager mViewPager;

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

        //初始化控件
        ViewPager viewPager = (ViewPager) findViewById(R.id.douban_view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.douban_sliding_tabs);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(viewPager);
        }
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
