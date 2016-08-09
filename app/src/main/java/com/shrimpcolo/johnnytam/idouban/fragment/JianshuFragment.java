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
import com.shrimpcolo.johnnytam.idouban.ui.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class JianshuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<HashMap<String, String>> titleList = new ArrayList<>();

    public JianshuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init the raw data
        String[] title = {
                "RecyclerView的重构之路(一)",
                "RecyclerView的重构之路(二)",
                "RecyclerView的重构之路(三)",
                "RecyclerView的重构之路(四)",
                "RecyclerView的重构之路(五)",
                "RecyclerView的重构之路(六)",
                "RecyclerView的重构之路(七)",
                "RecyclerView的重构之路(八)",
                "又一个寡头时代的来临"
        };
        String[] url = {
                "http://www.jianshu.com/p/99cd83778373",
                "http://www.jianshu.com/p/81270f444a6f",
                "http://www.jianshu.com/p/a5d60fa90d17",
                "http://www.jianshu.com/p/5eac09f5bb92",
                "http://www.jianshu.com/p/c4a8bfbe1f40",
                "http://www.jianshu.com/p/b1ea462f5602",
                "http://www.jianshu.com/p/9665955103c0",
                "http://www.jianshu.com/p/98399b00ae78",
                "http://www.jianshu.com/p/e9f4736fa08a"
        };

        //init the data
        for (int i = 0; i < title.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", title[i]);
            map.put("url", url[i]);
            titleList.add(map);
        }

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

            BlogAdapter blogAdapter = new BlogAdapter(titleList, R.layout.recyclerview_jianshu_item);
            mRecyclerView.setAdapter(blogAdapter);
        }
    }

    static class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

        private List<HashMap<String, String>> mBlogDataList;

        @LayoutRes
        private int layoutResId;
        public BlogAdapter(@NonNull List<HashMap<String, String>> list, @LayoutRes int layoutResId) {
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
            if(holder == null) return;

            holder.updateBlog(mBlogDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mBlogDataList.size();
        }
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mBlogTitle;
        private HashMap<String, String> blogMap;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mBlogTitle = (TextView) itemView.findViewById(R.id.txt_blog_title);
            mBlogTitle.setOnClickListener(this);
        }

        public void updateBlog(HashMap<String, String> blog) {
            mBlogTitle.setText(blog.get("title"));
            blogMap = blog;
        }

        @Override
        public void onClick(View v) {
            if(blogMap == null || itemView == null) return;

            Context context = itemView.getContext();
            if(context == null) return;

            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("website", blogMap.get("url"));
            context.startActivity(intent);
        }
    }

}