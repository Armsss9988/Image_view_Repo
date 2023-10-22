package com.example.image_view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.image_view.MainActivity;
import com.example.image_view.R;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ImageAdapter extends PagerAdapter {

    //Context object
    Context context;

    //Array of image
    public ArrayList<String> dataImg;
    public Integer[] images;

    //Layout Inflater
    LayoutInflater mLayoutInflater;
    PhotoView imageView;

    //Viewpager Constructor
    public ImageAdapter(Context context, Integer[] images) {
        this.context = context;
        this.images = images;
        this.dataImg = null;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public ImageAdapter(Context context, ArrayList<String> dataImg) {
        this.context = context;
        this.dataImg = dataImg;
        this.images = null;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return the number of images
        if(images!=null){
            return images.length;
        }
        else return dataImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);
        //referencing the image view from the item.xml file
        imageView = itemView.findViewById(R.id.imageViewMain);
        MainActivity main =(MainActivity) container.getContext();
        View recView;
        //setting the image in the imageView
        if(images != null){
            imageView.setImageResource(images[position]);
        }
        else{
            File newImg = new File(dataImg.get(position));
            Glide.with(context).load(newImg).into(imageView);
        }
        imageView.setMaximumScale(15f);
        imageView.setMediumScale(4f);
        imageView.setOnClickListener(view -> {
            Fragment fragment = main.fragmentScrollBtn;
            FragmentManager fm = main.fm;
            FragmentTransaction transaction = fm.beginTransaction();;
            if(fragment.isVisible()){
                transaction
                        .hide(fragment);
            }else{
                transaction
                        .show(fragment);
            }
            transaction.commit();
        });

        //Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}
