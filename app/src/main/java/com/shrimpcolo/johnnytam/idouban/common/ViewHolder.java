package com.shrimpcolo.johnnytam.idouban.common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
    protected T itemContent;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void updateItem(T itemContent) {
        this.itemContent = itemContent;
        onBindItem(itemContent);
    }

    protected abstract void onBindItem(T itemContent);

    public interface Builder<VH> {
        VH build(View itemView);
    }
}