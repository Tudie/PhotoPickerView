package com.tudie.photopickerlibrary.scanpicture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tudie.photopickerlibrary.PhotoPickerActivity;
import com.tudie.photopickerlibrary.R;
import com.tudie.photopickerlibrary.glide.GlideLoader;

import java.util.ArrayList;

/**
 * @name：
 * @author： 杨广
 * @phone： 17382373271
 * @createTime:： 2017/8/17.
 * @modifyTime： 2017/8/17.
 * @explain：
 */

public class ScanPictureActivity extends AppCompatActivity {

    private static ArrayList<String> paths;
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
        Log.i(">>>>>>>>>", ">>>>>>>>>" + paths);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(new SamplePagerAdapter());
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
                    getActivity().setResult(PhotoPickerActivity.Extra_Scan, data);
                    getActivity().finish();
                }
            });

//            photoView.setImageResource(paths.get(position));
            GlideLoader.GlideNormel(photoView, paths.get(position));
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
}
