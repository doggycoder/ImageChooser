/*
 *
 * AlbumAdapter.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;
import com.wuwang.imagechooser.res.IChooseDrawable;

import java.util.ArrayList;

/**
 * Description:
 */
public class FolderAdapter extends BaseAdapter {

    private IChooseDrawable drawable;
    private Fragment fragment;
    public ArrayList<ImageInfo> data;

    public FolderAdapter(Fragment fragment, ArrayList<ImageInfo> data, IChooseDrawable drawable){
        this.fragment=fragment;
        this.data=data;
        this.drawable=drawable;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(fragment.getContext()).inflate(R.layout.item_image,parent,false);
            holder=new ImageHolder(convertView);
        }else{
            holder= (ImageHolder) convertView.getTag();
        }
        holder.setData(data.get(position));
        return convertView;
    }

    private class ImageHolder{
        ImageView mImage;
        View mFlag;
        ImageHolder(View convertView){
            mImage= (ImageView) convertView.findViewById(R.id.mImage);
            mFlag=convertView.findViewById(R.id.mFlag);
            convertView.setTag(this);
        }

        void setData(ImageInfo info){
            //图片加载
            DrawableRequestBuilder r=Glide.with(fragment).load(info.path)
                    .error(ChooserSetting.errorResId)
                    .placeholder(ChooserSetting.placeResId);
            if(ChooserSetting.loadAnimateResId<=0){
                r.dontAnimate();
            }else{
                r.animate(ChooserSetting.loadAnimateResId);
            }
            r.into(mImage);
            if(info.state<=0){
                mImage.setColorFilter(ChooserSetting.unChooseFilter);
            }else{
                mImage.setColorFilter(ChooserSetting.chooseFilter);
            }

            //状态加载
            if(drawable!=null){
                setBg(mFlag,drawable.get(info.state));
            }
        }

        private void setBg(View v, Drawable drawable){
            v.setBackgroundDrawable(null);
            v.setBackgroundDrawable(drawable);
        }
    }

}
