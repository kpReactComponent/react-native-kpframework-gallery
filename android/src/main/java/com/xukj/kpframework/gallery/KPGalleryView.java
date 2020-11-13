package com.xukj.kpframework.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;

public class KPGalleryView extends RelativeLayout {
    private static final String TAG = "KPGalleryView";

    private ViewPagerBar mHeader;
    private KPViewPager mViewPager;
    private ViewPageTextView mPageText;
    private SeekBar mSeekBar;
    private GestureDetector mGestureDetector;
    private ReactContext mContext;
    private ScreenSlidePagerAdapter mAdapter;

    // 数据
    private Intent mIntent;

    // gallery图片
    private ArrayList<PhotoImage> mImages = new ArrayList<>();
    private int mPosition = 0;
    private boolean useSeek = false;

    public KPGalleryView(ReactContext context) {
        super(context);
        this.init(context);
    }

    private void init(ReactContext context) {
        this.mContext = context;
        inflate(context, R.layout.view_pager, this);
        setBackgroundColor(Color.BLACK);
        setHeaderConfiguration();
        setViewPagerConfiguration();
        setSeekConfig();
        setGestureConfig();
    }

    private void setOrientatoinConfiguration() {
        Bundle bundle = mIntent.getExtras();
        String orientation = bundle.getString("orientation");
        // TODO
//        if (orientation.equals("portrait")) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        else if (orientation.equals("landscape")) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        }
//        else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        }
    }


    private void setDefaultConfiguration() {
        Bundle bundle = mIntent.getExtras();
        mPosition = bundle.getInt("index");
        mImages = bundle.getParcelableArrayList("images");
        // 防止越界
        mPosition = (mImages.size() > mPosition && mPosition >= 0) ? mPosition : 0;
        useSeek = bundle.getBoolean("seek");
        mAdapter.notifyDataSetChanged();
        changeTitle();
        mSeekBar.setMax(mImages.size() - 1 >= 0 ? mImages.size() - 1 : 0);
        mSeekBar.setProgress(mPosition);

        // 如果设置了默认初始化的index，则这里需要移动pager
        if (mPosition > 0) {
            mViewPager.setCurrentItem(mPosition);
        }
    }

    private void setHeaderConfiguration() {
        // 高度
        mHeader = findViewById(R.id.header);
        mHeader.getTitleBarLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseClick();
            }
        });
    }

    private void setViewPagerConfiguration() {
        mAdapter = new ScreenSlidePagerAdapter(mContext);
        mViewPager = findViewById(R.id.horizontal_pager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new KPViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;

                WritableMap params = Arguments.createMap();
                params.putInt(KPGalleryConstant.KPPHOTO_GALLERY_KEY_INDEX, position);
                sendEventToJS(KPGalleryConstant.KPPHOTO_GALLERY_EVENT_ONPAGECHANGED, params);

                if (mPosition != mSeekBar.getProgress()) {
                    mSeekBar.setProgress(mPosition);
                }
                changeTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mImages.size() > mPosition) {
            mViewPager.setCurrentItem(mPosition);
        }
    }

    private void setSeekConfig() {
        mPageText = findViewById(R.id.notice);
        mPageText.getTitleText().setText(String.valueOf(mPosition + 1));
        mPageText.setVisibility(View.INVISIBLE);

        mSeekBar = findViewById(R.id.seekbar);
        mSeekBar.setMax(mImages.size() - 1 >= 0 ? mImages.size() - 1 : 0);
        mSeekBar.setProgress(mPosition);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPageText.getTitleText().setText(String.valueOf(i + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPageText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPageText.setVisibility(View.INVISIBLE);
                mViewPager.setCurrentItem(seekBar.getProgress(), true);
            }
        });
    }

    private void setGestureConfig() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (useSeek) {
                    mSeekBar.setVisibility(mSeekBar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

    }

    private void changeTitle() {
        mHeader.getTitleBarTitle().setText((mPosition + 1) + " / " + mImages.size());
        mHeader.requestLayout();
    }

    /**
     * 发送JS通知
     *
     * @param name   key
     * @param params 数据
     */
    private void sendEventToJS(String name, @Nullable WritableMap params) {
        mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, params);
    }

    private class ScreenSlidePagerAdapter extends PagerAdapter {

        private Context adapterContext;

        public ScreenSlidePagerAdapter(Context context) {
            super();
            this.adapterContext = context;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            KPGalleryViewItem item = new KPGalleryViewItem(adapterContext);
            container.addView(item);
            item.loadPhotoImage(mImages.get(position));
            return item;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void onCloseClick() {
        sendEventToJS(KPGalleryConstant.KPPHOTO_GALLERY_EVENT_ONCLOSEPRESS, null);
    }

    public void setmIntent(Intent mIntent) {
        this.mIntent = mIntent;
        setOrientatoinConfiguration();
        setDefaultConfiguration();
    }
}
