package com.shrimpcolo.johnnytam.idouban.utils;

import android.util.Log;

import com.shrimpcolo.johnnytam.idouban.interfaces.IModelView;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Johnny Tam on 2016/10/30.
 */

public class DataBindingUtils {
    public static <T> void bind(final Observable<T> dataSource, final IModelView<T> view) {
        dataSource.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        data -> view.onBindItem(data),
                        throwable -> Log.e("TANHQ", "error: " + throwable),
                        () -> {});
    }
}
