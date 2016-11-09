package com.wuwang.imagechooser.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.EnvironmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wuwang.imagechooser.ChooserSetting;
import com.wuwang.imagechooser.R;
import com.wuwang.imagechooser.abslayer.IAlpha;
import com.wuwang.imagechooser.abslayer.IImageClickListener;
import com.wuwang.imagechooser.res.IChooseDrawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Description:
 */
public class FolderFragment extends Fragment implements AlbumEntry.IFolderShower,IAlpha {

    private ViewGroup rootView;
    private GridView mGrid;
    private View mCover;
    private FolderAdapter adapter;
    private ArrayList<ImageInfo> data=new ArrayList<>();
    private Vector<ImageInfo> selectImgs;
    private IImageClickListener listener;
    private IChooseDrawable drawable;

    private static final int REQ_TACK_PIC=10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView= (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.image_chooser_fragment_album,container,false);
            initView();
            initData();
        }
        return rootView;
    }

    private void initView(){
        mGrid= (GridView) rootView.findViewById(R.id.mAlbum);
        mGrid.setNumColumns(ChooserSetting.NUM_COLUMNS);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.isTakePhoto(position)){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //TODO 传入保存路径直接保存
//                    File file = new File(path);
//                    Uri imageUri = Uri.fromFile(file );
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent,REQ_TACK_PIC);
                }else{
                    ImageInfo info=data.get(position);
                    if(info.state>0){   //点击已经选择过得
                        boolean a=listener.onCancel(selectImgs,info);
                        if(!a){
                            info.state=0;
                            selectImgs.removeElement(info);
                            int size=selectImgs.size();
                            for(int i=0;i<size;i++){
                                selectImgs.get(i).state=i+1;
                            }
                        }
                    }else{
                        boolean b=listener.onAdd(selectImgs,info);
                        if(!b){
                            info.state=selectImgs.size()+1;
                            selectImgs.add(info);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mCover=rootView.findViewById(R.id.mCover);
    }

    private void initData(){
        adapter=new FolderAdapter(this,data,drawable);
        mGrid.setAdapter(adapter);
        selectImgs=new Vector<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==REQ_TACK_PIC){
                //Bitmap bmp= (Bitmap) data.getExtras().get("data");
                //TODO 上面所设置的路径
            }
        }
    }

    @Override
    public void setChooseDrawable(IChooseDrawable drawable) {
        this.drawable=drawable;
        if(adapter!=null){
            adapter.setChooseDrawable(drawable);
        }
    }

    @Override
    public void setFolder(ImageFolder folder) {
        if(data!=null){
            data.clear();
            data.addAll(folder.getDatas());
            int size=data.size();
            for (ImageInfo s:selectImgs){
                for(int i=0;i<size;i++){
                    ImageInfo info=data.get(i);
                    if(info.state==0&&info.path.equals(s.path)){
                        data.remove(i);
                        data.add(i,s);
                        break;
                    }
                }
            }
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ChooserSetting.chooseDrawable!=null){
            ChooserSetting.chooseDrawable.clear();
        }
    }

    @Override
    public void setImageClickListener(IImageClickListener listener) {
        this.listener=listener;
    }

    @Override
    public List<ImageInfo> getSelectedImages() {
        return selectImgs;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setAlpha(int alpha) {
        mCover.setBackgroundColor(alpha);
    }

    @Override
    public void show() {
        mCover.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        mCover.setVisibility(View.GONE);
    }
}
