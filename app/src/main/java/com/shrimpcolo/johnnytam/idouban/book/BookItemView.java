package com.shrimpcolo.johnnytam.idouban.book;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.common.IModelView;
import com.squareup.picasso.Picasso;

/**
 * Created by Johnny Tam on 2016/7/28.
 */
public class BookItemView  extends CardView implements View.OnClickListener, IModelView<Book>{

    private ImageView bookImage;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookSubTitle;
    private TextView bookPubDate;
    private TextView bookPages;
    private TextView bookPrice;

    private Book mBook;

    public BookItemView(Context context) {
        super(context);
    }

    public BookItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bookImage = (ImageView) findViewById(R.id.book_cover);
        bookTitle = (TextView) findViewById(R.id.txt_title);
        bookAuthor = (TextView) findViewById(R.id.txt_author);
        bookSubTitle = (TextView) findViewById(R.id.txt_subTitle);
        bookPubDate = (TextView) findViewById(R.id.txt_pubDate);
        bookPrice = (TextView) findViewById(R.id.txt_prices);
        bookPages = (TextView) findViewById(R.id.txt_pages);

        setOnClickListener(this);
    }

    @Override
    public void onBindItem(Book book) {
        mBook = book;
        Context context = getContext();
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

//    @Override
//    public View getView() {
//        return this;
//    }

    @Override
    public void onClick(View v) {
        Log.e(HomeActivity.TAG, "==>onClick....Book Item");

        if (mBook == null) return;

        Context context = getContext();
        if (context == null) return;

        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("book", mBook);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, bookImage, "cover").toBundle();
            ActivityCompat.startActivity(activity, intent, bundle);
        }
    }
}
