package com.tudie.photopickerview;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iceteck.silicompressorr.SiliCompressor;
import com.tudie.photopickerlibrary.PhotoPickerActivity;
import com.tudie.photopickerlibrary.scanpicture.ScanPictureActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<String> permissions = new ArrayList<>();
    private boolean isvideo = false;
    private boolean isuri = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        findViewById(R.id.texturi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvideo = false;
                isuri = true;
                if (permissions.size() < 1) {
                    //权限
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

                }
                getPersimmionss(permissions);
            }
        });
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isvideo = false;
                isuri = false;
                if (permissions.size() < 1) {
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
                isvideo = true;
                isuri = false;
                if (permissions.size() < 1) {
                    //权限
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                getPersimmionss(permissions);

            }
        });
        findViewById(R.id.scanpaths).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();
                Bundle bundle = new Bundle();
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608009879524&di=0e581c7cbfc06024446f5efe67a01e6c&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F27%2F67%2F01300000921826141299672233506.jpg");
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608009879524&di=79bd407783c30c093e1d3be03f654fe9&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F42%2F31%2F01300001320894132989315766618.jpg");
                list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608009879524&di=ff8c7fb0a224b61167e189d5a929b2a0&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F55%2F22%2F20300000929429130630222900050.jpg");
                bundle.putStringArrayList(PhotoPickerActivity.PicList, list);
                bundle.putInt(PhotoPickerActivity.Picindex, 0);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanPictureActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public File saveBitmapFile(Bitmap bitmap, int index) {

        File dirI = MainActivity.this.getExternalFilesDir("Image");

        if (!dirI.exists()) {
            dirI.mkdirs();
        }
        File file = new File(dirI, "" + System.currentTimeMillis() + index + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
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

    private void Jumpe() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.Select_Count_CAMERA, true);//是否需要照相机 可以不传默认需要
        intent.putExtra(PhotoPickerActivity.Select_Count_Type, 5);//获取几张图片  可以不传默认一张
        intent.putExtra(PhotoPickerActivity.Select_Video_Type, isvideo);//图片还是视频
        intent.putExtra(PhotoPickerActivity.RESULT_URI, isuri);
        startActivityForResult(intent, 127);


    }

    public boolean gePpermissions(String permission) {
        PackageManager pm = MainActivity.this.getPackageManager();
        boolean ishava = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, getPackageName()));
        return ishava;
    }

    @TargetApi(23)
    public void getPersimmions(List<String> permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 127);
            }
        }
    }


    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 127) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != 0) {//0 始终允许 -1 仅使用期间
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
                    ((TextView) findViewById(R.id.textpaths)).setText("路径  " + paths);
//                    try {
//                        Bitmap imageBitmap = SiliCompressor.with(this).getCompressBitmap(paths.get(0));
//                        ((ImageView) findViewById(R.id.ma_pic_iv)).setImageBitmap(imageBitmap);
//                    }catch (Exception e){}
//                    String filePath= SiliCompressor.with(this).compress(paths.get(0), destinationDirectory);
//                    ((ImageView) findViewById(R.id.ma_pic_iv)).setImageBitmap(getimage(Uri.parse(paths.get(0))));
//                    saveBitmapFile(getimage(Uri.parse(paths.get(0))),0);
                    if (isuri){
                        Glideurl((ImageView) findViewById(R.id.ma_pic_iv), getimage(Uri.parse(paths.get(0))));
                    }else {
                        Glideurl((ImageView) findViewById(R.id.ma_picpath_iv), getimagepath(paths.get(0)));

                    }
                    break;


            }
        }
    }

    public void Glideurl(ImageView imageView, Object url) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(imageView.getContext()).load(url).thumbnail(0.01f).apply(options).into(imageView);

    }

    public Bitmap getimage(Uri uri) {
        Bitmap bitmap = null;// 此时返回bm为空
        BitmapFactory.Options newOpts = Options();
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri), null, newOpts);
        } catch (Exception e) {
        }

        return compressImage(bitmap, 0);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public Bitmap getimagepath(String srcPath) {
        BitmapFactory.Options newOpts = Options();
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, 0);// 压缩好比例大小后再进行质量压缩
    }


    public Bitmap compressImage(Bitmap image, int be) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (image == null)
            return null;
        int options = 100;
        if (be != 1)
            options = 80;
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Log.i(">>>>>>>>>>  ", ">>>>>>>>>>>  " + baos.toByteArray().length);
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options < 1) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, Options());// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public BitmapFactory.Options Options() {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 400f;// 这里设置高度为800f
        float ww = 400f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return newOpts;// 压缩好比例大小后再进行质量压缩
    }

    public double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    public final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    private double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

}
