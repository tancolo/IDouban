package com.shrimpcolo.johnnytam.idouban.books;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.mobileapi.IDoubanService;
import com.shrimpcolo.johnnytam.idouban.entity.Book;
import com.shrimpcolo.johnnytam.idouban.entity.BooksInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Johnny Tam on 2017/3/21.
 */

public class BooksPresenter implements BooksContract.Presenter{

    private static final String TAG = BooksPresenter.class.getSimpleName();

    private BooksContract.View mBookView;

    private final IDoubanService mIDuobanService;

    private boolean mFirstLoad = true;

    private int mBookTotal;

    public BooksPresenter(@NonNull IDoubanService booksService, @NonNull BooksContract.View bookFragment) {
        mIDuobanService = booksService;
        mBookView = bookFragment;

        mBookView.setPresenter(this);
    }

    @Override
    public void loadRefreshedBooks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadBooks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    @Override
    public void start() {
        loadRefreshedBooks(false);
    }

    private void loadBooks(boolean forceUpdate, final boolean showLoadingUI){
        //BooksFragment需要显示Loading 界面
        if(showLoadingUI) mBookView.setRefreshedIndicator(true);

        if(forceUpdate){
            mIDuobanService.searchBooks("黑客与画家", 0).enqueue(new Callback<BooksInfo>() {
                @Override
                public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {

                    List<Book> booksList = response.body().getBooks();
                    mBookTotal = response.body().getTotal();
                    //debug
                    Log.e(HomeActivity.TAG, "===> Search Book: Response, size = " + booksList.size() + ", total = " + mBookTotal
                            + " showLoadingUI: " + showLoadingUI);

                    //获取数据成功，Loading UI消失
                    if(showLoadingUI) {
                        mBookView.setRefreshedIndicator(false);
                    }

                    processBooks(response.body().getBooks());
                }

                @Override
                public void onFailure(Call<BooksInfo> call, Throwable t) {
                    Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                            + Thread.currentThread().getId() + ", Error: " + t.getMessage());

                    //获取数据成功，Loading UI消失
                    if(showLoadingUI) {
                        mBookView.setRefreshedIndicator(false);
                    }
                    processEmptyBooks();
                }
            });
        }
    }

    @Override
    public void loadMoreBooks(int bookStartIndex) {
        loadMoreBooks(bookStartIndex, true);
    }

    //showLoadingMoreUI is not used, for future.
    private void loadMoreBooks(int bookStartIndex, final boolean showLoadingMoreUI) {

        Log.e(HomeActivity.TAG, TAG + ", bookStartIndex: " + bookStartIndex + ", mBookTotal: " + mBookTotal);
        //If user scroll down to last item, the app should not send network request.Just show the loading UI
        if(bookStartIndex >= mBookTotal) {
            processLoadMoreEmptyBooks();
            return;
        }

        mIDuobanService.searchBooks("黑客与画家", bookStartIndex).enqueue(new Callback<BooksInfo>() {
            @Override
            public void onResponse(Call<BooksInfo> call, Response<BooksInfo> response) {

                List<Book> loadMoreList = response.body().getBooks();
                //debug
                Log.e(HomeActivity.TAG, "===> Load More Book: Response, size = " + loadMoreList.size());

                processLoadMoreMovies(response.body().getBooks());
            }

            @Override
            public void onFailure(Call<BooksInfo> call, Throwable t) {
                Log.d(HomeActivity.TAG, "===> onFailure: Thread.Id = "
                        + Thread.currentThread().getId() + ", Error: " + t.getMessage());

                processLoadMoreEmptyBooks();
            }
        });
    }

    private void processBooks(List<Book> books) {

        if (books.isEmpty()) processEmptyBooks();// Show a message indicating there are no movies for users
        else mBookView.showRefreshedBooks(books); // Show the list of books
    }

    private void processEmptyBooks() {
        //BooksFragment需要给出提示
        mBookView.showNoBooks();
    }

    private void processLoadMoreMovies(List<Book> books){

        if (books.isEmpty()) processLoadMoreEmptyBooks();
        else mBookView.showLoadedMoreBooks(books);
    }

    private void processLoadMoreEmptyBooks() {
        mBookView.showNoLoadedMoreBooks();
    }

}
