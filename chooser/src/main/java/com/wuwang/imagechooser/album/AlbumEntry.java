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
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.wuwang.utils.LogUtils;

import java.util.ArrayList;

/**
 * Description:
 */
public class AlbumEntry implements AlbumTool.Callback,IAlbumClickListener {

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
        albumShower.setAlbumClickListener(this);
        tool=new AlbumTool(activity);
        tool.setCallback(this);
        tool.findAlbumsAsync();
    }

    //显示相册选择器
    public void showAlbumChooser(){
        LogUtils.e("显示相册选择器");
        albumShower.show(activity.getSupportFragmentManager(),0);
    }

    //关闭相册选择器
    public void cancelAlbumChooser(){
        albumShower.cancel(activity.getSupportFragmentManager());
    }

    @Override
    public void onFolderFinish(ImageFolder folder) {
        LogUtils.e("图片集《"+folder.getName()+"》查找完毕");
        folderShower.setFolder(folder);
    }

    @Override
    public void onAlbumFinish(ArrayList<ImageFolder> albums) {
        LogUtils.e("相册查找完毕,共"+albums.size()+"个相册");
        albumShower.setAlbums(albums);
    }

    @Override
    public void onAlbumClick(ImageFolder folder) {
        tool.findFolderAsync(folder);
        cancelAlbumChooser();
    }

    public interface IAlbumShower{
        void setAlbums(ArrayList<ImageFolder> albums);
        void setAlbumClickListener(IAlbumClickListener listener);
        void show(FragmentManager manager,int tag);
        void cancel(FragmentManager manager);
        Fragment getFragment();
    }

    public interface IFolderShower{
        void setFolder(ImageFolder folder);
        Fragment getFragment();
    }

}
