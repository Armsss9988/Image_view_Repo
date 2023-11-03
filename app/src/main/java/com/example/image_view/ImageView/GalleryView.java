package com.example.image_view.ImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.MEDIA_MOUNTED;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.image_view.Adapter.ImageAdapter;

import java.util.ArrayList;

public class GalleryView{

    private ArrayList<String> images;
    private static GalleryView instance = null;
    public GalleryView(Context context){
        this.images = loadImages(context);
    }
    public static synchronized GalleryView getInstance(Context context){
        if(instance == null){
            instance = new GalleryView(context);
        }
        return instance;
    }

    public ArrayList<String> GetImages(){
        return images;
    }

    private ArrayList<String> loadImages(Context context) {
        ArrayList<String> imgLoaded = new ArrayList<>();
        boolean SDCard = Environment.getExternalStorageState().equals(MEDIA_MOUNTED);
        Log.i(TAG, "loadImages: start");
        if (SDCard) {
            Log.i(TAG, "loadImages: It has SD");
            final String[] colums = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String order = MediaStore.Images.Media.DATE_TAKEN + " DESC";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, colums, null, null, order);
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int colunmindex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imgLoaded.add(cursor.getString(colunmindex));
            }
        }
        return imgLoaded;
    }
}
