package com.shrimpcolo.johnnytam.idouban.common;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Johnny Tam on 2016/7/18.
 */
public class ModelViewHolder<T> extends BaseViewHolder<T> {

    private IModelView<T> mItemModelView;

    public ModelViewHolder(@NonNull View itemView) {
        super(itemView);
        if (itemView instanceof IModelView) {
            mItemModelView = (IModelView<T>) itemView;
        }
    }

//    public ModelViewHolder(IModelView<T> itemModelView) {
//        super(itemModelView.getView());
//    }

    @Override
    public void updateItem(T itemContent) {
        if (mItemModelView != null) {
            mItemModelView.onBindItem(itemContent);
        }
    }
}