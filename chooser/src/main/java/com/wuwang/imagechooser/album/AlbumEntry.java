/*
 *
 * AlbumEntry.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 * Description:
 */
public class AlbumEntry implements AlbumTool.Callback {

    private FragmentActivity activity;
    private int containerId;
    private IFolderShower folderShower;
    private IAlbumShower albumShower;

    private AlbumTool tool;

    public AlbumEntry(FragmentActivity activity,int containerId,IFolderShower fShower,IAlbumShower aShower){
        this.activity=activity;
        this.containerId=containerId;
        this.folderShower=fShower;
        this.albumShower=aShower;
        init();
    }

    private void init(){
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(containerId,folderShower.getFragment())
                .commitAllowingStateLoss();
        tool=new AlbumTool(activity);
        tool.setCallback(this);
        tool.findAlbumsAsync();
    }

    public void swithAlbumChooser(){
        if(albumShower.isShowing()){
            albumShower.cancel();
        }else{
            albumShower.show();
        }
    }

    //显示相册选择器
    public void showAlbumChooser(){
        albumShower.show();
    }

    //关闭相册选择器
    public void cancelAlbumChooser(){
        albumShower.cancel();
    }

    @Override
    public void onFolderFinish(ImageFolder folder) {
        folderShower.setFolder(folder);
    }

    @Override
    public void onAlbumFinish(ArrayList<ImageFolder> albums) {
        albumShower.setAlbums(albums);
    }

    public interface IAlbumShower{
        void setAlbums(ArrayList albums);
        void show();
        void cancel();
        boolean isShowing();
    }

    public interface IFolderShower{
        void setFolder(ImageFolder folder);
        Fragment getFragment();
    }

}
