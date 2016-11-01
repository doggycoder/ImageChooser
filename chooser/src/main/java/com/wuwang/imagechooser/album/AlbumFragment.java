/*
 *
 * AlbumPopup.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wuwang.imagechooser.R;

import java.util.ArrayList;

/**
 * Description:
 */
public class AlbumFragment extends DialogFragment implements AlbumEntry.IAlbumShower,
        AdapterView.OnItemClickListener {

    private View rootView;
    private ListView mList;
    private AlbumAdapter adapter;
    private ArrayList<ImageFolder> data=new ArrayList<>();
    private IAlbumClickListener clickListener;
    private final String TAG_DIALOG="tag_dialog";
    private final String STACK_DIALOG="stack_dialog";
    private int showTag=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=LayoutInflater.from(getContext()).inflate(R.layout.fragment_folder,container,false);
            initView();
            initData();
        }
        return rootView;
    }

    private void initView(){
        mList= (ListView) rootView.findViewById(R.id.mList);
    }

    private void initData(){
        adapter=new AlbumAdapter(this,data);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void setAlbums(ArrayList<ImageFolder> albums) {
        if(data!=null){
            data.clear();
            data.addAll(albums);
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setAlbumClickListener(IAlbumClickListener listener) {
        this.clickListener=listener;
    }

    @Override
    public void show(FragmentManager manager,int tag) {
        this.showTag=tag;
        if(tag<=0){
            manager.beginTransaction()
                    .add(this,TAG_DIALOG)
                    .addToBackStack(STACK_DIALOG)
                    .commit();
        }else{
            manager.beginTransaction()
                    .add(tag,this)
                    .addToBackStack(STACK_DIALOG)
                    .commit();
        }
    }

    @Override
    public void cancel(FragmentManager manager) {
        if(showTag<=0){
            dismiss();
        }else{
            manager.popBackStack();
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageFolder folder=data.get(position);
        if(clickListener!=null){
            clickListener.onAlbumClick(folder);
        }
    }
}
