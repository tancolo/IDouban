package com.shrimpcolo.johnnytam.idouban.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.shrimpcolo.johnnytam.idouban.R;
import com.shrimpcolo.johnnytam.idouban.utils.DataBindingUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Johnny Tam on 2016/7/18.
 */
public class RxModelAdapter<T> extends ModelAdapter<T> {

    public RxModelAdapter(@NonNull Observable<List<T>> dataSource, @LayoutRes int layoutResId) {
        super(new ArrayList<>(), layoutResId);
        DataBindingUtils.bind(dataSource, this);
    }
}