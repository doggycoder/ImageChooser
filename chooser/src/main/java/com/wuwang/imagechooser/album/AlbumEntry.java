/*
 *
 * AlbumEntry.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.IcFinal;
import com.wuwang.imagechooser.abslayer.IImageClickListener;
import com.wuwang.imagechooser.res.IChooseDrawable;
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

    private final int REQ_CROP=0x10;

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

        Intent intent=activity.getIntent();
        setCrop(intent.getBooleanExtra(IcFinal.INTENT_IS_CROP,false));
        if(isCrop()){
            setMax(1);
        }else{
            setMax(intent.getIntExtra(IcFinal.INTENT_MAX_IMG,getMax()));
        }
        if(getMax()==1){
            this.folderShower.setChooseDrawable(null);
        }else{
            this.folderShower.setChooseDrawable(ChooserSetting.chooseDrawable);
        }
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
        if(getMax()==1){
            if(isCrop()){
                Intent intent=new Intent(IcFinal.ACTION_CROP);
                intent.putExtra(IcFinal.INTENT_CROP_DATA,info.path);
                activity.startActivityForResult(intent,REQ_CROP);
            }else{
                Intent intent=new Intent();
                ArrayList<String> result=new ArrayList<>();
                result.add(info.path);
                intent.putExtra(IcFinal.RESULT_DATA_IMG,result);
                activity.setResult(Activity.RESULT_OK,intent);
                activity.finish();
            }
            return true;
        }else{
            if(data.size()==getMax()){
                Toast.makeText(activity,"无法继续选中",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void chooseFinish(){
        List<ImageInfo> res=folderShower.getSelectedImages();
        int resSize=res.size();
        if(resSize>0){
            ArrayList<String> data=new ArrayList<>(resSize);
            for (int i=0;i<resSize;i++){
                data.add(res.get(i).path);
            }
            chooseFinish(data);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==REQ_CROP){
                chooseFinish(data.getStringExtra(IcFinal.RESULT_DATA_IMG));
            }
        }
    }

    private void chooseFinish(ArrayList<String> data){
        if(data.size()>0){
            Intent intent=new Intent();
            intent.putExtra(IcFinal.RESULT_DATA_IMG,data);
            activity.setResult(Activity.RESULT_OK,intent);
            activity.finish();
        }
    }

    private void chooseFinish(String data){
        if(data!=null){
            ArrayList<String> c=new ArrayList<>(1);
            c.add(data);
            chooseFinish(c);
        }
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
        void setChooseDrawable(IChooseDrawable drawable);
        void setFolder(ImageFolder folder);
        void setImageClickListener(IImageClickListener listener);
        List<ImageInfo> getSelectedImages();
        Fragment getFragment();
    }

}
