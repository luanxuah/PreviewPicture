package com.example.previewpicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.photoview.PhotoActivity;
import java.util.ArrayList;

public class MainActivity extends Activity {
    //上下文对象
    private Context context;
    private GridView gridView;
    private ImageAdapter adapter;
    //缩略图集合
    private ArrayList<String> s_imagePaths;
    //原图集合
    private ArrayList<String> b_imagePaths;
    //缩略图
    String[] s_picArray = {"http://d040779c2cd49.scdn.itc.cn/s_w_z/pic/20161213/184474627873966848.jpg",
            "http://d040779c2cd49.scdn.itc.cn/s_w_z/pic/20161213/184474627999795968.jpg",
            "http://d040779c2cd49.scdn.itc.cn/s_w_z/pic/20161213/184474628071099136.jpg"
    };
    //原图
    String[] b_picArray = {"http://d040779c2cd49.scdn.itc.cn/s_big/pic/20161213/184474627873966848.jpg",
            "http://d040779c2cd49.scdn.itc.cn/s_big/pic/20161213/184474627999795968.jpg",
            "http://d040779c2cd49.scdn.itc.cn/s_big/pic/20161213/184474628071099136.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        init();
    }

    //初始化数据和控件
    private void init(){
        //准备数据
        s_imagePaths = new ArrayList<String>();
        b_imagePaths = new ArrayList<String>();
        for (int i=0; i<5; i++){
            s_imagePaths.add(s_picArray[0]);
            s_imagePaths.add(s_picArray[1]);
            s_imagePaths.add(s_picArray[2]);

            b_imagePaths.add(b_picArray[0]);
            b_imagePaths.add(b_picArray[1]);
            b_imagePaths.add(b_picArray[2]);
        }
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(context, s_imagePaths);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //图片的位置信息
                ArrayList<Rect> rects = new ArrayList<>();
                // 图片的地址
                ArrayList<String> tempData = new ArrayList<String>();
                //添加图片的地址
                tempData.add(b_imagePaths.get(position));
                //获取图片的位置信息
                ImageView imageView = (ImageView) view.findViewById(R.id.iv);
                Rect rect = new Rect();
                imageView.getGlobalVisibleRect(rect);
                rects.add(rect);
                //获取图片的bitmap
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("imagePaths", tempData);
                intent.putExtra("rect", rects);
                intent.putExtra("imageBitmap", bitmap);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });
    }
}
