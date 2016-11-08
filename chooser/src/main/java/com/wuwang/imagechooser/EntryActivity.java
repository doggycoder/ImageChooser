package com.wuwang.imagechooser;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuwang.imagechooser.album.AlbumEntry;
import com.wuwang.imagechooser.album.AlbumPopup;
import com.wuwang.imagechooser.album.FolderFragment;
import com.wuwang.imagechooser.album.ImageFolder;
import com.wuwang.imagechooser.album.ImageInfo;
import com.wuwang.widget.TitleBar;

import java.util.List;

/**
 * Description:
 */
public class EntryActivity extends FragmentActivity{

    private AlbumEntry entry;
    private Toolbar toolbar;
    private MenuItem mSure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_entry);
        setTitle();
        Rect outRect=new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        ChooserSetting.albumPopupHeight=(int) (outRect.height()*0.6f);
//        mAlbumNum= (TextView) findViewById(R.id.mAlbum);
//        mAlbumNum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                entry.showAlbumChooser();
//            }
//        });
//        findViewById(R.id.mSure).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                entry.chooseFinish();
//            }
//        });
        FolderFragment m=new FolderFragment();
        entry=new AlbumEntry(this, R.id.mEntry, m, new AlbumPopup(this,toolbar,m)){
            @Override
            public void onAlbumClick(ImageFolder folder) {
                super.onAlbumClick(folder);
//                toolbar.setTitle(folder.getName()+"("+folder.getCount()+")");
            }

            @Override
            public boolean onAdd(List<ImageInfo> data, ImageInfo info) {
                boolean a=super.onAdd(data, info);
                if(!a&&mSure!=null&&getMax()!=1){
                    mSure.setEnabled(true);
                }
                return a;
            }

            @Override
            public boolean onCancel(List<ImageInfo> data, ImageInfo info) {
                boolean a=super.onCancel(data, info);
                if(!a&&mSure!=null&&data.size()<=1){
                    mSure.setEnabled(false);
                }
                return a;
            }
        };

        if(entry.getMax()>1){
            mSure=toolbar.getMenu().getItem(1);
            mSure.setEnabled(false);
        }else{
            toolbar.getMenu().getItem(1).setVisible(false);
        }
    }

    private void setTitle(){
        toolbar= (Toolbar) findViewById(R.id.mTitle);
        toolbar.setTitle("图片选择");
        toolbar.setNavigationIcon(R.mipmap.image_chooser_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.menu_album);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.mAlbum) {
                    entry.showAlbumChooser();
                }else if(i==R.id.mSure){
                    entry.chooseFinish();
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        entry.onActivityResult(requestCode, resultCode, data);
    }
}
