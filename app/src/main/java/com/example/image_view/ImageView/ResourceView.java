package com.example.image_view.ImageView;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;

import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ResourceView {
    private int[] images;
    ImageAdapter imageAdapter;
    ViewPager viewPager;
    Context context;
    public ResourceView(Context context, ImageAdapter imageAdapter, ViewPager viewPager){
        this.context = context;
        this.imageAdapter = imageAdapter;
        this.viewPager = viewPager;
    }
    public int[] GetImages(){
        return images;
    }
    public void SetImage(int[] images){
        this.images = images;
    }
    public void GetImageResource(){

        images = GetImageArray();

        //Initializing the ViewPagerAdapter
        imageAdapter = new ImageAdapter(context, images);

        //Adding the Adapter to the ViewPager
        viewPager.setAdapter(imageAdapter);
    }
    int[] GetImageArray(){
        Field[] ID_Fields = R.drawable.class.getDeclaredFields();
        int[] resArray = new int[ID_Fields.length];
        for(int i = 0; i < ID_Fields.length; i++) {
            try {
                resArray[i] = ID_Fields[i].getInt(null);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resArray;
    }
}
