package com.shrimpcolo.johnnytam.idouban.activity.detail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.fragment.BookDetailFragment;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //get the content from the intent.
        book = (Book) getIntent().getSerializableExtra("book");
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

    static class BookPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFrgmentTitles = new ArrayList<>();

        public BookPagerAdapter(FragmentManager fm) {
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

}
