/*
 *
 * EntryActivity.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.wuwang.imagechooser.album.AlbumEntry;
import com.wuwang.imagechooser.album.AlbumPopup;
import com.wuwang.imagechooser.album.FolderFragment;
import com.wuwang.imagechooser.album.ImageFolder;

/**
 * Description:
 */
public class EntryActivity extends FragmentActivity{

    private AlbumEntry entry;
    private TextView mAlbumNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_entry);
        Rect outRect=new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        ChooserSetting.albumPopupHeight=(int) (outRect.height()*0.6f);
        mAlbumNum= (TextView) findViewById(R.id.mAlbum);
        mAlbumNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.showAlbumChooser();
            }
        });
        FolderFragment m=new FolderFragment();
        entry=new AlbumEntry(this, R.id.mEntry, m, new AlbumPopup(this,mAlbumNum,m)){
            @Override
            public void onAlbumClick(ImageFolder folder) {
                super.onAlbumClick(folder);
                mAlbumNum.setText(folder.getName()+"("+folder.getCount()+")");
            }
        };
    }
}
