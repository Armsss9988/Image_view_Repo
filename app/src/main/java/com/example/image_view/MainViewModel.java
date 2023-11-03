package com.example.image_view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.Adapter.ResourceListAdapter;


public class MainViewModel extends AndroidViewModel {
    MutableLiveData<Integer> currentResourcePos = new MutableLiveData<>();
    MutableLiveData<Integer> currentGalleryPos = new MutableLiveData<>();
    MutableLiveData<RecyclerView.Adapter> adapter = new MutableLiveData<>();
    MutableLiveData<Integer> previousResourcePos = new MutableLiveData<>();
    MutableLiveData<Integer> previousGalleryPos = new MutableLiveData<>();
    MutableLiveData<ImageAdapter> imageAdapter = new MutableLiveData<>();

    public MutableLiveData<ImageAdapter> getImageAdapter() {
        return imageAdapter;
    }

    public void setImageAdapter(ImageAdapter imageAdapter) {
        this.imageAdapter.setValue(imageAdapter);
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.previousGalleryPos.setValue(-1);
        this.currentGalleryPos.setValue(0);
        this.currentResourcePos.setValue(0);
        this.previousResourcePos.setValue(-1);
    }
    public MutableLiveData<RecyclerView.Adapter> getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter.setValue(adapter);
    }

    public MutableLiveData<Integer> getCurrentResourcePos() {
        return currentResourcePos;
    }

    public void setCurrentResourcePos(Integer currentResourcePos) {
        this.currentResourcePos.setValue(currentResourcePos);
    }

    public MutableLiveData<Integer> getCurrentGalleryPos() {
        return currentGalleryPos;
    }

    public void setCurrentGalleryPos(Integer currentGalleryPos) {
        this.currentGalleryPos.setValue(currentGalleryPos);
    }

    public MutableLiveData<Integer> getPreviousResourcePos() {
        return previousResourcePos;
    }
    public Integer getPreviousResourcePosValue(){ return previousResourcePos.getValue();}
    public Integer getCurrentResourcePosValue(){ return currentResourcePos.getValue();}

    public void setPreviousResourcePos(Integer previousResourcePos) {
        this.previousResourcePos.setValue(previousResourcePos);
    }

    public MutableLiveData<Integer> getPreviousGalleryPos() {
        return previousGalleryPos;
    }

    public void setPreviousGalleryPos(Integer previousGalleryPos) {
        this.previousGalleryPos.setValue(previousGalleryPos);
    }

}
