package com.wuwang.imagechooser.crop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Description:
 */
public class CropHelper implements View.OnTouchListener {

    private static final String TAG = "Touch";

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Rect limit;

    // We can be in one of these 3 states
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private int mode = NONE;

    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private ImageView imageView;
    private CropPath cropPath;

    public CropHelper(Rect limit){
        this.limit=limit;
    }

    public CropHelper(){

    }

    public CropHelper attractTo(final ImageView iv){
        this.imageView=iv;
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                iv.setOnTouchListener(CropHelper.this);
                matrix.set(iv.getImageMatrix());
                iv.setScaleType(ImageView.ScaleType.MATRIX);
                iv.setImageMatrix(matrix);
                CropHelper.this.onTouch(v,event);
                return true;
            }
        });
        return this;
    }

    public void setCropPath(CropPath cp){
        this.cropPath=cp;
    }

    private float[] values=new float[9];

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(v!=null&&((ImageView) v).getDrawable()!=null){
            ImageView view = (ImageView) v;
            Rect rect=view.getDrawable().getBounds();
            //事件处理
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                //一个手指按下时，标记为移动模式
                case MotionEvent.ACTION_DOWN:
                    matrix.set(view.getImageMatrix());
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                //第二个手指按下时，标记为缩放模式
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = distance(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    checkMatrix(rect);
                    mode = NONE;
                    break;
                //手指移动时，根据当前是移动模式还是缩放模式做相应处理
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = distance(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }
            view.setImageMatrix(matrix);
        }
        return true;
    }

    public Bitmap crop(){
        if(imageView!=null&&cropPath!=null){
            if(limit==null){
                limit=cropPath.limit();
            }
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            imageView.setDrawingCacheEnabled(true);
            Bitmap bmp=Bitmap.createBitmap(limit.width(),limit.height(), Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(bmp);
            canvas.drawColor(Color.TRANSPARENT);
            int lId=canvas.saveLayer(0,0,limit.width(),limit.height(),null,Canvas.ALL_SAVE_FLAG);
            Path path=new Path();
            path.addPath(cropPath.path(),-limit.left,-limit.top);
            canvas.drawPath(path,paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(imageView.getDrawingCache(),-limit.left,-limit.top,paint);
            paint.setXfermode(null);
            canvas.restoreToCount(lId);
            imageView.setDrawingCacheEnabled(false);
            return bmp;
        }
        return null;
    }

    public String cropAndSave(String path) throws IOException {
        Bitmap bmp=crop();
        if(bmp==null)return null;
        File file=new File(path);
        if(!file.getParentFile().exists()){
            boolean b=file.mkdirs();
            if(!b)return null;
        }
        if(file.exists()){
            boolean c=file.delete();
            if(!c)return null;
        }
        FileOutputStream fos=new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.PNG,100,fos);
        fos.flush();
        fos.close();
        bmp.recycle();
        return file.getAbsolutePath();
    }

    private void checkMatrix(Rect rect){
        if(limit==null&&cropPath!=null){
            limit=cropPath.limit();
        }
        if(limit!=null){
            if(mode==ZOOM){
                matrix.getValues(values);
                if(rect.width()*values[0]<limit.width()){   //当前宽度小于最小宽度
                    float scale = limit.width()/(float)rect.width()/values[0];
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
                matrix.getValues(values);
                if(rect.height()*values[4]<limit.height()){   //当前宽度小于最小宽度
                    float scale = limit.height()/(float)rect.height()/values[4];
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
            }
            matrix.getValues(values);
            if(values[2]>=limit.left){
                matrix.postTranslate(limit.left-values[2],0);
            }
            matrix.getValues(values);
            if(values[2]+rect.width()*values[0]<limit.right){
                matrix.postTranslate(limit.right-rect.width()*values[0]-values[2],0);
            }
            matrix.getValues(values);
            if(values[5]>limit.top){
                matrix.postTranslate(0,limit.top-values[5]);
            }
            matrix.getValues(values);
            if(values[5]+rect.height()*values[4]<limit.bottom){
                matrix.postTranslate(0,limit.bottom-rect.height()*values[4]-values[5]);
            }
        }
    }

    //计算MotionEvent的两个点之间的距离
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    //计算MotionEvent两个点之间的重点，用于缩放
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
