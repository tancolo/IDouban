package com.shrimpcolo.johnnytam.idouban.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.loginShare.LoginActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.BasePagerAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.QQEntity;
import com.shrimpcolo.johnnytam.idouban.fragment.AboutMeFragment;
import com.shrimpcolo.johnnytam.idouban.fragment.BooksFragment;
import com.shrimpcolo.johnnytam.idouban.fragment.JianshuFragment;
import com.shrimpcolo.johnnytam.idouban.fragment.MoviesFragment;
import com.squareup.picasso.Picasso;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class HomeActivity extends BaseActivity {
    public static final String TAG = "COLO";
    private static final String MENU_BLOG = "JIANSHU";
    private static final String MENU_ABOUT = "ABOUTME";
    private static final int LOGIN_REQUEST_CODE = 1000;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private static final int TAB_MOVIES = 0;
    private static final int TAB_BOOK = 1;

    private LinearLayout mLinearLayout;
    private ImageView profileView;
    private TextView profileName;

    QQEntity qqEntity = null;
    private ISetupProfile iSetupProfile;

    public interface ISetupProfile{
        void setupProfile(QQEntity qqEntity);
    }

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
        setupUserProfile();

        //初始化控件Home界面
        mViewPager = (ViewPager) findViewById(R.id.douban_view_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.douban_sliding_tabs);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(mViewPager);
        }

        //init tab_container LinearLayout
        mLinearLayout = (LinearLayout) findViewById(R.id.tab_container);

        initOthersFragment();
    }

    private void initOthersFragment() {
        //init blog fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        JianshuFragment jianshuFragment = new JianshuFragment();
        AboutMeFragment aboutFragment = new AboutMeFragment();
        iSetupProfile = aboutFragment;

        transaction.add(R.id.frame_container, jianshuFragment, MENU_BLOG);
        transaction.add(R.id.frame_container, aboutFragment, MENU_ABOUT);
        transaction.commit();
    }

    private void setupUserProfile() {
        View headView = mNavigationView.inflateHeaderView(R.layout.navigation_header);
        profileView = (ImageView) headView.findViewById(R.id.profile_image);
        profileName = (TextView) headView.findViewById(R.id.profile_name);

        if (profileView != null) {
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
                FragmentTransaction transacation = getSupportFragmentManager().beginTransaction();
                Log.e(TAG, "===> getFragments.size = " + getSupportFragmentManager().getFragments().size());

                //for show or hide fragment
                switch (item.getItemId()) {
                    case R.id.navigation_item_movies:
                    case R.id.navigation_item_book:
                        if (mLinearLayout.getVisibility() == View.GONE) {
                            mLinearLayout.setVisibility(View.VISIBLE);
                        }
                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            if (fragment.getTag().equals(MENU_BLOG) || fragment.getTag().equals(MENU_ABOUT)) {
                                transacation.hide(fragment);
                            } else {
                                transacation.show(fragment);
                            }
                        }
                        break;
                    case R.id.navigation_item_blog:
                    case R.id.navigation_item_about:
                        if (mLinearLayout.getVisibility() == View.VISIBLE) {
                            mLinearLayout.setVisibility(View.GONE);
                        }
                        break;
                }

                switch (item.getItemId()) {
                    case R.id.navigation_item_movies:
                        mViewPager.setCurrentItem(TAB_MOVIES);
                        break;
                    case R.id.navigation_item_book:
                        mViewPager.setCurrentItem(TAB_BOOK);
                        break;
                    case R.id.navigation_item_about:
                    case R.id.navigation_item_blog:
                        String frgTag;
                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            frgTag = fragment.getTag();
                            if ((item.getItemId() == R.id.navigation_item_blog && frgTag.equals(MENU_BLOG))
                                    || (item.getItemId() == R.id.navigation_item_about && frgTag.equals(MENU_ABOUT))) {
                                transacation.show(fragment);
                            } else {
                                transacation.hide(fragment);
                            }
                        }
                        break;

                    case R.id.navigation_item_login:
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LOGIN_REQUEST_CODE);
                        //startActivity(intent);
                        break;

                    case R.id.navigation_item_logout:
                        ShareSDK.initSDK(HomeActivity.this);
                        Platform qq = ShareSDK.getPlatform(HomeActivity.this, QQ.NAME);
                        Platform weibo = ShareSDK.getPlatform(HomeActivity.this, SinaWeibo.NAME);
                        Platform wechat = ShareSDK.getPlatform(HomeActivity.this, Wechat.NAME);

                        if (qq.isValid()) {
                            Log.e(TAG, "remove QQ account!");
                            qq.removeAccount(true);
                        } else if (weibo.isValid()) {
                            Log.e(TAG, "remove weibo account!");
                            weibo.removeAccount(true);
                        } else if (wechat.isValid()) {
                            Log.e(TAG, "remove wechat account!");
                            wechat.removeAccount(true);
                        } else {
                        }
                        break;
                }
                transacation.commit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                qqEntity = (QQEntity)data.getSerializableExtra("qqentity");
                String url = qqEntity.getFigureurl_qq_2();
                String name = qqEntity.getNickname();
                Log.e(TAG, "url: " + url + ",  name: " + name );

                Picasso.with(this).load(url).into(profileView);
                profileName.setText(name);

                //callback for about me
                iSetupProfile.setupProfile(qqEntity);

            }
        }
    }
}
