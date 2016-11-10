package com.wuwang.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuwang on 2016/11/4
 */

public class ShowAdapter extends BaseAdapter {

    public List<String> data=new ArrayList<>();

    public ShowAdapter(){
    }

    @Override
    public int getCount() {
        return data.size();
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
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_chooser_item_image,parent,false);
            ImageView iv= (ImageView) convertView.findViewById(R.id.mImage);
            Glide.with(parent.getContext()).load(data.get(position)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
            convertView.setTag(iv);
        }else{
            Glide.with(parent.getContext()).load(data.get(position)).into((ImageView) convertView.getTag());
        }
        return convertView;
    }
}
