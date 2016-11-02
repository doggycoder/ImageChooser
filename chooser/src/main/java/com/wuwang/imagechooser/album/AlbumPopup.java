/*
 *
 * AlbumPopup.java
 * 
 * Created by Wuwang on 2016/11/2
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;
import com.wuwang.imagechooser.abslayer.IAlpha;

import java.util.ArrayList;

/**
 * Description:
 */
public class AlbumPopup implements AlbumEntry.IAlbumShower{

    private Activity context;
    private PopupWindow window;
    private View contentView;
    private ListView mList;
    private AlbumAdapter adapter;
    private ArrayList<ImageFolder> data;
    private IAlbumClickListener listener;
    private IAlpha alpha;
    private View anchor;

    public AlbumPopup(Activity context, View anchor, IAlpha iAlpha){
        this.context=context;
        this.anchor=anchor;
        this.alpha=iAlpha;
        createPopup();
    }

    private void createPopup(){
        contentView= LayoutInflater.from(context).inflate(R.layout.fragment_folder,null);
        window=new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                alpha.hide();
            }
        });
        window.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    window.dismiss();
                    return true;
                }
                return false;
            }
        });
        mList= (ListView) contentView.findViewById(R.id.mList);
        adapter=new AlbumAdapter(context,data=new ArrayList<>());
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onAlbumClick(data.get(position));
            }
        });
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    @Override
    public void setAlbums(ArrayList<ImageFolder> albums) {
        data.clear();
        data.addAll(albums);
        adapter.notifyDataSetChanged();
        mList.pointToPosition(0,0);
    }

    @Override
    public void setAlbumClickListener(IAlbumClickListener listener) {
        this.listener=listener;
    }

    @Override
    public void show() {
        alpha.show();
        mList.getLayoutParams().height= ChooserSetting.albumPopupHeight;
        mList.setLayoutParams(mList.getLayoutParams());
        window.showAsDropDown(anchor);
    }

    @Override
    public void cancel() {
        window.dismiss();
    }

}
