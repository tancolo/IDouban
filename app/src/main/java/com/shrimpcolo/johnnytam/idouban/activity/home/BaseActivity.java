package com.shrimpcolo.johnnytam.idouban.activity.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Johnny Tam on 2016/7/30.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initViews(savedInstanceState);
    }

    protected abstract void initVariables();

    protected abstract void initViews(Bundle savedInstanceState);
}
