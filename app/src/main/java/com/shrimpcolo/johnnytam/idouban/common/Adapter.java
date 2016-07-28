package com.shrimpcolo.johnnytam.idouban.common;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Adapter<T, VH extends ViewHolder<T>> extends RecyclerView.Adapter<VH> {
    List<T> data;
    ViewHolder.Builder<VH> builder;

    @LayoutRes
    int layoutResId;

    public Adapter(@NonNull List<T> data, @LayoutRes int layoutResId, ViewHolder.Builder<VH> builder) {
        this.data = data;
        this.layoutResId = layoutResId;
        this.builder = builder;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return builder.build(itemView);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (holder == null) return;
        holder.updateItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public T getItem(int pos) {
        return data.get(pos);
    }
}