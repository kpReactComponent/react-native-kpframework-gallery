package com.xukj.kpframework.gallery;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.xukj.kpframework.gallery.R;
import com.xukj.kpframework.gallery.KPGalleryModule;

import java.util.ArrayList;


public class ViewPagerActivity extends AppCompatActivity {

    private ViewPagerBar mHeader;
    private KPViewPager mViewPager;
    private ViewPageTextView mPageText;
    private SeekBar mSeekBar;
    private GestureDetector mGestureDetector;

    // gallery图片
    private ArrayList<PhotoImage> mImages = new ArrayList<>();
    private int mPosition = 0;
    private boolean useSeek = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOrientatoinConfiguration();
        setContentView(R.layout.view_pager);

        setDefaultConfiguration();
        setHeaderConfiguration();
        setViewPagerConfiguration();
        setSeekConfig();
        setGestureConfig();
        changeTitle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void changeTitle() {
        mHeader.getTitleBarTitle().setText((mPosition + 1) + " / " + mImages.size());
    }

    private void setOrientatoinConfiguration() {
        Bundle bundle = getIntent().getExtras();
        String orientation = bundle.getString("orientation");
        if (orientation.equals("portrait")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (orientation.equals("landscape")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }


    private void setDefaultConfiguration() {
        Bundle bundle = getIntent().getExtras();
        mPosition = bundle.getInt("index");
        mImages = bundle.getParcelableArrayList("images");
        useSeek = bundle.getBoolean("seek");
    }

    private void setHeaderConfiguration() {
        // 高度
        mHeader = findViewById(R.id.header);
        mHeader.getTitleBarLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setViewPagerConfiguration() {
        // 分页
        mViewPager = findViewById(R.id.horizontal_pager);
        mViewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
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
        mPageText.setVisibility(View.GONE);

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
                mPageText.setVisibility(View.GONE);
                mViewPager.setCurrentItem(seekBar.getProgress(), true);
            }
        });
    }

    private void setGestureConfig() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (useSeek) {
                    mSeekBar.setVisibility(mSeekBar.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

    }

    @Override
    protected void onDestroy() {
        sendEventToJS(KPGalleryConstant.KPPHOTO_GALLERY_EVENT_ONCLOSE, null);
        super.onDestroy();
    }

    /**
     * 发送JS通知
     *
     * @param name   key
     * @param params 数据
     */
    private void sendEventToJS(String name, @Nullable WritableMap params) {
        ReactContext context = KPGalleryModule.context;
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, params);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ViewPagerFragment fragment = new ViewPagerFragment();
            fragment.setImage(mImages.get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }
}
