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
import android.widget.AdapterView;
import android.widget.GridView;

import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;
import com.wuwang.imagechooser.abslayer.IAlpha;
import com.wuwang.imagechooser.abslayer.IImageClickListener;
import com.wuwang.imagechooser.res.CircleChooseDrawable;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Description:
 */
public class FolderFragment extends Fragment implements AlbumEntry.IFolderShower,IAlpha {

    private ViewGroup rootView;
    private GridView mGrid;
    private View mCover;
    private FolderAdapter adapter;
    private ArrayList<ImageInfo> data=new ArrayList<>();
    private Vector<ImageInfo> selectImgs;
    private IImageClickListener listener;
    private int maxSize=9;

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
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageInfo info=data.get(position);
                if(info.state>0){
                    boolean a=listener.onCancel(selectImgs,info);
                    if(!a){
                        info.state=0;
                        selectImgs.removeElement(info);
                    }
                }else{
                    boolean b=listener.onAdd(selectImgs,info);
                    if(!b){
                        info.state=selectImgs.size()+1;
                        selectImgs.add(info);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        mCover=rootView.findViewById(R.id.mCover);
    }

    private void initData(){
        adapter=new FolderAdapter(this,data,new CircleChooseDrawable());
        mGrid.setAdapter(adapter);
        selectImgs=new Vector<>();
    }

    @Override
    public void setFolder(ImageFolder folder) {
        if(data!=null){
            data.clear();
            data.addAll(folder.getDatas());
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setImageClickListener(IImageClickListener listener) {
        this.listener=listener;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setAlpha(int alpha) {
        mCover.setBackgroundColor(alpha);
    }

    @Override
    public void show() {
        mCover.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        mCover.setVisibility(View.GONE);
    }
}
