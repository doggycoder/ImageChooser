package com.wuwang.imagechooser.crop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.wuwang.imagechooser.IcFinal;
import com.wuwang.imagechooser.R;
import com.wuwang.utils.LogUtils;

/**
 * Description:
 */
public class CropActivity extends FragmentActivity {

    private Toolbar toolbar;
    private CropFragment cropFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_chooser_activity_crop);
        setTitle();
        LogUtils.e("wuang",getIntent().getStringExtra(IcFinal.INTENT_CROP_DATA));
        cropFragment=CropFragment.newFragment(
                getIntent().getStringExtra(IcFinal.INTENT_CROP_DATA),
                CropPath.SHAPE_CIRCLE,
                500,500);
        cropFragment.setOnReadyRunnable(new Runnable() {
            @Override
            public void run() {
                toolbar.getMenu().getItem(0).setEnabled(true);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mContainer,cropFragment)
                .commit();
    }

    public void setTitle(){
        toolbar= (Toolbar) findViewById(R.id.mTitle);
        toolbar.setTitle("裁剪图片");
        toolbar.setNavigationIcon(R.mipmap.image_chooser_back);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.inflateMenu(R.menu.menu_crop);
        toolbar.getMenu().getItem(0).setEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.mSure){
                    finish();
                }
                return false;
            }
        });
    }
}
