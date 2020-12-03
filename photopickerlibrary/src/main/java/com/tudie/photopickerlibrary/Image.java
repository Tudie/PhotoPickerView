package com.tudie.photopickerlibrary;

import android.net.Uri;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image {
    public Uri uri;
    public String path;
    public String name;
    public long time;

    public Image(Uri uri, String path, String name, long time) {
        this.uri = uri;
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
