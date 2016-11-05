package com.wuwang.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.wuwang.imagechooser.EntryActivity;
import com.wuwang.imagechooser.IcFinal;

public class MainActivity extends AppCompatActivity {

    private GridView mGrid;
    private ShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGrid= (GridView) findViewById(R.id.mGrid);
        mGrid.setAdapter(adapter=new ShowAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(IcFinal.ACTION_ALBUM);
        switch (item.getItemId()){
            case R.id.mOne:
                intent.putExtra(IcFinal.INTENT_MAX_IMG,1);
                break;
            case R.id.mNine:
                intent.putExtra(IcFinal.INTENT_MAX_IMG,9);
                break;
            case R.id.mCrop:
                intent.putExtra(IcFinal.INTENT_IS_CROP,true);
                break;
        }
        startActivityForResult(intent,1);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            adapter.data.clear();
            adapter.data.addAll(data.getStringArrayListExtra(IcFinal.RESULT_DATA_IMG));
            adapter.notifyDataSetChanged();
        }
    }
}
