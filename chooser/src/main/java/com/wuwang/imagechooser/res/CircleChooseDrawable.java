/*
 *
 * CircleChooseDrawable.java
 * 
 * Created by Wuwang on 2016/11/2
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.imagechooser.res;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wuwang.utils.LogUtils;

/**
 * Description:
 */
public class CircleChooseDrawable extends IChooseDrawable {

    public CircleChooseDrawable(){
        super();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int state) {
        width=canvas.getWidth();
        height=canvas.getHeight();
        if(state==0){
            paint.setColor(0x55000000);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
            paint.setColor(0xDDFFFFFF);
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
        }else{
            paint.setColor(0xFFFF8800);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2,height/2,width/2-2,paint);
        }
    }

}
