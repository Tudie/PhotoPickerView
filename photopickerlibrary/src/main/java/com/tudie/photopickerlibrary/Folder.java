package com.tudie.photopickerlibrary;

import android.net.Uri;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class Folder {

    public Uri uri;
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

//    public Folder() {
//
//    }

//    public Folder(String name, String path, Image cover, List<Image> images) {
//        this.name = name;
//        this.path = path;
//        this.cover = cover;
//        this.images = images;
//    }

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
