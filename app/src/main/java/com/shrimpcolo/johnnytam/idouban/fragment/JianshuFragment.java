package com.shrimpcolo.johnnytam.idouban.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.activity.detail.WebViewActivity;
import com.shrimpcolo.johnnytam.idouban.activity.home.HomeActivity;
import com.shrimpcolo.johnnytam.idouban.entity.Blog;
import com.shrimpcolo.johnnytam.idouban.entity.BlogInfo;
import com.shrimpcolo.johnnytam.idouban.net.IShrimpColoService;
import com.shrimpcolo.johnnytam.idouban.net.ShrimpColoManager;
import com.shrimpcolo.johnnytam.idouban.ui.DividerItemDecoration;
import com.shrimpcolo.johnnytam.idouban.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class JianshuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Blog> mBlog = new ArrayList<>();

    public JianshuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        IShrimpColoService shrimpColoService = ShrimpColoManager.createShrimpService();
        shrimpColoService.getBlog()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BlogInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.e(HomeActivity.TAG, "onCompleted!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(HomeActivity.TAG, "onError: " + e.getMessage());
                        ToastUtil.getInstance().showToast(getContext(), getString(R.string.msg_error));
                    }

                    @Override
                    public void onNext(BlogInfo blog) {
                        Log.e(HomeActivity.TAG, "onNext!!! ");
                        mBlog = blog.getBlog();
                        Log.e(HomeActivity.TAG, "blog list size = " + mBlog.size());
                        BlogAdapter blogAdapter = new BlogAdapter(mBlog, R.layout.recyclerview_jianshu_item);
                        mRecyclerView.setAdapter(blogAdapter);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jianshu, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_blog);
        Log.e(HomeActivity.TAG, "===> onCreateView = " + this.getClass().getSimpleName());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        }
    }

    static class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

        private List<Blog> mBlogDataList;

        @LayoutRes
        private int layoutResId;

        public BlogAdapter(@NonNull List<Blog> list, @LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            mBlogDataList = list;
        }

        @Override
        public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
            return new BlogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {
            if (holder == null) return;

            holder.updateBlog(mBlogDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mBlogDataList.size();
        }
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mBlogTitle;
        private Blog mBlog;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mBlogTitle = (TextView) itemView.findViewById(R.id.txt_blog_title);
            mBlogTitle.setOnClickListener(this);
        }

        public void updateBlog(Blog blog) {
            mBlogTitle.setText(blog.getName());
            mBlog = blog;
        }

        @Override
        public void onClick(View v) {
            if (mBlog == null || itemView == null) return;

            Context context = itemView.getContext();
            if (context == null) return;

            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("website", mBlog.getUrl());
            context.startActivity(intent);
        }
    }

}