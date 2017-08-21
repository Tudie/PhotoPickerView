package com.tudie.photopickerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tudie.photopickerlibrary.PhotoPickerActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.Select_Count_CAMERA,false);//是否需要照相机 可以不传默认需要
                intent.putExtra(PhotoPickerActivity.Select_Count_Type,5);//获取几张图片  可以不传默认一张
                startActivityForResult(intent,127);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 127) {
            switch (requestCode) {
                // 选择照片
                case 127:
                    ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.PicList);
                    ((TextView)findViewById(R.id.textpaths)).setText("图片路径"+paths);
                    break;


            }
        }
    }
}
