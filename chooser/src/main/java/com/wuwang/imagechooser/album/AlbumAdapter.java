/*
 *
 * FolderAdapter.java
 * 
 * Created by Wuwang on 2016/11/1
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Description:
 */
public class AlbumAdapter extends BaseAdapter {

    private ArrayList<ImageFolder> data;
    private Context context;

    public AlbumAdapter(Context context, ArrayList<ImageFolder> data){
        this.context=context;
        this.data=data;
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
        FolderHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.image_chooser_item_album,parent,false);
            holder=new FolderHolder(convertView);
        }else{
            holder= (FolderHolder) convertView.getTag();
        }
        holder.setData(data.get(position));
        return convertView;
    }

    private class FolderHolder{
        ImageView mImage;
        TextView mInfo;
        FolderHolder(View view){
            mImage= (ImageView) view.findViewById(R.id.mImage);
            mInfo= (TextView) view.findViewById(R.id.mInfo);
            view.setTag(this);
        }

        void setData(ImageFolder folder){
            //图片加载
            DrawableRequestBuilder r= Glide.with(context).load(folder.getFirstImagePath())
                    .error(ChooserSetting.errorResId)
                    .placeholder(ChooserSetting.placeResId);
            if(ChooserSetting.loadAnimateResId<=0){
                r.dontAnimate();
            }else{
                r.animate(ChooserSetting.loadAnimateResId);
            }
            r.into(mImage);
            mInfo.setText(String.format(Locale.CHINA,"%1$s（%2$d）",folder.getName(),folder.getCount()));
        }

    }

}
