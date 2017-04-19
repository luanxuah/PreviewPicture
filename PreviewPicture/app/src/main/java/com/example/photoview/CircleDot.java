package com.example.photoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.previewpicture.R;

import java.util.ArrayList;
import java.util.List;

public class CircleDot extends View {


	private int count = 0;
	
	private Context context = null;
	
	private LinearLayout lt = null;
	
	private List<ImageView> dotList = null;
	
	
	public CircleDot(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleDot(Context context, LinearLayout lt, int courseCount, int current) {
		
		super(context);
		
		this.count = courseCount;
		
		this.context = context;
		
		this.lt = lt;
		
		dotList = new ArrayList<ImageView>();
		
		addDot(current);
	}
	
	 private void addDot(int current){
		 
		 LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(
				 LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);;
		 ImageView dot = null;
		 for (int i = 0; i < count; i++) {
			 dot = new ImageView(context);
		     if (i == current) {
				dot.setImageResource(R.mipmap.bg_indicat_select);
			}else{
				dot.setImageResource(R.mipmap.bg_indicat_no);
			}
			 
			dot.setPadding(10, 5, 10, 5);
			lt.addView(dot, params);
			dotList.add(dot);
		}
	 }
	
	public void selected(int selectPosition){
		for (int i = 0; i < count; i++) {
			if (selectPosition == i) {
				dotList.get(i).setImageResource(R.mipmap.bg_indicat_select);
			}else{
				dotList.get(i).setImageResource(R.mipmap.bg_indicat_no);
			}
		}
	}
}
