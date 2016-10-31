/*
 *
 * EntryActivity.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuwang.imagechooser.album.AlbumEntry;
import com.wuwang.imagechooser.album.AlbumFragment;

import java.util.ArrayList;

/**
 * Description:
 */
public class EntryActivity extends FragmentActivity implements AlbumEntry.IAlbumShower{

    private AlbumEntry entry;
    private TextView mAlbumNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        mAlbumNum= (TextView) findViewById(R.id.mAlbum);
        mAlbumNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.showAlbumChooser();
            }
        });
        entry=new AlbumEntry(this, R.id.mEntry, new AlbumFragment(), this);
    }


    @Override
    public void setAlbums(ArrayList albums) {

    }

    @Override
    public void show() {

    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isShowing() {
        return false;
    }
}
