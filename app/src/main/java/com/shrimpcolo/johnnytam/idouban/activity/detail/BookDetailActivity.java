package com.shrimpcolo.johnnytam.idouban.activity.detail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.home.BaseActivity;
import com.shrimpcolo.johnnytam.idouban.adapter.BasePagerAdapter;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.fragment.BookDetailFragment;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends BaseActivity {
    private Book book;

    @Override
    protected void initVariables() {
        //get the content from the intent.
        book = (Book) getIntent().getSerializableExtra("book");
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_book_detail);

        //CollapsingToolbarLayout
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(book.getTitle());
        }
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        Picasso.with(ivImage.getContext())
                .load(book.getImages().getLarge())
                .into(ivImage);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        BookPagerAdapter adapter = new BookPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BookDetailFragment.newInstance(book.getSummary()), getString(R.string.book_content_desc));
        adapter.addFragment(BookDetailFragment.newInstance(book.getAuthor_intro()), getString(R.string.book_author_desc));
        adapter.addFragment(BookDetailFragment.newInstance(book.getCatalog()), getString(R.string.book_Catalog));
        viewPager.setAdapter(adapter);
    }

    static class BookPagerAdapter extends BasePagerAdapter {

        public BookPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFrgmentTitles.add(title);
        }
    }

}