package com.example.photoview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.previewpicture.LoaderImageUtil;
import com.example.previewpicture.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author: luanxu
 * @createTime: 2017/4/17 15:20
 * @description: 图片预览单个图片的frgment
 * @changed by:
 */
public class PhotoFragment extends LazyFragment {
    //上下文对象
    private PhotoActivity context;
    /**预览图片 类型*/
    public static int IMAGE_PREVIEW =100101;
    public static final String KEY_IMG_BITMAP = "img_bitmap";
    public static final String KEY_START_BOUND = "startBounds";
    public static final String KEY_TRANS_PHOTO = "is_trans_photo";
    public static final String KEY_POSITION = "key_position";
    public static final String KEY_PATH = "key_path";
    //图片地址
    private String imgUrl;
    // 是否是以动画进入的Fragment
    private boolean isTransPhoto = false;
    //图片的位置
    private int position;
    //缩略图的bitmap
    private Bitmap bitmap;

    //图片
    private SmoothImageView photoView;
    //图片的外部控件
    private View rootView;
    //进度条
    private ProgressBar loading;

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_layout, container, false);
        context = (PhotoActivity) getActivity();
        initView(view);
        initDate();

        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        loading = (ProgressBar) view.findViewById(R.id.loading);
        photoView = (SmoothImageView) view.findViewById(R.id.photoView);
        rootView = view.findViewById(R.id.rootView);
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            //图片的位置
            position = bundle.getInt(KEY_POSITION);
            //地址
            imgUrl = bundle.getString(KEY_PATH);
            //位置
            Rect startBounds = bundle.getParcelable(KEY_START_BOUND);
            photoView.setThumbRect(startBounds);
            //是否展示动画
            isTransPhoto = bundle.getBoolean(KEY_TRANS_PHOTO, false);
            bitmap = bundle.getParcelable(KEY_IMG_BITMAP);
            if (isTransPhoto){
                photoView.setImageBitmap(bitmap);
            }else{
                //加载缩略图
                LoaderImageUtil.displayFromNet(imgUrl, R.mipmap.ic_launcher, photoView);
            }
        }
        // 非动画进入的Fragment，默认背景为黑色
        if (!isTransPhoto) {
            rootView.setBackgroundColor(Color.BLACK);
        }
        photoView.setMinimumScale(1f);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (photoView.checkMinScale()) {
                    ((PhotoActivity) getActivity()).transformOut();
                }
            }
        });

        photoView.setAlphaChangeListener(new SmoothImageView.OnAlphaChangeListener() {
            @Override
            public void onAlphaChange(int alpha) {
                rootView.setBackgroundColor(getColorWithAlpha(alpha / 255f, Color.BLACK));
            }
        });

        photoView.setTransformOutListener(new SmoothImageView.OnTransformOutListener() {
            @Override
            public void onTransformOut() {
                if (photoView.checkMinScale()) {
                    ((PhotoActivity) getActivity()).transformOut();
                }
            }
        });
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    private boolean isLoaded = false;
    private boolean isLoadFinish = false;

    @Override
    protected void onLazy() {
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        //加载原图
        LoaderImageUtil.display(imgUrl, R.mipmap.ic_launcher, photoView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                loading.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        if (!isLoadFinish) {
                            loading.setVisibility(View.VISIBLE);
                        }
//                    }
//                }, 400);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                isLoadFinish = true;
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public void transformIn() {
        photoView.transformIn(new SmoothImageView.onTransformListener() {
            @Override
            public void onTransformCompleted(SmoothImageView.Status status) {
                rootView.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public void transformOut(SmoothImageView.onTransformListener listener) {
        photoView.transformOut(listener);
    }

    public void changeBg(int color) {
        rootView.setBackgroundColor(color);
    }

}
