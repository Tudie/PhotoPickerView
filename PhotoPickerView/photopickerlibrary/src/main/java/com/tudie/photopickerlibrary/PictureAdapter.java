package com.tudie.photopickerlibrary;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tudie.photopickerlibrary.glide.GlideLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tudie.com.photopickerlibrary.ImageCaptureManager;

/**
 * @name：
 * @createTime:： 2017/8/17.
 * @modifyTime： 2017/8/17.
 * @explain：
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.RecyclerViewHolder> {

    private Activity mContext;
    private int itemSize;
    private ImageCaptureManager captureManager;
    private boolean IsShowCamera;
    private int count = 1;

    private List<Image> mImages = new ArrayList<>();
    private List<Image> mSelectedImages = new ArrayList<>();
    private CallBack callBack;

    public PictureAdapter(Activity mContext, int whith, boolean IsShowCamera, int count, ImageCaptureManager captureManager, CallBack callBack) {
        this.mContext = mContext;
        this.itemSize = whith;
        this.IsShowCamera = IsShowCamera;
        this.count = count;
        this.captureManager = captureManager;
        this.callBack = callBack;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_picture, parent, false);
        //动态设置ImageView的宽高，根据自己每行item数量计算
        //dm.widthPixels-dip2px(20)即屏幕宽度-左右10dp+10dp=20dp再转换为px的宽度，最后/3得到每个item的宽高
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((DeviceUtils.getScrnWidthDp(activity)) , DeviceUtils.getScrnHeightDp(activity));
//        view.setLayoutParams(lp);
        return new PictureAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        if (IsShowCamera) {
            if (position == 0) {
                holder.mask.setVisibility(View.GONE);
                holder.checkmark.setVisibility(View.GONE);
                GlideLoader.GlideNormel(holder.image, R.mipmap.pic_camera, itemSize, itemSize);
                holder.flayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCameraAction();
                    }
                });
            } else {
                final int index = position;
                final Image data = mImages.get(position - 1);

                if (mSelectedImages.contains(data)) {
                    // 设置选中状态
                    holder.checkmark.setImageResource(R.mipmap.btn_selected);
                    holder.mask.setVisibility(View.VISIBLE);
                } else {
                    // 未选择
                    holder.checkmark.setImageResource(R.mipmap.btn_unselected);
                    holder.mask.setVisibility(View.GONE);
                }
                // 显示图片
                GlideLoader.GlideNormel(holder.image, mImages.get(position - 1).path);

                holder.flayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (mSelectedImages.contains(data)) {
                            mSelectedImages.remove(data);
                            callBack.values(mSelectedImages.size());
                            notifyItemChanged(index);
                        } else {
                            if (mSelectedImages.size() < count) {
                                mSelectedImages.add(data);
                                callBack.values(mSelectedImages.size());
                                notifyItemChanged(index);
                            } else {
                                Toast.makeText(mContext, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }

        } else {
            final int index = position;
            final Image data = mImages.get(position);

            if (mSelectedImages.contains(data)) {
                // 设置选中状态
                holder.checkmark.setImageResource(R.mipmap.btn_selected);
                holder.mask.setVisibility(View.VISIBLE);
            } else {
                // 未选择
                holder.checkmark.setImageResource(R.mipmap.btn_unselected);
                holder.mask.setVisibility(View.GONE);
            }
            // 显示图片
            GlideLoader.GlideNormel(holder.image, mImages.get(position).path);

            holder.flayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mSelectedImages.contains(data)) {
                        mSelectedImages.remove(data);
                        callBack.values(mSelectedImages.size());
                        notifyItemChanged(index);
                    } else {
                        if (mSelectedImages.size() < count) {
                            mSelectedImages.add(data);
                            callBack.values(mSelectedImages.size());
                            notifyItemChanged(index);
                        } else {
                            Toast.makeText(mContext, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }

    }

    /**
     * 设置数据集
     *
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     *
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        mSelectedImages.clear();
        for (String path : resultList) {
            Image image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        notifyDataSetChanged();
    }

    private Image getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (Image image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }


    public ArrayList<String> GetSelectPath() {
        ArrayList<String> mselectpath = new ArrayList<>();
        for (int i = 0; i < mSelectedImages.size(); i++) {
            mselectpath.add(mSelectedImages.get(i).path);
        }
        return mselectpath;
    }

    /**
     * 选择某个图片，改变选择状态
     *
     * @param image
     */
    public void select(Image image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else {
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }


    /**
     * 选择相机
     */
    private void showCameraAction() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            mContext.startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mContext, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    protected interface CallBack {
        void values(int selectnumber);
    }

    @Override
    public int getItemCount() {
        return IsShowCamera ? mImages.size() + 1 : mImages.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView image, checkmark;
        View mask;
        FrameLayout flayout;

        public RecyclerViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            checkmark = (ImageView) view.findViewById(R.id.checkmark);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(itemSize, itemSize);
            image.setLayoutParams(lp);
            mask = (View) view.findViewById(R.id.mask);
            flayout = (FrameLayout) view.findViewById(R.id.flayout);
        }
    }

}
