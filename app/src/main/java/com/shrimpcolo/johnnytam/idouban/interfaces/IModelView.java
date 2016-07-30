package com.shrimpcolo.johnnytam.idouban.interfaces;

/**
 * Created by Johnny Tam on 2016/7/28.
 */
public interface IModelView<T> {
    void onBindItem(T item);
//    View getView();//not used
}
