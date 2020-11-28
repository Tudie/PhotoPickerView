package com.tudie.photopickerview;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tudie.photopickerlibrary.PhotoPickerActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<String> permissions = new ArrayList<>();
    private boolean isvideo=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION //这里删除的话  可以解决华为虚拟按键的覆盖
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);//这里删除的话
        }

        setContentView(R.layout.activity_main);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvideo=false;
                if (permissions.size()< 1) {
                    //权限
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                getPersimmionss(permissions);
            }
        });
        findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvideo=true;
                if (permissions.size()< 1) {
                    //权限
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                getPersimmionss(permissions);

            }
        });

    }


    public void getPersimmionss(List<String> permissions) {
        for (int i = 0; i < permissions.size(); i++) {
            if (!gePpermissions(permissions.get(i))) {
                getPersimmions(permissions);
                return;
            }
        }
        Jumpe();
    }

    private void Jumpe(){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.Select_Count_CAMERA,true);//是否需要照相机 可以不传默认需要
        intent.putExtra(PhotoPickerActivity.Select_Count_Type,5);//获取几张图片  可以不传默认一张
        intent.putExtra(PhotoPickerActivity.Select_Video_Type,isvideo);//图片还是视频
        startActivityForResult(intent,127);


    }
    public  boolean gePpermissions(String permission) {
        PackageManager pm = MainActivity.this.getPackageManager();
        boolean ishava = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, getPackageName()));
        return ishava;
    }
    @TargetApi(23)
    public void getPersimmions(List<String> permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.size()> 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            }
        }
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 127) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != 0) {//0 始终允许 -1 仅使用期间
                    Log.i(">>>>>>>>>>>>>>>>aaaaa ",">>>>>>>>>>>>>>>>");
                    return;
                }
            }
            Jumpe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 127) {
            switch (resultCode) {
                // 选择照片
                case RESULT_OK:
                    ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.PicList);
                    ((TextView)findViewById(R.id.textpaths)).setText("路径  "+paths);
                    break;


            }
        }
    }
}
