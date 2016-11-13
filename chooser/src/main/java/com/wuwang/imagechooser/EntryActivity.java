package com.wuwang.imagechooser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wuwang.imagechooser.abslayer.IPhotoShoot;
import com.wuwang.imagechooser.album.AlbumEntry;
import com.wuwang.imagechooser.album.AlbumPopup;
import com.wuwang.imagechooser.album.FolderFragment;
import com.wuwang.imagechooser.album.ImageFolder;
import com.wuwang.imagechooser.album.ImageInfo;
import com.wuwang.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 */
public class EntryActivity extends FragmentActivity implements IPhotoShoot{

    private AlbumEntry entry;
    private Toolbar toolbar;
    private MenuItem mSure;

    private String tackPicStr;
    private static final int REQ_TACK_PIC=0x15;

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
        m.setPhotoShoot(this);
        m.setSelectImgs(getIntent().getStringArrayListExtra(IcFinal.INTENT_EXIST_DATA));
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

//        IntentFilter filter=new IntentFilter(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        registerReceiver(receiver,filter);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            entry.refreshData();
        }
    };

    private void setTitle(){
        toolbar= (Toolbar) findViewById(R.id.mTitle);
        toolbar.setBackgroundColor(ChooserSetting.TITLE_COLOR);
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
        LogUtils.e("result-"+resultCode+"/"+requestCode);
        entry.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==REQ_TACK_PIC){
                //Bitmap bmp= (Bitmap) data.getExtras().get("data");
                //让拍摄的图片可以在相册目录中出现

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(tackPicStr))));

                if(entry.isCrop()){
                    entry.crop(tackPicStr);
                }else{
                    Intent intent=new Intent();
                    ArrayList<String> d=new ArrayList<>();
                    d.add(tackPicStr);
                    intent.putExtra(IcFinal.RESULT_DATA_IMG,d);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    @Override
    public void takePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //TODO 传入保存路径直接保存
        tackPicStr=ChooserSetting.tempFolder+"photo/"+System.currentTimeMillis()+".jpg";
        File folder=new File(ChooserSetting.tempFolder+"photo/");
        if(!folder.exists()){
            if(!folder.mkdirs()){
                Toast.makeText(this,"无法拍照",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File file = new File(tackPicStr);
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,REQ_TACK_PIC);
    }

}
