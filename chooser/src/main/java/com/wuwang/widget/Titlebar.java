package com.wuwang.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuwang.utils.DensityUtils;

/**
 * Created by wuwang on 2016/11/6
 */

public class TitleBar extends RelativeLayout{

    @IdRes private final int IdNav=0x1010;
    @IdRes private final int IdTitle=0x1011;
    @IdRes private final int IdLogo=0x1012;

    private ImageView mNavButtonView;
    private TextView mTitleTextView;
    private ImageView mLogoView;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(CharSequence title){
        ensureTitle();
        mTitleTextView.setText(title);
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        if (icon != null) {
            ensureNavButtonView();
            mNavButtonView.setImageDrawable(icon);
        }
    }

    public void setNavigationIcon(@DrawableRes int res){
        if (res>0) {
            ensureNavButtonView();
            mNavButtonView.setImageResource(res);
        }
    }

    public void setNavigationPadding(int left,int top,int right,int bottom){
        ensureNavButtonView();
        mNavButtonView.setPadding(left, top, right, bottom);
    }

    public void setLogo(Drawable icon){
        if(icon!=null){
            ensureLogo();
            mLogoView.setImageDrawable(icon);
        }
    }

    public void setLogo(@DrawableRes int res){
        ensureLogo();
        mLogoView.setImageResource(res);
    }

    private void ensureNavButtonView() {
        if (mNavButtonView == null) {
            mNavButtonView = new SquareImageView(getContext());
            mNavButtonView.setId(IdNav);
            mNavButtonView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            final LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.addRule(ALIGN_PARENT_LEFT);
            mNavButtonView.setLayoutParams(lp);
            addView(mNavButtonView);
        }
    }

    private void ensureTitle(){
        if(mTitleTextView==null){
            mTitleTextView=new TextView(getContext());
            mTitleTextView.setId(IdTitle);
            mTitleTextView.setLines(1);
            mTitleTextView.setTextSize(18);
            mTitleTextView.setMaxEms(9);
            mTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            mTitleTextView.setTextColor(0xFFFFFFFF);
            final LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_IN_PARENT);
            mTitleTextView.setLayoutParams(lp);
            addView(mTitleTextView);
        }
    }

    private void ensureLogo(){
        if(mLogoView==null){
            mLogoView=new ImageView(getContext());
            mLogoView.setId(IdLogo);
            final LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(CENTER_VERTICAL);
            mLogoView.setLayoutParams(lp);
            addView(mLogoView);
        }
    }

    public int getTitleId(){
        return IdTitle;
    }

    public int getNavigationId(){
        return IdNav;
    }

    public void addTitleRule(int verb){
        ensureTitle();
        LayoutParams lp= (LayoutParams) mTitleTextView.getLayoutParams();
        lp.addRule(verb);
        mTitleTextView.setLayoutParams(lp);
    }

    public void addTitleRule(int verb,int anchor){
        ensureTitle();
        LayoutParams lp= (LayoutParams) mTitleTextView.getLayoutParams();
        lp.addRule(verb,anchor);
        mTitleTextView.setLayoutParams(lp);
    }

    public void addLogoRule(int verb){
        ensureTitle();
        LayoutParams lp= (LayoutParams) mLogoView.getLayoutParams();
        lp.addRule(verb);
        mLogoView.setLayoutParams(lp);
    }

    public void addLogoRule(int verb,int anchor){
        ensureTitle();
        LayoutParams lp= (LayoutParams) mLogoView.getLayoutParams();
        lp.addRule(verb,anchor);
        mLogoView.setLayoutParams(lp);
    }

    public void setTitleClickListener(OnClickListener listener){
        ensureTitle();
        mTitleTextView.setOnClickListener(listener);
    }

    public void setNavigationClickListener(OnClickListener listener){
        ensureNavButtonView();
        mNavButtonView.setOnClickListener(listener);
    }

    public void setTitleMargin(int left,int top,int right,int bottom){
        ensureTitle();
        LayoutParams lp= (LayoutParams) mTitleTextView.getLayoutParams();
        lp.setMargins(left, top, right, bottom);
        mTitleTextView.setLayoutParams(lp);
    }

    public void setTitleSize(float dp){
        ensureTitle();
        mTitleTextView.setTextSize(getPix(dp));
    }

    public void setTitleColor(int color){
        ensureTitle();
        mTitleTextView.setTextColor(color);
    }

    public void setTitleColor(ColorStateList list){
        ensureTitle();
        mTitleTextView.setTextColor(list);
    }

    private int getPix(float dp){
        return DensityUtils.dip2px(getContext(),dp);
    }


}
