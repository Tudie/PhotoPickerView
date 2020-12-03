package com.tudie.photopickerview;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class AppApption extends MultiDexApplication {

    private static Context mContext;

    public static Context getmContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }
}
