package com.example.image_view.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image_view.MainActivity;
import com.example.image_view.R;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder> {
    private int[] resourceImages;
    private Context context;
    private ArrayList<String> galleryImages;
    public ImageListAdapter(Context context, ArrayList<String> galleryImages){
        this.galleryImages = galleryImages;
        this.resourceImages = null;
        this.context = context;
    }
    public ImageListAdapter(Context context,int[] resourceImages){
        this.resourceImages = resourceImages;
        this.galleryImages = null;
        this.context = context;
    }
    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_view,parent,false);
        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder holder, int position) {
        if(galleryImages != null)holder.imageView.setImageURI(Uri.parse(galleryImages.get(position)));
        else holder.imageView.setImageResource(resourceImages[position]);
        MainActivity main = (MainActivity) context;
        holder.imageView.setOnClickListener(view -> {
            Log.i(TAG, "onBindViewHolder: " + main.viewPager.getCurrentItem());
            main.viewPager.setCurrentItem(position);

        });
    }

    @Override
    public int getItemCount() {
        return (galleryImages != null) ? galleryImages.size() : resourceImages.length;
    }
    public static class ImageListViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
        public ImageListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_list_view);
        }
    }
}
