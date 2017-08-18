package com.tudie.photopickerlibrary.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @name： bitmap
 * @author： 杨广
 * @phone： 17382373271
 * @createTime:： 2017/6/7.
 * @modifyTime： 2017/6/7.
 * @explain：
 */

public class BitTransformation extends BitmapTransformation {

    public BitTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        // 如果BitmapPool中找不到符合该条件的Bitmap，get()方法会返回null，就需要我们自己创建Bitmap了
        if (result == null) {
            // 如果想让Bitmap支持透明度，就需要使用ARGB_8888
            result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }
        //创建最终Bitmap的Canvas.
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAlpha(128);
        // 将原始Bitmap处理后画到最终Bitmap中
        canvas.drawBitmap(toTransform, 0, 0, paint);
        // 由于我们的图片处理替换了原始Bitmap，就return我们新的Bitmap就行。
        // Glide会自动帮我们回收原始Bitmap。
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
