package com.example.image_view.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image_view.MainActivity;
import com.example.image_view.R;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder> {
    private Integer[] resourceImages;
    private Context context;
    private ArrayList<String> galleryImages;
    public int previousExpandedPosition = 0;
    public int mExpandedPosition = 0;
    public ImageListAdapter(Context context, ArrayList<String> galleryImages){
        this.galleryImages = galleryImages;
        this.resourceImages = null;
        this.context = context;
    }
    public ImageListAdapter(Context context,Integer[] resourceImages){
        this.resourceImages = resourceImages;
        this.galleryImages = null;
        this.context = context;
    }
    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(galleryImages != null)holder.imageView.setImageURI(Uri.parse(galleryImages.get(position)));
        else holder.imageView.setImageResource(resourceImages[position]);
        holder.cardView.setBackgroundResource(R.drawable.card_view_style);
        MainActivity main = (MainActivity) context;
        MainActivity.NextPevFragment fragment = (MainActivity.NextPevFragment) main.getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        ViewGroup.LayoutParams expandedView = holder.cardView.getLayoutParams();
        View viewMain = main.getWindow().getDecorView();
        final boolean isExpanded = (position == mExpandedPosition);
        viewMain.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                viewMain.removeOnLayoutChangeListener(this);
                if(main.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    expandedView.width = viewMain.getWidth()/10;
                    expandedView.height = viewMain.getHeight()/20;
                    if(isExpanded){
                        expandedView.width = viewMain.getWidth()/5;
                        holder.cardView.setLayoutParams(expandedView);
                    }
                }
                else{
                    expandedView.width = viewMain.getWidth()/20;
                    expandedView.height = viewMain.getHeight()/10;
                    if(isExpanded){
                        expandedView.width = viewMain.getWidth()/10;
                        holder.cardView.setLayoutParams(expandedView);
                    }
                }


            }
        });

        holder.cardView.setLayoutParams(expandedView);
        if (isExpanded) previousExpandedPosition = position;
        holder.cardView.setOnClickListener(view -> {
            if(!isExpanded) mExpandedPosition = position;
            main.viewPager.setCurrentItem(position);
            fragment.ScrollCenterItem(position);
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return (galleryImages != null) ? galleryImages.size() : resourceImages.length;
    }
    public static class ImageListViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView, expanded;
            CardView cardView;

        public ImageListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_list_view);
            cardView = itemView.findViewById(R.id.card_view_item);
        }
    }

}
