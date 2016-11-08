package com.wuwang.imagechooser.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.ImageFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.wuwang.imagechooser.ChooserSetting;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Description:
 */
public class AlbumTool {

    private Handler handler;
    //private Semaphore semaphore;
    private Callback callback;
    private Context context;

    private final int TYPE_FOLDER=1;
    private final int TYPE_ALBUM=2;

    public AlbumTool(Context context){
        this.context=context;
        handler=new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(callback!=null){
                    switch (msg.what){
                        case TYPE_FOLDER:
                            callback.onFolderFinish((ImageFolder) msg.obj);
                            break;
                        case TYPE_ALBUM:
                            callback.onAlbumFinish((ArrayList<ImageFolder>) msg.obj);
                            break;
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    public void setCallback(Callback callback){
        this.callback=callback;
    }

    public void findAlbumsAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlbums(context);
            }
        }).start();
    }

    public void findFolderAsync(final ImageFolder folder){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getFolder(context,folder);
            }
        }).start();
    }

    //获取所有图片集
    private ArrayList<ImageFolder> getAlbums(Context context) {
        ArrayList<ImageFolder> albums=new ArrayList<>();
        albums.add(getNewestPhotos(context));
        //利用ContentResolver查询数据库，找出所有包含图片的文件夹，保存到相册列表中
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_ID,
                        MediaStore.Images.Media.DATE_MODIFIED,
                        "count(*) as count"
                },
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=?) " +
                        "group by (" + MediaStore.Images.ImageColumns.BUCKET_ID,
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                final File file = new File(cursor.getString(0));
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.setDir(file.getParent());
                imageFolder.setId(cursor.getString(1));
                imageFolder.setFirstImagePath(cursor.getString(0));
                String[] all=file.getParentFile().list(new FilenameFilter() {

                    private boolean e(String filename,String ends){
                        return filename.toLowerCase().endsWith(ends);
                    }

                    @Override
                    public boolean accept(File dir, String filename) {
                        return e(filename,".png") || e(filename,".jpg") || e(filename,"jpeg");
                    }
                });
                if(all!=null&&all.length>0){
                    imageFolder.setCount(all.length);
                    albums.add(imageFolder);
                }
            }
            cursor.close();
        }
        sendMessage(TYPE_ALBUM,albums);
        return albums;
    }

    //获取《最新图片》集
    private ImageFolder getNewestPhotos(Context context) {
        ImageFolder newestFolder=new ImageFolder();
        newestFolder.setName(ChooserSetting.newestAlbumName);
        ArrayList<ImageInfo> imageBeans = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_MODIFIED,
                },
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
                        + (ChooserSetting.newestAlbumSize < 0 ? ""
                        : (" limit " + ChooserSetting.newestAlbumSize)));
        if (cursor != null){
            while (cursor.moveToNext()) {
                ImageInfo info=new ImageInfo();
                info.path=cursor.getString(0);
                info.displayName=cursor.getString(1);
                info.time=cursor.getLong(2);
                imageBeans.add(info);
            }
            cursor.close();
            newestFolder.setFirstImagePath(imageBeans.get(0).path);
            newestFolder.setDatas(imageBeans);
            newestFolder.setCount(imageBeans.size());
        }
        sendMessage(TYPE_FOLDER,newestFolder);
        return newestFolder;
    }

    //获取具体图片集，确保图片数据已被查询
    private ImageFolder getFolder(Context context,ImageFolder folder) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor;
        if(folder!=null&&folder.getDatas()!=null&&folder.getDatas().size()>0){
            sendMessage(TYPE_FOLDER,folder);
            return folder;
        }
        if (folder == null) {
            return getNewestPhotos(context);
        } else {
            cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.DATE_MODIFIED
                    },
                    MediaStore.Images.ImageColumns.BUCKET_ID + "=? and (" +
                            MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?) ",
                    new String[]{folder.getId(), "image/jpeg", "image/png", "image/jpg"},
                    MediaStore.Images.Media.DATE_MODIFIED + " desc");
        }
        ArrayList<ImageInfo> datas=new ArrayList<>();
        folder.setDatas(datas);
        if (cursor != null){
            while (cursor.moveToNext()) {
                ImageInfo info=new ImageInfo();
                info.path=cursor.getString(0);
                info.displayName=cursor.getString(1);
                info.time=cursor.getLong(2);
                datas.add(info);
            }
            cursor.close();
        }
        sendMessage(TYPE_FOLDER,folder);
        return folder;
    }

    private void sendMessage(int what,Object obj){
        Message msg=new Message();
        msg.what=what;
        msg.obj=obj;
        handler.sendMessage(msg);
    }

    public interface Callback{

        //文件夹查找完毕
        void onFolderFinish(ImageFolder folder);
        //成功搜索出所有的图片集
        void onAlbumFinish(ArrayList<ImageFolder> albums);

    }

}
