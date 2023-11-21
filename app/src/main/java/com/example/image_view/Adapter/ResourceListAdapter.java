package com.example.image_view.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image_view.MainActivity;
import com.example.image_view.MainViewModel;
import com.example.image_view.R;

import java.util.ArrayList;

public class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.ImageListViewHolder> {
    private final Integer[] resourceImages;
    private final Context context;
    private final MainViewModel mainViewModel;

    public ResourceListAdapter(Context context,Integer[] resourceImages,MainViewModel mainViewModel){
        this.resourceImages = resourceImages;
        this.mainViewModel = mainViewModel;
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
        holder.imageView.setImageResource(resourceImages[position]);
        holder.cardView.setBackgroundResource(R.drawable.card_view_style);
        MainActivity main = (MainActivity) context;
        ViewGroup.LayoutParams expandedView = holder.cardView.getLayoutParams();
        View viewMain = main.getWindow().getDecorView();
        boolean isExpanded = (position == mainViewModel.getCurrentResourcePosValue());
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
        if (isExpanded) mainViewModel.setPreviousResourcePos(position);
        holder.cardView.setOnClickListener(view -> {
            if(!isExpanded) mainViewModel.setCurrentResourcePos(position);
            this.notifyItemChanged(mainViewModel.getPreviousResourcePosValue());
            this.notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return resourceImages.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ImageListViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            CardView cardView;

        public ImageListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_list_view);
            cardView = itemView.findViewById(R.id.card_view_item);
        }
    }

}
