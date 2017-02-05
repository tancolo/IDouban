package com.shrimpcolo.johnnytam.idouban.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;
import com.shrimpcolo.johnnytam.idouban.adapter.BaseViewHolder.Builder;
import com.shrimpcolo.johnnytam.idouban.interfaces.IModelView;

/**
 * Created by Johnny Tam on 2016/7/18.
 */
public class ModelAdapter<T> extends BaseAdapter<T, ModelViewHolder<T>> implements IModelView<List<T>> {

    public ModelAdapter(@NonNull List<T> data, @LayoutRes int layoutResId) {
        super(data, layoutResId, ModelViewHolder::new);
    }

    @Override
    public void onBindItem(List<T> item) {
        setData(item);
    }
}