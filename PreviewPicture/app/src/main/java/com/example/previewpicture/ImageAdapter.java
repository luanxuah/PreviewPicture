package com.example.previewpicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;

/**
 * @author: luanxu
 * @createTime: on 2017/4/18 14:54
 * @className:
 * @description:
 * @changed by:
 */
public class ImageAdapter extends BaseAdapter{
    //上下文对象
    private Context context;
    //图片地址
    private List<String> imagePaths;

    public ImageAdapter(Context context, List<String> imagePaths){
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image, null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LoaderImageUtil.displayFromNet(imagePaths.get(position), R.mipmap.ic_launcher, viewHolder.iv);
        return convertView;
    }

    static class ViewHolder{
        ImageView iv;
    }

}
