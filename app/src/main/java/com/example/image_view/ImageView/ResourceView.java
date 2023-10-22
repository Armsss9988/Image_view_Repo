package com.example.image_view.ImageView;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

import androidx.viewpager.widget.ViewPager;

import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.R;
import com.github.chrisbanes.photoview.BuildConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ResourceView {
    private Integer[] images;
    ImageAdapter imageAdapter;
    ViewPager viewPager;
    Context context;
    public ResourceView(Context context, ImageAdapter imageAdapter, ViewPager viewPager){
        this.context = context;
        this.imageAdapter = imageAdapter;
        this.viewPager = viewPager;
    }
    public Integer[] GetImages(){
        return images;
    }
    public void SetImage(Integer[] images){
        this.images = images;
    }
    public void GetImageResource(){

        images = GetImageArray();

        //Initializing the ViewPagerAdapter
        imageAdapter = new ImageAdapter(context, images);

        //Adding the Adapter to the ViewPager
        viewPager.setAdapter(imageAdapter);
    }
    Integer[] GetImageArray(){
        Field[] ID_Fields = R.drawable.class.getDeclaredFields();
        ArrayList<Integer> resArray = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)$");
        for(int i = 0; i < ID_Fields.length; i++) {
            TypedValue value = new TypedValue();
            String filename = ID_Fields[i].getName();
            int rawId = context.getResources().getIdentifier(filename, "drawable", context.getPackageName());
            context.getResources().getValue(rawId, value, true);
            String type = value.toString().split(" ")[1];
            type = type.substring(14,type.length()-1);
            Log.i(TAG, "GetImageArray: "+ i + ": " + filename);
            Log.i(TAG, "GetImageArray: "+ i + ": " + rawId);
            Log.i(TAG, "GetImageArray: "+ i + ": " + type);
            try {
                if(pattern.matcher(type).find()){
                    resArray.add(ID_Fields[i].getInt(null));
                }

            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Integer[] intArr = resArray.toArray(new Integer[resArray.size()]);
        return intArr;
    }
}
