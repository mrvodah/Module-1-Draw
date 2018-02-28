package com.example.qklahpita.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qklahpita on 2/4/18.
 */

public class GridImageAdapter extends BaseAdapter {
    List<ImageModel> imageModelList = ImageUtils.getListImage();
    Context context;

    public GridImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //1. create image view
        ImageView imageView = new ImageView(context);

        imageView.setLayoutParams(
                new AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5, 5, 5, 5);
        imageView.setCropToPadding(true);

        //2. set image
        Bitmap bitmap = BitmapFactory.decodeFile(imageModelList.get(i).path);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }
}
