package com.example.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.previewpicture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: luanxu
 * @createTime: 2017/4/17 15:19
 * @description: 图片预览页面
 * @changed by:
 */
public class PhotoActivity extends FragmentActivity {
    //上下文对象
    private Context context;

    private boolean isTransformOut = false;
    //图片的地址
    private List<String> imgUrls;
    //图片的位置集合
    private List<Rect> rects;
    //图片的bitmap
    private Bitmap bitmap;
    //当前图片的位置
    private int currentIndex;
    //图片的展示的Fragment
    private List<PhotoFragment> fragments = new ArrayList<>();

    //小圆点
    private CircleDot cirDot;

    //展示图片的viewPager
    private PhotoViewPager viewPager;
    //下方指示小圆点
    private LinearLayout ltAddDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        context = this;
        initDate();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        imgUrls = getIntent().getStringArrayListExtra("imagePaths");
        rects = getIntent().getParcelableArrayListExtra("rect");
        bitmap = getIntent().getParcelableExtra("imageBitmap");
        currentIndex = getIntent().getIntExtra("position", -1);
        if (imgUrls != null && rects != null && imgUrls.size() == rects.size()) {
            for (int i = 0; i < imgUrls.size(); i++) {
                PhotoFragment fragment = new PhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PhotoFragment.KEY_PATH, imgUrls.get(i));
                bundle.putParcelable(PhotoFragment.KEY_IMG_BITMAP, bitmap);
                bundle.putParcelable(PhotoFragment.KEY_START_BOUND, rects.get(i));
                bundle.putInt(PhotoFragment.KEY_POSITION, i);
                bundle.putBoolean(PhotoFragment.KEY_TRANS_PHOTO, currentIndex==i);
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        } else {
            finish();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        viewPager = (PhotoViewPager) findViewById(R.id.viewPager);
        ltAddDot = (LinearLayout) findViewById(R.id.ltAddDot);
        cirDot = new CircleDot(context, ltAddDot,imgUrls.size(), currentIndex);
        cirDot.selected(currentIndex);

        //viewPager的适配器
        PhotoPagerAdapter adapter = new PhotoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当被选中的时候设置小圆点和当前位置
                if (cirDot != null) {
                    cirDot.selected(position);
                }
                currentIndex = position;
                viewPager.setCurrentItem(currentIndex, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(currentIndex);

        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                final PhotoFragment fragment = fragments.get(currentIndex);
                fragment.transformIn();
            }
        });

    }

    //退出预览的动画
    public void transformOut() {
        if (isTransformOut) {
            return;
        }
        isTransformOut = true;
        int currentItem = viewPager.getCurrentItem();
        if (currentItem < rects.size()) {
            PhotoFragment fragment = fragments.get(currentItem);
            ltAddDot.setVisibility(View.GONE);
            fragment.changeBg(Color.TRANSPARENT);
            fragment.transformOut(new SmoothImageView.onTransformListener() {
                @Override
                public void onTransformCompleted(SmoothImageView.Status status) {
                    exit();
                }
            });
        } else {
            exit();
        }
    }

    /**
     * 关闭页面
     */
    private void exit() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            transformOut();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * pager的适配器
     */
    private class PhotoPagerAdapter extends FragmentPagerAdapter {

        PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
