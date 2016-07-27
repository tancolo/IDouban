package com.shrimpcolo.johnnytam.idouban.book;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.api.DoubanManager;
import com.shrimpcolo.johnnytam.idouban.api.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.movie.HotMoviesInfo;
import com.shrimpcolo.johnnytam.idouban.movie.MovieDetailActivity;
import com.shrimpcolo.johnnytam.idouban.movie.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {
    //private String mBookName = "黑客与画家";
    private List<Book> mBookList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;

    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(HomeActivity.TAG, "===>Book onAttach:  " + context.getClass().getName());

        loadBooks(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                mBookList = response.body().getBooks();
                //debug
                Log.e(HomeActivity.TAG, "===>Book Response, size = " + mBookList.size());
                mBookAdapter.setData(mBookList);
            }

            @Override
            public void onFailure(Call<BookInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===>Book onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_books);
        Log.e(HomeActivity.TAG, "===> mRecyclerView = " + mRecyclerView);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);

            mBookAdapter = new BookAdapter(getContext(), mBookList, R.layout.recyclerview_book_item);

            mRecyclerView.setAdapter(mBookAdapter);
        }
    }

    private void loadBooks(Callback<BookInfo> callback) {
        IDoubanService movieService = DoubanManager.createDoubanService();
        movieService.searchBooks("黑客与画家").enqueue(callback);
    }

    static class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
        private List<Book> mBookList;
        private Context mContext;

        @LayoutRes
        private int layoutResId;

        public BookAdapter(Context context, @NonNull List<Book> bookList, @LayoutRes int layoutResId) {
            this.mBookList = bookList;
            this.layoutResId = layoutResId;
            this.mContext = context;
        }

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutResId, parent, false);
            return new BookViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int position) {
            if (holder == null) return;

            holder.updateBook(mBookList.get(position));
        }

        @Override
        public int getItemCount() {
            return mBookList.size();
        }

        public void setData(List<Book> bookList) {
            this.mBookList = bookList;
            notifyDataSetChanged();
        }
    }


    static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        ImageView bookImage;
        TextView bookTitle;
        TextView bookAuthor;
        TextView bookSubTitle;
        TextView bookPubDate;
        TextView bookPages;
        TextView bookPrice;
        Book book;


        public BookViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            bookImage = (ImageView) itemView.findViewById(R.id.book_cover);
            bookTitle = (TextView) itemView.findViewById(R.id.txt_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.txt_author);
            bookSubTitle = (TextView) itemView.findViewById(R.id.txt_subTitle);
            bookPubDate = (TextView) itemView.findViewById(R.id.txt_pubDate);
            bookPrice = (TextView) itemView.findViewById(R.id.txt_prices);
            bookPages = (TextView) itemView.findViewById(R.id.txt_pages);

            itemView.setOnClickListener(this);
        }

        public void updateBook(Book book) {

            if (book == null) return;
            this.book = book;

            Context context = itemView.getContext();
            if (context == null) return;

            //get the prefix string
            String prefixSubTitle = context.getString(R.string.prefix_subtitle);
            String prefixAuthor = context.getString(R.string.prefix_author);
            String prefixPubDate = context.getString(R.string.prefix_pubdata);
            String prefixPages = context.getString(R.string.prefix_pages);
            String prefixPrice = context.getString(R.string.prefix_price);

            bookTitle.setText(book.getTitle());
            bookAuthor.setText(String.format(prefixAuthor, book.getAuthor()));
            bookSubTitle.setText(String.format(prefixSubTitle, book.getSubtitle()));
            bookPubDate.setText(String.format(prefixPubDate, book.getPubdate()));
            bookPages.setText(String.format(prefixPages, book.getPages()));
            bookPrice.setText(String.format(prefixPrice, book.getPrice()));

            Picasso.with(context)
                    .load(book.getImages().getLarge())
                    .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                    .into(bookImage);

        }

        @Override
        public void onClick(View v) {
            Log.e(HomeActivity.TAG, "==>Book onClick....Item");

            if (book == null) return;
            if (itemView == null) return;

            Context context = itemView.getContext();
            if (context == null) return;

            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("book", book);

            if (context instanceof Activity) {
                Activity activity = (Activity) context;

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, bookImage, "cover").toBundle();
                ActivityCompat.startActivity(activity, intent, bundle);
            }
        }
    }

}
