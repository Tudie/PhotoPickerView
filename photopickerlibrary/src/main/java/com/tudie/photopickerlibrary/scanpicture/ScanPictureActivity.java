package com.tudie.photopickerlibrary.scanpicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tudie.photopickerlibrary.PhotoPickerActivity;
import com.tudie.photopickerlibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @name：
 * @author：
 * @phone：
 * @createTime:： 2017/8/17.
 * @modifyTime： 2017/8/17.
 * @explain：
 */

public class ScanPictureActivity extends AppCompatActivity {

    private static ArrayList<String> paths;
    private int index=0;
    private static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        ScanPictureActivity.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivity(this);

        setContentView(R.layout.act_scanpicture);
        paths = getIntent().getStringArrayListExtra(PhotoPickerActivity.PicList);
        try {
            index = getIntent().getIntExtra(PhotoPickerActivity.Picindex,0);
        }catch (Exception e){

        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (Length(paths) < 1) {
            return;
        }
        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(index);
    }

    static class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    Intent data = new Intent();
                    data.putStringArrayListExtra(PhotoPickerActivity.PicList, paths);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            });

//            photoView.setImageResource(paths.get(position));
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(photoView.getContext()).load(paths.get(position)).thumbnail(0.1f).apply(options).into(photoView);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private int Length(List<String> list) {
        int length = 0;
        if (list != null) {
            length = list.size();
        }
        return length;
    }
}
