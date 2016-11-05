/*
 *
 * CropActivity.java
 * 
 * Created by Wuwang on 2016/11/5
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.crop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wuwang.imagechooser.IcFinal;
import com.wuwang.imagechooser.R;
import com.wuwang.utils.LogUtils;

/**
 * Description:
 */
public class CropActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_crop);
        LogUtils.e("wuang",getIntent().getStringExtra(IcFinal.INTENT_CROP_DATA));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mContainer,CropFragment.newFragment(
                        getIntent().getStringExtra(IcFinal.INTENT_CROP_DATA),
                        CropPath.SHAPE_CIRCLE,
                        500,500))
                .commit();
    }
}
