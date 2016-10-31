/*
 *
 * AlbumVar.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.album;

/**
 * Description:
 */
public class AlbumVar {

    private static AlbumVar instance;
    private int goal;
    private int now;

    private AlbumVar(){

    }

    public static AlbumVar getInstance(){
        if(null==instance){
            synchronized (AlbumVar.class){
                if(null==instance){
                    instance=new AlbumVar();
                }
            }
        }
        return instance;
    }

    public void init(int goal,int now){
        this.goal=goal;
        this.now=now;
    }

    public void nowAdd(){

    }

    public void nowDec(){

    }

}
