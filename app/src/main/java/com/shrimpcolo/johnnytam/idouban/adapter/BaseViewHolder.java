package com.shrimpcolo.johnnytam.idouban.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Johnny Tam on 2016/7/18.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void updateItem(T itemContent);

    public interface Builder<VH> {
        VH build(View itemView);
    }
}