package com.xukj.kpframework.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xukj.kpframework.gallery.R;

public class ViewPagerBar extends RelativeLayout {
    private Button titleBarLeftBtn;
    private Button titleBarRightBtn;
    private TextView titleBarTitle;

    public ViewPagerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_pager_bar, this, true);
        titleBarLeftBtn = findViewById(R.id.title_bar_left);
        titleBarRightBtn = findViewById(R.id.title_bar_right);
        titleBarTitle = findViewById(R.id.title_bar_title);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerBar);
        if (attributes != null) {
            //处理titleBar背景色
            int titleBarBackGround = attributes.getResourceId(R.styleable.ViewPagerBar_title_background_color, Color.GREEN);
            setBackgroundResource(titleBarBackGround);
            //先处理左边按钮
            //获取是否要显示左边按钮
            boolean leftButtonVisible = attributes.getBoolean(R.styleable.ViewPagerBar_left_button_visible, true);
            if (leftButtonVisible) {
                titleBarLeftBtn.setVisibility(View.VISIBLE);
            } else {
                titleBarLeftBtn.setVisibility(View.INVISIBLE);
            }
            //设置左边按钮的文字
            String leftButtonText = attributes.getString(R.styleable.ViewPagerBar_left_button_text);
            if (!TextUtils.isEmpty(leftButtonText)) {
                titleBarLeftBtn.setText(leftButtonText);
                //设置左边按钮文字颜色
                int leftButtonTextColor = attributes.getColor(R.styleable.ViewPagerBar_left_button_text_color, Color.WHITE);
                titleBarLeftBtn.setTextColor(leftButtonTextColor);
            }
            //设置左边图片icon 这里是二选一 要么只能是文字 要么只能是图片
            int leftButtonDrawable = attributes.getResourceId(R.styleable.ViewPagerBar_left_button_drawable, -1);
            if (leftButtonDrawable != -1) {
                titleBarLeftBtn.setCompoundDrawablesWithIntrinsicBounds(leftButtonDrawable, 0, 0, 0);  //设置到哪个控件的位置（）
            }


            //处理标题
            //先获取标题是否要显示图片icon
            int titleTextDrawable = attributes.getResourceId(R.styleable.ViewPagerBar_title_text_drawable, -1);
            if (titleTextDrawable != -1) {
                titleBarTitle.setBackgroundResource(titleTextDrawable);
            } else {
                //如果不是图片标题 则获取文字标题
                String titleText = attributes.getString(R.styleable.ViewPagerBar_title_text);
                if (!TextUtils.isEmpty(titleText)) {
                    titleBarTitle.setText(titleText);
                }
                //获取标题显示颜色
                int titleTextColor = attributes.getColor(R.styleable.ViewPagerBar_title_text_color, Color.WHITE);
                titleBarTitle.setTextColor(titleTextColor);
            }

            //先处理右边按钮
            //获取是否要显示右边按钮
            boolean rightButtonVisible = attributes.getBoolean(R.styleable.ViewPagerBar_right_button_visible, true);
            if (rightButtonVisible) {
                titleBarRightBtn.setVisibility(View.VISIBLE);
            } else {
                titleBarRightBtn.setVisibility(View.INVISIBLE);
            }
            //设置右边按钮的文字
            String rightButtonText = attributes.getString(R.styleable.ViewPagerBar_right_button_text);
            if (!TextUtils.isEmpty(rightButtonText)) {
                titleBarRightBtn.setText(rightButtonText);
                //设置右边按钮文字颜色
                int rightButtonTextColor = attributes.getColor(R.styleable.ViewPagerBar_right_button_text_color, Color.WHITE);
                titleBarRightBtn.setTextColor(rightButtonTextColor);
            }
            //设置右边图片icon 这里是二选一 要么只能是文字 要么只能是图片
            int rightButtonDrawable = attributes.getResourceId(R.styleable.ViewPagerBar_right_button_drawable, -1);
            if (rightButtonDrawable != -1) {
                titleBarRightBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightButtonDrawable, 0);  //设置到哪个控件的位置（）
            }
            attributes.recycle();
        }
    }

    public void setTitleClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            titleBarLeftBtn.setOnClickListener(onClickListener);
            titleBarRightBtn.setOnClickListener(onClickListener);
        }
    }

    public Button getTitleBarLeftBtn() {
        return titleBarLeftBtn;
    }

    public Button getTitleBarRightBtn() {
        return titleBarRightBtn;
    }

    public TextView getTitleBarTitle() {
        return titleBarTitle;
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    // 修复手动添加子view大小不正确
    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };
}
