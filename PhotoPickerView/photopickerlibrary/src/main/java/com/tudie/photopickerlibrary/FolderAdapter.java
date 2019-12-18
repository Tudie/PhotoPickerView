package com.tudie.photopickerlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹Adapter
 * Created by Nereo on 2015/4/7.
 */
public class FolderAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<>();

    int mImageSize;

    int lastSelected = 0;

    public FolderAdapter(Activity context){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    /**
     * 设置数据集
     * @param folders
     */
    public void setData(List<Folder> folders) {
        if(folders != null && folders.size()>0){
//            List<Folder> folderss=new ArrayList<>();
//            for (int i = 0; i < folders.size(); i++) {
//                List<Image> liss=folders.get(i).images;
//                List<Image> lis=new ArrayList<>();
//                for (int j = 0; j < liss.size(); j++) {
//                    Image image = liss.get(j);
//                    if (getFileSize(new File(image.path))>2000)
//                        lis.add(liss.get(i));
//                }
//                folderss.add(new Folder(folders.get(i).name,folders.get(i).path,folders.get(i).cover,lis));
//            }
            mFolders = folders;
        }else{
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size()+1;
    }

    @Override
    public Folder getItem(int i) {
        if(i == 0) return null;
        return mFolders.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if(i == 0){
                holder.name.setText(mContext.getResources().getString(R.string.all_image));
                holder.size.setText(getTotalImageSize() + "张");
                if(mFolders.size()>0){
                    Folder f = mFolders.get(0);
                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                    Glide.with(mContext).load(new File(f.cover.path)).thumbnail(0.1f).apply(options).into(holder.cover);
//                    holder.cover.setImageBitmap(decodeSampledBitmapPath(f.cover.path));
                }
            }else {
                holder.bindData(getItem(i));
            }
            if(lastSelected == i){
                holder.indicator.setVisibility(View.VISIBLE);
            }else{
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize(){
        int result = 0;
        if(mFolders != null && mFolders.size()>0){
            for (Folder f: mFolders){
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if(lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex(){
        return lastSelected;
    }

    class ViewHolder{
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;
        ViewHolder(View view){
            cover = (ImageView)view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            name.setText(data.name);
            size.setText(data.images.size() + "张");
            // 显示图片
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(mContext).load(new File(data.cover.path)).thumbnail(0.1f).apply(options).into(cover);
//            cover.setImageBitmap(decodeSampledBitmapPath(data.cover.path));
//            Glide.with(mContext)
//                    .load(new File(data.cover.path))
//                    .placeholder(R.mipmap.default_error)
//                    .error(R.mipmap.default_error)
//                    .override(mImageSize, mImageSize)
//                    .centerCrop()
//                    .into(cover);

        }
    }

    /**
     * 获取指定文件大小
     * @param f
     * @return
     * @throws Exception
     */
    private long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            }catch (Exception e){}

        }
        return size;
    }


}
