package com.xukj.kpframework.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.xukj.kpframework.gallery.PhotoImage;
import com.xukj.kpframework.gallery.ViewPagerActivity;


import java.util.ArrayList;


public class KPGalleryModule extends ReactContextBaseJavaModule {

    // 图片地址
    private ArrayList<PhotoImage> images = new ArrayList<>();
    private int index = 0;
    private float minScale = (float) 0.5;
    private float maxScale = 2;
    private String mode; // 模式
    private boolean debug = false;
    private String orientation = "auto";
    private boolean seek = false;

    private ReadableMap options;


    public static ReactContext context;
    private ReactContext mReactContext;

    public KPGalleryModule(ReactApplicationContext context) {
        super(context);
        this.mReactContext = context;
        KPGalleryModule.context = context;
    }

    @Override
    public String getName() {
        return "KPGallery";
    }

    /**
     * 开启图片浏览器
     *
     * @param options 配置
     */
    @ReactMethod
    public void showGallery(ReadableMap options) {
        this.options = options;
        setConfiguration(options);

        Intent intent = new Intent();
        intent.setClass(mReactContext, ViewPagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("images", images);
        bundle.putInt("index", index);
        bundle.putString("orientation", orientation);
        bundle.putBoolean("seek", seek);
        intent.putExtras(bundle);
        mReactContext.startActivity(intent);
    }

    /**
     * 获取缓存大小
     * @param options
     * @param promise
     */
    @ReactMethod
    public void getCacheSize(ReadableMap options, Promise promise) {
        long size = KPCacheUtil.getInstance().getCacheSize(mReactContext);
        promise.resolve(String.valueOf(size));
    }

    /**
     * 清空缓存
     * @param options
     * @param promise
     */
    @ReactMethod
    public void clearCache(ReadableMap options, Promise promise) {
        boolean result = KPCacheUtil.getInstance().clearCache(mReactContext);
        if (result) {
            promise.resolve(null);
        }
        else {
            promise.reject("-1", "清除缓存出错");
        }
    }

    private void setConfiguration(final ReadableMap options) {
        index = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_INDEX) ? options.getInt(KPGalleryConstant.KPPHOTO_GALLERY_KEY_INDEX) : index;
        minScale = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MINSCALE) ? (float) options.getDouble(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MINSCALE) : minScale;
        maxScale = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MAXSCALE) ? (float) options.getDouble(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MAXSCALE) : maxScale;
        debug = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_DEBUG) ? options.getBoolean(KPGalleryConstant.KPPHOTO_GALLERY_KEY_DEBUG) : debug;
        mode = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MODE) ? options.getString(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MODE) : mode;
        orientation = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_ORIENTATION) ? options.getString(KPGalleryConstant.KPPHOTO_GALLERY_KEY_ORIENTATION) : orientation;
        seek = options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_USESEEK) ? options.getBoolean(KPGalleryConstant.KPPHOTO_GALLERY_KEY_USESEEK) : seek;

        images.clear();
        if (options.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_IMAGES)) {
            setPhotoImages(options.getArray(KPGalleryConstant.KPPHOTO_GALLERY_KEY_IMAGES));
        }

        Log.v("orientation", orientation);
    }

    private void setPhotoImages(final ReadableArray images) {

        for (int i = 0; i < images.size(); i++) {
            ReadableMap map = images.getMap(i);
            PhotoImage image = new PhotoImage();

            if (map.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_IMAGE_URI)) {
                image.setUri(map.getString(KPGalleryConstant.KPPHOTO_GALLERY_KEY_IMAGE_URI));
            }

            // 设置通用缩放
            image.setMinScale(minScale);
            image.setMaxScale(maxScale);
            image.setDebug(debug);
            image.setMode(mode);

            if (map.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MINSCALE)) {
                // 如果单个image设置了缩放，则使用单独设置的值
                image.setMinScale((float) map.getDouble(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MINSCALE));
            }

            if (map.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MAXSCALE)) {
                // 如果单个image设置了缩放，则使用单独设置的值
                image.setMaxScale((float) map.getDouble(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MAXSCALE));
            }

            if (map.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MODE)) {
                // 如果单个image设置了模式，则使用单独设置的值
                image.setMode(map.getString(KPGalleryConstant.KPPHOTO_GALLERY_KEY_MODE));
            }

            if (map.hasKey(KPGalleryConstant.KPPHOTO_GALLERY_KEY_DEBUG)) {
                image.setDebug(map.getBoolean(KPGalleryConstant.KPPHOTO_GALLERY_KEY_DEBUG));
            }

            this.images.add(image);
        }
    }
}