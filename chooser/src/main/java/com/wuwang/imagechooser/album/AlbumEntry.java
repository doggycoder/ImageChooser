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
import android.view.View;
import android.widget.Toast;

import com.wuwang.imagechooser.abslayer.IAlpha;
import com.wuwang.imagechooser.abslayer.IImageClickListener;
import com.wuwang.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 */
public class AlbumEntry extends AbsAlbumEntry implements AlbumTool.Callback,IAlbumClickListener,IImageClickListener {

    private FragmentActivity activity;
    private int containerId;
    private IFolderShower folderShower;
    private IAlbumShower albumShower;

    private AlbumTool tool;

    public AlbumEntry(FragmentActivity activity, int containerId, IFolderShower fShower, IAlbumShower aShower){
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
        folderShower.setImageClickListener(this);
        tool=new AlbumTool(activity);
        tool.setCallback(this);
        tool.findAlbumsAsync();
    }

    //显示相册选择器
    public void showAlbumChooser(){
        LogUtils.e("显示相册选择器");
        albumShower.show();
    }

    //关闭相册选择器
    public void cancelAlbumChooser(){
        albumShower.cancel();
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

    @Override
    public boolean onAdd(List<ImageInfo> data, ImageInfo info) {
        if(data.size()==getMax()){
            Toast.makeText(activity,"无法继续选中",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCancel(List<ImageInfo> data, ImageInfo info) {
        return false;
    }

    public interface IAlbumShower{
        void setAlbums(ArrayList<ImageFolder> albums);
        void setAlbumClickListener(IAlbumClickListener listener);
        void show();
        void cancel();
    }

    public interface IFolderShower{
        void setFolder(ImageFolder folder);
        void setImageClickListener(IImageClickListener listener);
        Fragment getFragment();
    }

}
