package com.wuwang.imagechooser;


import com.wuwang.imagechooser.res.CircleChooseDrawable;
import com.wuwang.imagechooser.res.IChooseDrawable;

/**
 * Description:
 */
public class ChooserSetting{

    public static final int TP_SYSTEM=0;
    public static final int TP_CUSTOM=1;
    public static final int TP_NONE=2;

    /**标题的背景颜色*/
    public static int TITLE_COLOR=0xFF0dc6e2;

    /**图片选择页，每行显示数*/
    public static int NUM_COLUMNS=3;

    /**图片加载失败的图片*/
    public static int errorResId=0;

    /**图片加载的占位图片*/
    public static int placeResId=R.mipmap.image_chooser_placeholder;

    /**图片加载的动画*/
    public static int loadAnimateResId=0;

    /**拍照设置*/
    public static int takePhotoType=TP_SYSTEM;

    public static int tackPhotoIcon=0;

    /**选中图片的滤镜颜色*/
    public static int chooseFilter=0x55000000;

    /**未被选中的图片的滤镜颜色*/
    public static int unChooseFilter=0;

    /**最新的图片集合显示名字*/
    public static String newestAlbumName="最新图片";

    /**最新图片集合的最大数量*/
    public static int newestAlbumSize=100;

    public static int albumPopupHeight=600;

    public static String tantoToast="";

    /**照片选择指示器*/
    public static IChooseDrawable chooseDrawable=new CircleChooseDrawable(true,0xFF25c2e6);

}
