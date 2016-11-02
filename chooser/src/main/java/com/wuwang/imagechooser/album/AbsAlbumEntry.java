/*
 *
 * AbsAlbumEntry.java
 * 
 * Created by Wuwang on 2016/11/2
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

/**
 * Description:
 */
public abstract class AbsAlbumEntry {

    private int max=9;
    private int min=1;
    private boolean isCrop;

    public void setMax(int max){
        this.max=max;
    }

    public void setMin(int min){
        this.min=min;
    }

    public void setCrop(boolean isCrop){
        this.isCrop=isCrop;
    }

    public int getMax(){
        return this.max;
    }

    public int getMin(){
        return this.min;
    }

    public boolean isCrop(){
        return this.isCrop;
    }

}
