# PhotoPickerView

## 1. 在Module下的build.gradle中添加依赖
### Step 1. Add the JitPack repository to your build file
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
### Step 2. Add the dependency
     dependencies {
	         implementation 'com.github.Tudie:PhotoPickerView:v1.0.1'
	}

## 2. 跳转到相册使用
    Intent intent=new Intent();
    intent.setClass(MainActivity.this,PhotoPickerActivity.class);
    intent.putExtra(PhotoPickerActivity.Select_Count_CAMERA,false);//是否需要照相机 可以不传默认需要
    intent.putExtra(PhotoPickerActivity.Select_Count_Type,5);//获取几张图片  可以不传默认一张
    startActivityForResult(intent,127);//请求code  默认127

## 3. 图片浏览

    Intent data = new Intent();
	ArrayList<String> imgList = new ArrayList<>(); // 结果数据
    data.putStringArrayListExtra(PhotoPickerActivity.PicList,imgList);//图片数组
    data.setClass(PhotoPickerActivity.this, ScanPictureActivity.class);
    startActivityForResult(data, PhotoPickerActivity.Extra_Scan);
