package com.xukj.kpframework.gallery;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;

import com.bumptech.glide.Glide;

import java.io.File;

public class KPCacheUtil {

    private static KPCacheUtil instance;

    public static KPCacheUtil getInstance() {
        if (null == instance) {
            instance = new KPCacheUtil();
        }
        return instance;
    }

    /**
     * 获取缓存大小
     * @param context
     * @return
     */
    public long getCacheSize(final Context context) {
        String path = context.getCacheDir().getPath();
        return getFolderSize(new File(path + "/" + "image_manager_disk_cache"));
    }

    /**
     * 清除缓存
     * @param context
     * @return
     */
    public boolean clearCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 获取指定文件夹内所有文件大小的和
    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
