package com.xukj.kpframework.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPageTextView extends RelativeLayout {

    private TextView titleText;

    public ViewPageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_pager_text, this, true);
        titleText = findViewById(R.id.view_pager_text_content);
    }

    public TextView getTitleText() {
        return titleText;
    }
}
