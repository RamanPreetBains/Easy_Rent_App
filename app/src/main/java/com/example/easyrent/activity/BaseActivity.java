package com.example.easyrent.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initViews();
        initViewsWithData();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initViewsWithData();
}
