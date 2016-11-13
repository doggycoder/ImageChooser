package com.wuwang.app;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;

import com.wuwang.imagechooser.crop.ACropCoverDrawable;

/**
 * Created by wuwang on 2016/11/13
 */

public class ExampleCropPath extends ACropCoverDrawable{

    Paint paint;
    private Rect rect;
    private Path cropPath;

    public ExampleCropPath(int param){
        super(0);
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x88000000);
    }

    @Override
    public void draw(Canvas canvas) {
        int cWidth=canvas.getWidth();
        int cHeight=canvas.getHeight();
        if(rect==null){
            rect=new Rect(cWidth/6,cHeight/2-cWidth/3,cWidth*5/6,cHeight/2+cWidth/3);
        }
        canvas.drawColor(Color.TRANSPARENT);
        Path path=new Path();
        path.addRect(0,0,cWidth,cHeight, Path.Direction.CW);
        cropPath=new Path();
        cropPath.moveTo(rect.left,rect.top+rect.height()/4);
        cropPath.lineTo(rect.left+rect.width()/4,rect.top);
        cropPath.lineTo(rect.right-rect.width()/4,rect.top);
        cropPath.lineTo(rect.right,rect.top+rect.height()/4);
        cropPath.lineTo(rect.right,rect.centerY());
        cropPath.quadTo(rect.right,rect.bottom,rect.centerX(),rect.bottom);
        cropPath.quadTo(rect.left,rect.bottom,rect.left,rect.centerY());
        cropPath.close();
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
