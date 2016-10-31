/*
 *
 * AlbumFragment.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;

import java.util.ArrayList;

/**
 * Description:
 */
public class AlbumFragment extends Fragment implements AlbumEntry.IFolderShower {

    private ViewGroup rootView;
    private GridView mGrid;
    private AlbumAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView= (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.fragment_album,container,false);
            initView();
            initData();
        }
        return rootView;
    }

    private void initView(){
        mGrid= (GridView) rootView.findViewById(R.id.mAlbum);
        mGrid.setNumColumns(ChooserSetting.NUM_COLUMNS);
    }

    private void initData(){
        adapter=new AlbumAdapter(this,getContext(),new ArrayList<ImageInfo>(),null);
        mGrid.setAdapter(adapter);

    }

    @Override
    public void setFolder(ImageFolder folder) {
        adapter.data.clear();
        adapter.data.addAll(folder.getDatas());
        adapter.notifyDataSetChanged();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
