package com.wuwang.imagechooser.crop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by wuwang on 2016/10/25
 */

public class CropCoverDrawable extends ICropCoverDrawable {

    private Paint paint;
    private Rect rect;
    private int width,height;
    private int shape;
    private Path cropPath;

    public CropCoverDrawable(int width,int height){
        this.width=width;
        this.height=height;
        paintInit();
    }

    public CropCoverDrawable(int shape,int x,int y,int width,int height){
        this.shape=shape;
        rect=new Rect(x-width/2,y-height/2,x+width/2,y+height/2);
        paintInit();
    }

    private void paintInit(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x88000000);
    }

    public CropCoverDrawable setShape(int shape){
        this.shape=shape;
        return this;
    }

    public void setShadowColor(int color){
        paint.setColor(color);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int cWidth=canvas.getWidth();
        int cHeight=canvas.getHeight();
        if(rect==null){
            rect=new Rect(cWidth/2-width/2,cHeight/2-height/2,cWidth/2+width/2,cHeight/2+height/2);
        }
        canvas.drawColor(Color.TRANSPARENT);
        Path path=new Path();
        path.addRect(0,0,cWidth,cHeight, Path.Direction.CW);
        cropPath=new Path();
        if(shape==SHAPE_RECT){
            cropPath.addRect(rect.left,rect.top,rect.right,rect.bottom, Path.Direction.CW);
        }else if(shape==SHAPE_CIRCLE){
            cropPath.addCircle(rect.centerX(),rect.centerY(),rect.width()/2, Path.Direction.CW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            path.op(cropPath, Path.Op.DIFFERENCE);        //可以抗锯齿
            canvas.drawPath(path,paint);
        }else{
            //此方法可以去掉锯齿
            //在这里saveLayer然后restoreToCount的操作不能少，否则不会得到想要的效果
            int layerId = canvas.saveLayer(0, 0, cWidth, cHeight, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawPath(path,paint);
            //已经绘制的可以看做为目标图
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(cropPath,paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);
            //裁剪的方式会有锯齿，没找到方法去掉锯齿
            //canvas.clipPath(opPath, Region.Op.DIFFERENCE);
            //canvas.drawRect(0,0,cWidth,cHeight,paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public Rect limit() {
        return rect;
    }

    @Override
    public Path path() {
        return cropPath;
    }
}
