package com.shrimpcolo.johnnytam.idouban.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;
import com.shrimpcolo.johnnytam.idouban.adapter.BaseViewHolder.Builder;
/**
 * Created by Johnny Tam on 2016/7/18.
 */
public class ModelAdapter<T> extends BaseAdapter<T, ModelViewHolder<T>> {

    public ModelAdapter(@NonNull List<T> data, @LayoutRes int layoutResId) {
        super(data, layoutResId, new Builder<ModelViewHolder<T>>() {
            @Override
            public ModelViewHolder<T> build(View itemView) {
                return new ModelViewHolder<>(itemView);
            }
        });
    }
}