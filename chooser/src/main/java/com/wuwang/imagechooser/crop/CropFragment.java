package com.wuwang.imagechooser.crop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wuwang.imagechooser.R;

/**
 * Created by wuwang on 2016/11/4
 */

public class CropFragment extends Fragment {

    private View rootView;
    private ImageView mImage;
    private View mCover;

    private CropCoverDrawable bg;
    private CropHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.item_chooser_fragment_crop,container,false);
            mImage= (ImageView) rootView.findViewById(R.id.mImage);
            mCover=rootView.findViewById(R.id.mCover);
            initData();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initData(){
        bg=new CropCoverDrawable(500,500);
        bg.setShape(CropPath.SHAPE_CIRCLE);
        mCover.setBackgroundDrawable(bg);
        helper=new CropHelper().attractTo(mImage);
        helper.setCropPath(bg);
    }


}
