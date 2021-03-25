package com.xukj.kpframework.gallery;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class KPGalleryViewItem extends RelativeLayout {

    private SubsamplingScaleImageView mImageView;
    private ProgressBar mProgress;
    private TextView mTextView;
    private ImageView mGIF;

    public KPGalleryViewItem(Context context) {
        super(context);
        this.init(context, null);
    }

    public KPGalleryViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KPGalleryViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_pager_page, this);
        mImageView = this.findViewById(R.id.imageView);
        mProgress = this.findViewById(R.id.loading);
        mTextView = this.findViewById(R.id.info);
        mGIF = this.findViewById(R.id.ivGif);
    }

    public void loadPhotoImage(final PhotoImage image) {
        if (image == null || image.getUri() == null) return;
        mProgress.setVisibility(View.VISIBLE);
        mTextView.setText("");
        mTextView.setVisibility(View.GONE);
        RequestOptions options = new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Glide.with(this).asFile().load(image.getUri())
                .apply(options)
                .listener(new RequestListener<File>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        useErrorTextView();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        boolean isGif = false;
                        try {
                            isGif = PhotoImage.isGif(resource.getPath());
                        } catch (Exception e) {
                            // do nothing
                        }

                        if (isGif) {
                            useGifImageView(resource, image);
                        }
                        else {
                            useNormalImageView(resource, image);
                        }

                        return false;
                    }
                }).submit();

    }

    private void useErrorTextView() {
        mProgress.setVisibility(View.GONE);
        mTextView.setText("图片加载失败");
        mTextView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
        mGIF.setVisibility(View.GONE);
    }

    private void useGifImageView(File resource, PhotoImage image) {
        mTextView.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mGIF.setVisibility(View.VISIBLE);
        Glide.with(this).asGif().load(resource).into(mGIF);
    }

    private void useNormalImageView(File resource, PhotoImage image) {
        String mode = image.getMode() != null ? image.getMode() : "inside";
        switch (mode) {
            case "custom":
                setCustomMode(resource, image);
                break;
            case "crop":
                setCropMode(resource, image);
                break;
            default:
                setInsideMode(resource, image);
                break;
        }

        mTextView.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mGIF.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 高宽都在视图范围内
     */
    private void setInsideMode(File resource, PhotoImage image) {
        mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        mImageView.setDebug(image.isDebug());
        mImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
    }

    /**
     * 图片显示宽度等于视图宽度(如果图片原始宽度小于视图宽度则默认inside)
     *
     * @param resource
     */
    private void setCropMode(File resource, PhotoImage image) {
        DisplayMetrics displayMetrics = ScreenUtils.getDisplayMetrics(getContext());
        BitmapFactory.Options options = ScreenUtils.getImageFileOptions(resource);
        float scale = displayMetrics.widthPixels * 1f / options.outWidth;
        ImageViewState defaultState = null;
        if (options.outHeight > options.outWidth && options.outHeight * scale > displayMetrics.heightPixels) {
            // 大于屏幕的长图，需要把图片移到最上方
            defaultState = new ImageViewState(0, new PointF(0, 0), 0);
        }

        mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        mImageView.setMinScale(scale);
        mImageView.setDebug(image.isDebug());
        mImageView.setImage(ImageSource.uri(Uri.fromFile(resource)), defaultState);
    }

    /**
     * 自定义模式
     */
    private void setCustomMode(File resource, PhotoImage image) {
        mImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        mImageView.setMinScale(image.getMinScale());
        mImageView.setMaxScale(image.getMaxScale());
        mImageView.setDebug(image.isDebug());
        mImageView.setImage(ImageSource.uri(Uri.fromFile(resource)));
    }
}
