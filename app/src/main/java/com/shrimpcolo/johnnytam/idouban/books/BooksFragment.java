package com.shrimpcolo.johnnytam.idouban.books;


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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.beans.Book;
import com.shrimpcolo.johnnytam.idouban.bookdetail.BookDetailActivity;
import com.shrimpcolo.johnnytam.idouban.utils.ConstContent;
import com.shrimpcolo.johnnytam.idouban.utils.EndlessRecyclerViewScrollListener;
import com.shrimpcolo.johnnytam.idouban.utils.ScrollChildSwipeRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.shrimpcolo.johnnytam.idouban.utils.ConstContent.MSG_LOADMORE_DATA;
import static com.shrimpcolo.johnnytam.idouban.utils.ConstContent.MSG_LOADMORE_UI_ADD;
import static com.shrimpcolo.johnnytam.idouban.utils.ConstContent.MSG_LOADMORE_UI_DELETE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment implements BooksContract.View {
    private static final String TAG = BooksFragment.class.getSimpleName();

    private BooksContract.Presenter mPresenter;

    private RecyclerView mBookRecyclerView;

    private View mNoBooksView;

    private BookAdapter mBookAdapter;

    private List<Book> mAdapterBooksData;

    private BooksHandle mHandler;

    class BooksHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADMORE_UI_ADD:
                    //Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_UI_ADD totalItem: " + msg.arg1);
                    mBookAdapter.mBooks.add(null);
                    mBookAdapter.notifyItemInserted(mBookAdapter.mBooks.size() - 1);

                    Message msgLoadMore = mHandler.obtainMessage(MSG_LOADMORE_DATA, msg.arg1, -1);
                    mHandler.sendMessage(msgLoadMore);
                    break;

                case MSG_LOADMORE_UI_DELETE:
                    Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_UI_DELETE : ");
                    mBookAdapter.mBooks.remove(mBookAdapter.mBooks.size() - 1);
                    mBookAdapter.notifyItemRemoved(mBookAdapter.mBooks.size());
                    break;

                case MSG_LOADMORE_DATA:
                    Log.e(HomeActivity.TAG, "==> MSG_LOADMORE_DATA load start index: " + msg.arg1);
                    mPresenter.loadMoreBooks(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    }


    public BooksFragment() {
        // Required empty public constructor
    }

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookAdapter = new BookAdapter(new ArrayList<>(0),
                R.layout.recyclerview_book_item, R.layout.recyclerview_loading_item);
        mAdapterBooksData = new ArrayList<>();
        mHandler = new BooksHandle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_books, container, false);
        mBookRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_books);
        mNoBooksView = view.findViewById(R.id.ll_no_books);

        //set recycle view
        mBookRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mBookRecyclerView.setLayoutManager(layoutManager);
        mBookRecyclerView.setAdapter(mBookAdapter);

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) view.findViewById(R.id.book_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(mBookRecyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            Log.e(HomeActivity.TAG, "\n\n onRefresh loadRefreshedBooks...");
            mPresenter.loadRefreshedBooks(true);
        });

        //set listener to recycle view
        mBookRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;//use Footer view
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(HomeActivity.TAG, " CallBack onLoadMore => page: " + page + ", books item count is " + totalItemsCount);

                final Message msg = mHandler.obtainMessage(MSG_LOADMORE_UI_ADD, totalItemsCount, -1);
                msg.sendToTarget();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mPresenter != null){
            mPresenter.start();
        }
    }

    @Override
    public void showRefreshedBooks(List<Book> books) {

        //If the refreshed data is a part of mAdapterMovieData, don't operate mMovieAdapter
        if(mAdapterBooksData.size() != 0
                && books.get(0).getId().equals(mAdapterBooksData.get(0).getId())) {
            return;
        }

        mAdapterBooksData.addAll(books);
        mBookAdapter.replaceData(mAdapterBooksData);

        Log.e(HomeActivity.TAG,  TAG + " showRefreshedMovies: \n" +
                "mAdapterMoviesData.size() =  " + mAdapterBooksData.size()
        + ", getMoviesList.size = " + books.size() + ", adapter's movies.size = " + mBookAdapter.mBooks.size());

        mBookRecyclerView.setVisibility(View.VISIBLE);
        mNoBooksView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadedMoreBooks(List<Book> books) {
        Log.e(HomeActivity.TAG,  TAG + " showLoadedMoreMovies 111 : \n" +
                "mAdapterMoviesData.size() =  " + mAdapterBooksData.size()
                + ", LoadMoreList.size = " + books.size() + ", adapter's movies.size = " + mBookAdapter.mBooks.size());

        mBookAdapter.mBooks.remove(mBookAdapter.mBooks.size() - 1);
        mBookAdapter.notifyItemRemoved(mBookAdapter.mBooks.size());

        mAdapterBooksData.addAll(books);
        mBookAdapter.replaceData(mAdapterBooksData);

        Log.e(HomeActivity.TAG,  TAG + " showLoadedMoreMovies 222 : \n" +
                "mAdapterMoviesData.size() =  " + mAdapterBooksData.size()
                + ", LoadMoreList.size = " + books.size() + ", adapter's movies.size = " + mBookAdapter.mBooks.size());

    }

    @Override
    public void showNoBooks() {
        mBookRecyclerView.setVisibility(View.GONE);
        mNoBooksView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoLoadedMoreBooks() {
        Log.e(HomeActivity.TAG, "\n\n showNoLoadedMoreBooks");
        mHandler.sendEmptyMessageDelayed(MSG_LOADMORE_UI_DELETE, 500);
    }

    @Override
    public void setRefreshedIndicator(boolean active) {
        if(getView() == null) return;

        Log.e(HomeActivity.TAG, TAG + "=> loading indicator: " + active);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.book_refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        swipeRefreshLayout.post(() -> {
            Log.e(HomeActivity.TAG, "swipeRefreshLayout run() active: " + active);
            swipeRefreshLayout.setRefreshing(active);
        });
    }

    @Override
    public void setPresenter(BooksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    static class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Book> mBooks;

        @LayoutRes
        private int mLayoutItemViewResId;
        @LayoutRes
        private int mLayoutLoadingResId;

        public BookAdapter(@NonNull List<Book> books,
                           @LayoutRes int layoutItemViewId, @LayoutRes int layoutLoadingResId ){
            setList(books);
            this.mLayoutItemViewResId = layoutItemViewId;
            this.mLayoutLoadingResId = layoutLoadingResId;
        }

        @Override
        public int getItemViewType(int position) {
            return mBooks.get(position) == null ? ConstContent.VIEW_TYPE_LOADING : ConstContent.VIEW_TYPE_ITEM;
        }

        private void setList(List<Book> books){
            mBooks = checkNotNull(books);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder viewHolder;

            if(ConstContent.VIEW_TYPE_ITEM == viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutItemViewResId, parent, false);
                viewHolder = new BookViewHolder(itemView);
            }else {
                View loadingView = LayoutInflater.from(parent.getContext()).inflate(mLayoutLoadingResId, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder == null) return;

            if(holder instanceof BookViewHolder) {
                ((BookViewHolder) holder).updateBook(mBooks.get(position));

            }else if(holder instanceof LoadingViewHolder){
                ((LoadingViewHolder) holder).updateLoading();
            }
        }

        @Override
        public int getItemCount() {
            return  mBooks == null ? 0 : mBooks.size();
        }

        public void replaceData(List<Book> books) {
            setList(books);
            notifyDataSetChanged();
        }
    }

    static class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            intent.putExtra(ConstContent.INTENT_EXTRA_BOOK, book);

            if (context instanceof Activity) {
                Activity activity = (Activity) context;

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, bookImage, "cover").toBundle();
                ActivityCompat.startActivity(activity, intent, bundle);
            }
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progress_bar);
        }

        void updateLoading(){
            progressBar.setIndeterminate(true);
        }

        void updateLoadingTitle() {

        }

    }

}
