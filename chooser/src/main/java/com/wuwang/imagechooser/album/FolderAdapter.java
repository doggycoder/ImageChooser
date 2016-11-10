package com.wuwang.imagechooser.album;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;
import com.wuwang.imagechooser.res.IChooseDrawable;

import java.util.ArrayList;

/**
 * Description:
 */
public class FolderAdapter extends BaseAdapter {

    private IChooseDrawable drawable;
    private Fragment fragment;
    public ArrayList<ImageInfo> data;

    private final int TYPE_DEFAULT=0;
    private final int TYPE_TAKEPIC=1;

    private boolean isTackPhoto=ChooserSetting.takePhotoType!=ChooserSetting.TP_NONE;

    public FolderAdapter(Fragment fragment, ArrayList<ImageInfo> data, IChooseDrawable drawable){
        this.fragment=fragment;
        this.data=data;
        this.drawable=drawable;
    }

    public void setChooseDrawable(IChooseDrawable drawable){
        this.drawable=drawable;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0&&isTackPhoto){
            return TYPE_TAKEPIC;
        }
        return TYPE_DEFAULT;
    }

    public boolean isTakePhoto(int position){
        return getItemViewType(position)==TYPE_TAKEPIC;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position)==TYPE_TAKEPIC){
            convertView= LayoutInflater.from(fragment.getContext()).inflate(R.layout.image_chooser_item_camera,parent,false);
            if(ChooserSetting.tackPhotoIcon>0){
                ((ImageView)convertView).setImageResource(ChooserSetting.tackPhotoIcon);
            }
        }else{
            ImageHolder holder;
            if(convertView==null||convertView.getTag()==null){
                convertView= LayoutInflater.from(fragment.getContext()).inflate(R.layout.image_chooser_item_image,parent,false);
                holder=new ImageHolder(convertView);
            }else{
                holder= (ImageHolder) convertView.getTag();
            }
            holder.setData(data.get(position));
        }
        return convertView;
    }

    private class ImageHolder{
        ImageView mImage;
        View mFlag;
        ImageHolder(View convertView){
            mImage= (ImageView) convertView.findViewById(R.id.mImage);
            mFlag=convertView.findViewById(R.id.mFlag);
            convertView.setTag(this);
        }

        void setData(ImageInfo info){
            //图片加载
            DrawableRequestBuilder r=Glide.with(fragment).load(info.path)
                    .error(ChooserSetting.errorResId)
                    .placeholder(ChooserSetting.placeResId);
            if(ChooserSetting.loadAnimateResId<=0){
                r.dontAnimate();
            }else{
                r.animate(ChooserSetting.loadAnimateResId);
            }
            r.into(mImage);

            //状态加载
            if(drawable!=null){
                if(info.state<=0){
                    mImage.setColorFilter(ChooserSetting.unChooseFilter);
                }else{
                    mImage.setColorFilter(ChooserSetting.chooseFilter);
                }
                setBg(mFlag,drawable.get(info.state));
            }
        }

        private void setBg(View v, Drawable drawable){
            v.setBackgroundDrawable(null);
            v.setBackgroundDrawable(drawable);
        }
    }

}
