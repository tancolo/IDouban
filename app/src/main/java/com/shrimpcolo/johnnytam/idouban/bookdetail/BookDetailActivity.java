package com.shrimpcolo.johnnytam.idouban.bookdetail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.base.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.base.BaseViewPagerAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.moviedetail.MovieDetailActivity;
import com.shrimpcolo.johnnytam.idouban.utils.AppConstants;
import com.squareup.picasso.Picasso;

/**
 * 这里没有使用MVP， 因为Book数据是从书籍列表Item 传入。
 * 目的是 参考{@link MovieDetailActivity}，对直接在 View 中操作数据 和 MVP中 P层操作数据的区别
 * 如果传入的是 书籍的某个Key (豆瓣自己定义的 book id)， 最好考虑使用MVP，P层请求网络数据，完成后
 * 再使用View.showRefreshedBooks 来间接更新 View 中数据
 */
public class BookDetailActivity extends BaseActivity {

    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initVariables() {
        //get the content from the intent.
        mBook = (Book) getIntent().getSerializableExtra(AppConstants.INTENT_EXTRA_BOOK);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_detail);

        initToolbarLayout();
        initPicasso();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        iniTabLayout(viewPager);

    }

    private void initToolbarLayout(){
        //CollapsingToolbarLayout
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mBook.getTitle());
    }

    private void initPicasso(){
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        Picasso.with(ivImage.getContext())
                .load(mBook.getImages().getLarge())
                .into(ivImage);
    }

    private void iniTabLayout(ViewPager viewPager){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        BookDetailPagerAdapter adapter = new BookDetailPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getSummary()), getString(R.string.book_content_desc));
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getAuthor_intro()), getString(R.string.book_author_desc));
        adapter.addFragment(BookDetailFragment.newInstance(mBook.getCatalog()), getString(R.string.book_catalog));
        viewPager.setAdapter(adapter);
    }

    static class BookDetailPagerAdapter extends BaseViewPagerAdapter {

        public BookDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

    }

}
