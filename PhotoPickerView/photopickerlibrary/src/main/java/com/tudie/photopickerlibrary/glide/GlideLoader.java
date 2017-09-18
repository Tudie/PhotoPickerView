package com.tudie.photopickerlibrary.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestOptions;
import com.tudie.photopickerlibrary.R;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/12/8.
 */

public class GlideLoader {

    /**
     * 有缓存
     *
     * @param imageView
     * @param url
     */
    public static void GlideNormel(ImageView imageView, Object url) {
        if (!isNull(url)) {
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.pic_noimages)
                    .placeholder(R.mipmap.pic_noimages)
                    .skipMemoryCache(true);
            Glide.with(imageView.getContext()).load(url).thumbnail(0.1f).apply(options).into(imageView);
        }
    }

    public static void GlideNormel(ImageView imageView, Object url, int width, int height) {
        if (!isNull(url)) {
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.pic_noimages)
                    .placeholder(R.mipmap.pic_noimages)
                    .skipMemoryCache(true)
                    .override(width, height);
            Glide.with(imageView.getContext()).load(url).thumbnail(0.1f).apply(options).into(imageView);
        }
    }


    /**
     * 返回bitmap
     *
     * @param imgurl
     */

    public static void GlideBitmap(Context context, String imgurl, final LoadImageCallback callback) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true).bitmapTransform(new Transformation<Bitmap>() {
            @Override
            public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int outWidth, int outHeight) {
                callback.callback(resource.get());
                return null;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {

            }
        });
        Glide.with(context)
                .load(imgurl).apply(options);

    }

    /**
     * 返回bitmap
     *
     * @param imageView
     */

    public static Bitmap GlideControlBitmap(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap obmp = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
        return obmp;
    }


    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param url
     */
    public static void GlideCircle(ImageView imageView, Object url) {
        if (!isNull(url)) {
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.pic_noimages)
                    .placeholder(R.mipmap.pic_noimages)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .transform(new GlideCircleTransform(imageView.getContext()));
            Glide.with(imageView.getContext()).load(url).thumbnail(0.1f).apply(options).into(imageView);
        }

    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param url
     */
    public static void GlideRoundTransform(ImageView imageView, Object url) {
        if (!isNull(url)) {
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.pic_noimages)
                    .placeholder(R.mipmap.pic_noimages)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .transform(new GlideRoundTransform(imageView.getContext(), 6));
            Glide.with(imageView.getContext()).load(url).thumbnail(0.1f).apply(options).into(imageView);
        }
    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param url
     * @param circular
     */
    public static void GlideRoundTransform(ImageView imageView, Object url, int circular) {
        if (!isNull(url)) {
            RequestOptions options = new RequestOptions()
                    .error(R.mipmap.pic_noimages)
                    .placeholder(R.mipmap.pic_noimages)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .transform(new GlideRoundTransform(imageView.getContext(), circular));
            Glide.with(imageView.getContext()).load(url).thumbnail(0.1f).apply(options).into(imageView);
        }
    }


    public interface LoadImageCallback {
        void callback(Bitmap bitmap);
    }

    /**
     * 判断对象是否为null,不允许空白串
     *
     * @param object 目标对象类型
     * @return
     */
    public static boolean isNull(Object object) {
        if (null == object) {
            return true;
        }
        if ((object instanceof String)) {
            return "".equals(((String) object).trim());
        }
        return false;
    }

}
