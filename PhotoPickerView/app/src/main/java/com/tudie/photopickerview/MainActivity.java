package com.tudie.photopickerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                intent.putExtra(PhotoPickerActivity.Select_Count_CAMERA,true);//是否需要照相机 可以不传默认需要
                intent.putExtra(PhotoPickerActivity.Select_Count_Type,5);//获取几张图片  可以不传默认一张
                startActivityForResult(intent,127);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 127) {
            switch (resultCode) {
                // 选择照片
                case RESULT_OK:
                    ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.PicList);
                    ((TextView)findViewById(R.id.textpaths)).setText("图片路径"+paths);
                    break;


            }
        }
    }
}
