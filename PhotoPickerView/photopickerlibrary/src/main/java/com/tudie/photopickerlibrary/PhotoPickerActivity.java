package com.tudie.photopickerlibrary;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.tudie.photopickerlibrary.scanpicture.ScanPictureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tudie.com.photopickerlibrary.ImageCaptureManager;

/**
 * @name：
 * @createTime:： 2017/8/17.
 * @modifyTime： 2017/8/17.
 * @explain：
 */

public class PhotoPickerActivity extends AppCompatActivity {

    public static final String Extra_Code_Type = "reqeuse_code";
    public static final String Select_Count_Type = "max_select_count";
    public static final String Select_Count_CAMERA = "show_camera";
    public static final String PicList = "listpicdata";
    private int DefaultPicNumber = 1;//默认最大照片数量
    private boolean IsShowCamera = true;//是否显示相机
    private int Extra_Code = 127;//相册请求状态码
    public static final int Extra_Scan = 99;//预览请求状态码

    private ArrayList<String> resultList = new ArrayList<>(); // 结果数据

    private ArrayList<Folder> mResultFolder = new ArrayList<>();    // 文件夹数据

    private RecyclerView my_recycler_view;
    private PictureAdapter adapter;

    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;
    private ImageCaptureManager captureManager;

    private View mPopupAnchorView;
    private Button btnAlbum;
    private Button btnPreview;
    private TextView title_next;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photopicker);
        initView();
        initData();

    }

    private void initView() {
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        mPopupAnchorView = findViewById(R.id.photo_picker_footer);
        btnAlbum = (Button) findViewById(R.id.btnAlbum);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        title_next = (TextView) findViewById(R.id.title_next);

    }

    private void initData() {
        captureManager = new ImageCaptureManager(PhotoPickerActivity.this);
        IntentData();
        initRecyclerView();
        // 首次加载所有图片
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        // 打开相册列表
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });
        title_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultList = adapter.GetSelectPath();
                complete();
            }
        });
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putStringArrayListExtra(PhotoPickerActivity.PicList, adapter.GetSelectPath());
                data.setClass(PhotoPickerActivity.this, ScanPictureActivity.class);
                startActivityForResult(data, PhotoPickerActivity.Extra_Scan);
            }
        });
        mFolderAdapter = new FolderAdapter(PhotoPickerActivity.this);


    }

    private void IntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Select_Count_Type)) {
                DefaultPicNumber = bundle.getInt(Select_Count_Type);
            }
            if (bundle.containsKey(Select_Count_CAMERA)) {
                IsShowCamera = bundle.getBoolean(Select_Count_CAMERA);
            }
            if (bundle.containsKey(Extra_Code_Type)) {
                Extra_Code = bundle.getInt(Extra_Code_Type);
            }
        }
    }

    private void initRecyclerView() {
        adapter = new PictureAdapter(PhotoPickerActivity.this, getItemImageWidth(), IsShowCamera, DefaultPicNumber, captureManager, new PictureAdapter.CallBack() {
            @Override
            public void values(int selectnumber) {
                refreshActionStatus(selectnumber);
            }
        });
        my_recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
        my_recycler_view.addItemDecoration(new GridDivider(PhotoPickerActivity.this, 4, R.color.trans));
        my_recycler_view.setAdapter(adapter);
    }


    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(PhotoPickerActivity.this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);

        // 计算ListPopupWindow内容的高度(忽略mPopupAnchorView.height)，R.layout.item_foloer
        int folderItemViewHeight =
                // 图片高度
                getResources().getDimensionPixelOffset(R.dimen.folder_cover_size) +
                        // Padding Top
                        getResources().getDimensionPixelOffset(R.dimen.folder_padding) +
                        // Padding Bottom
                        getResources().getDimensionPixelOffset(R.dimen.folder_padding);
        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;

        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        if (folderViewHeight >= screenHeigh) {
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        } else {
            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        }

        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            btnAlbum.setText(R.string.all_image);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                adapter.setData(folder.images);
                                btnAlbum.setText(folder.name);
                            }
                        }

                        // 滑动到最初始位置
                        my_recycler_view.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            // 根据图片设置参数新增验证条件
            StringBuilder selectionArgs = new StringBuilder();

            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(PhotoPickerActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selectionArgs.toString(), null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                String selectionStr = selectionArgs.toString();
                if (!"".equals(selectionStr)) {
                    selectionStr += " and" + selectionStr;
                }
                CursorLoader cursorLoader = new CursorLoader(PhotoPickerActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'" + selectionStr, null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    adapter.setData(images);

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        resultList.add(captureManager.getCurrentPhotoPath());
                    }
                    complete();
                    break;
                // 预览照片
                case Extra_Scan:
                    ArrayList<String> pathArr = data.getStringArrayListExtra(PicList);
                    // 刷新页面
                    if (pathArr != null && pathArr.size() != resultList.size()) {
                        resultList = pathArr;
                        refreshActionStatus(resultList.size());
                        adapter.setDefaultSelected(resultList);
                    }
                    break;
            }
        }
    }

    // 返回已选择的图片数据
    private void complete() {
        Intent data = new Intent();
        data.putStringArrayListExtra(PicList, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 刷新操作按钮状态
     */
    private void refreshActionStatus(int selectnumber) {
        if (selectnumber < 1) {
            title_next.setText(getResources().getString(R.string.done));
            btnPreview.setText(getResources().getString(R.string.preview));
            btnPreview.setEnabled(false);
            title_next.setEnabled(false);
        } else {
            if (DefaultPicNumber == 1) {
                resultList=adapter.GetSelectPath();
                complete();
            } else {
                title_next.setText(getResources().getString(R.string.done) + "(" + selectnumber + "/" + DefaultPicNumber + ")");
                title_next.setVisibility(View.VISIBLE);
                btnPreview.setText(getResources().getString(R.string.preview) + "(" + selectnumber + ")");
                btnPreview.setEnabled(true);
                title_next.setEnabled(true);
            }

        }

    }

    /**
     * 获取GridView Item宽度
     *
     * @return
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        Log.i(">>>>>>>>columnSpace",">>>>>>>>columnSpace"+columnSpace);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列
     *
     * @return
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
