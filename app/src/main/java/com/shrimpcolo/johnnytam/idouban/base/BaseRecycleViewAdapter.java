package com.shrimpcolo.johnnytam.idouban.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewHolder.BuilderItemViewHolder;
import com.shrimpcolo.johnnytam.idouban.base.BaseRecycleViewHolder.BuilderLoadingViewHolder;
import com.shrimpcolo.johnnytam.idouban.utils.AppConstants;
import static com.google.common.base.Preconditions.checkNotNull;

public class BaseRecycleViewAdapter<T, VH extends BaseRecycleViewHolder<T>> extends RecyclerView.Adapter<VH> {

    List<T> mData;
    BuilderItemViewHolder<VH> mBuilderItemViewHolder;
    BuilderLoadingViewHolder<VH> mBuilderLoadingViewHolder;

    @LayoutRes
    private int mLayoutItemViewResId;
    @LayoutRes
    private int mLayoutLoadingResId;

    public BaseRecycleViewAdapter(@NonNull List<T> data, @LayoutRes int layoutItemViewResId, @LayoutRes int layoutLoadingResId,
                                  @NonNull BuilderItemViewHolder<VH> itemBuilder, @NonNull BuilderLoadingViewHolder<VH> loadingBuilder ) {
        setList(data);
        this.mLayoutItemViewResId = layoutItemViewResId;
        this.mLayoutLoadingResId = layoutLoadingResId;

        this.mBuilderItemViewHolder = itemBuilder;
        this.mBuilderLoadingViewHolder = loadingBuilder;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? AppConstants.VIEW_TYPE_LOADING : AppConstants.VIEW_TYPE_ITEM;
    }

    private void setList(List<T> movies) {
        mData =  checkNotNull(movies);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        if(AppConstants.VIEW_TYPE_ITEM == viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayoutItemViewResId, parent, false);
            return mBuilderItemViewHolder.build(itemView);

        }else {
            View loadingView = LayoutInflater.from(parent.getContext()).inflate(mLayoutLoadingResId, parent, false);
            return mBuilderLoadingViewHolder.build(loadingView);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (holder == null) return;

        holder.updateItem(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void replaceData(List<T> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public List<T> getData(){
        return mData;
    }
}