/*
 *
 * IChooseDrawable.java
 * 
 * Created by Wuwang on 2016/10/31
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.res;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Description:
 */
public abstract class IChooseDrawable extends Drawable {

    private Paint paint;
    private int state;

    public IChooseDrawable(int state){
        this.state=state;
        paint=new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        if(state==0){
            drawUnChoose(canvas,paint);
        }else{
            drawChoose(canvas,paint,state);
        }
    }

    public void setState(int state){
        this.state=state;
    }

    public abstract void drawUnChoose(Canvas canvas,Paint paint);
    public abstract void drawChoose(Canvas canvas,Paint paint,int state);

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
