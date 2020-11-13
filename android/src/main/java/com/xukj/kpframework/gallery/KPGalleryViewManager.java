package com.xukj.kpframework.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;

public class KPGalleryViewManager extends SimpleViewManager<KPGalleryView> {
    private static final String TAG = "KPGalleryViewManager";
    public static final String REACT_CLASS = "KPGalleryView";

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

    private ThemedReactContext reactContext = null;


    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected KPGalleryView createViewInstance(ThemedReactContext reactContext) {
        this.reactContext = reactContext;
        return new KPGalleryView(reactContext);
    }

    @ReactProp(name="options")
    public void setOptions(KPGalleryView view, ReadableMap options) {
        setConfiguration(options);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("images", images);
        bundle.putInt("index", index);
        bundle.putString("orientation", orientation);
        bundle.putBoolean("seek", seek);
        intent.putExtras(bundle);
        view.setmIntent(intent);
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
