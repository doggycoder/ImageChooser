package com.wuwang.imagechooser.crop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.wuwang.imagechooser.R;

import java.io.File;

/**
 * Created by wuwang on 2016/11/4
 */

public class CropFragment extends Fragment {

    private View rootView;
    private ImageView mImage;
    private View mCover;

    private ICropCoverDrawable bg;
    private CropHelper helper;
    private Runnable ready;

    public static CropFragment newFragment(String path,int shape,int width,int height){
        CropFragment f=new CropFragment();
        Bundle b=new Bundle();
        b.putString("path",path);
        b.putInt("shape",shape);
        b.putInt("width",width);
        b.putInt("height",height);
        f.setArguments(b);
        return f;
    }

    public static CropFragment newFragment(String path,ICropCoverDrawable cover){
        CropFragment f=new CropFragment();
        Bundle b=new Bundle();
        b.putString("path",path);
        b.putSerializable("cover",cover);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.image_chooser_fragment_crop,container,false);
            mImage= (ImageView) rootView.findViewById(R.id.mImage);
            mCover=rootView.findViewById(R.id.mCover);
            initData();
        }
        return rootView;
    }

    private void initData(){
        Bundle b=getArguments();
        String src=b.getString("path");
        if(src!=null){
            Glide.with(this).load(src).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontTransform()
                    .dontAnimate()
                    .into(new GlideDrawableImageViewTarget(mImage){
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            if(ready!=null){
                                ready.run();
                            }
                        }
                    });
        }
        if(b.getInt("shape",-1)!=-1){
            bg=new CropCoverDrawable(b.getInt("width",500),b.getInt("height",500))
                    .setShape(b.getInt("shape"));
        }else{
            bg= (ICropCoverDrawable) b.getSerializable("cover");
        }
        mCover.setBackgroundDrawable(bg);
        helper=new CropHelper().attractTo(mImage);
        helper.setCropPath(bg);
    }

    public void setOnReadyRunnable(Runnable runnable){
        this.ready=runnable;
    }


}
