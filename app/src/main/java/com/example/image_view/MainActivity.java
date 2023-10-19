package com.example.image_view;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.MEDIA_MOUNTED;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.ImageView.GalleryView;
import com.example.image_view.ImageView.ResourceView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{




    public static class NextPevFragment extends DialogFragment {
    Button btnLeft, btnRight, btnSwitchView;
        public void prev(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }

        public void next(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            Log.i(TAG, "next: " + viewPager.getCurrentItem());
        }

        void FixSizeView(View view , int width, int height){
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width/5;
            layoutParams.height = height/4*3;
            view.setLayoutParams(layoutParams);
        }




        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.next_prev_btn,container,false);
            btnLeft = v.findViewById(R.id.btn_left);
            btnRight = v.findViewById(R.id.btn_right);
/*            btnGallery = v.findViewById(R.id.btn_gallery);*/
            btnSwitchView = v.findViewById(R.id.btn_view_gallery);
            MainActivity main = (MainActivity) this.getContext();
            View mainView = main.getWindow().getDecorView();
            int width = mainView.getWidth();
            int height = mainView.getHeight();
            FixSizeView(btnLeft,width,height);
            FixSizeView(btnRight,width,height);

            
            btnLeft.setOnClickListener(view -> prev(main.viewPager));
            btnRight.setOnClickListener(view -> next(main.viewPager));
/*            btnGallery.setOnClickListener(view -> {
                main.mGetContent.launch("image/*");

            });*/
            btnSwitchView.setOnClickListener(view -> {
                if(btnSwitchView.getText() == getResources().getString(R.string.gallery)){
                    main.checkPermissions();
                    Log.i(TAG, "checked");
                    if(main.permission){
                        main.galleryView.GetGallery();
                        btnSwitchView.setText("Resource");
                    }
                }
                else{
                    main.resourceView.GetImageResource();
                    btnSwitchView.setText(getResources().getString(R.string.gallery));
                }

            });
            return v;
        }


    }
    ViewPager viewPager;
    public Fragment fragmentScrollBtn;
    public FragmentManager fm;
    public ResourceView resourceView;
    public GalleryView galleryView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean permission = false;



    //Creating Object of ViewPagerAdapter
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing the ViewPager Object
        viewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        fragmentScrollBtn = new NextPevFragment();
        fm = getSupportFragmentManager();
        galleryView = new GalleryView(this,imageAdapter,viewPager);
        resourceView = new ResourceView(this,imageAdapter,viewPager);
        resourceView.GetImageResource();
        fm.beginTransaction().add(R.id.fragmentContainerView2, fragmentScrollBtn).detach(fragmentScrollBtn).commit();

    }
    private void checkPermissions() {
        int result= ContextCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE);
        if(result != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
            else{
            ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            }
        }
        else{
            permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            boolean accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(accepted){
                permission = true;
                Toast.makeText(this,"You have accepted the permission", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"You have dined the permission", Toast.LENGTH_LONG).show();
            }
        }else{

        }
    }

    /*ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                Bitmap bitmap = null;
                ContentResolver contentResolver = getContentResolver();
                try {
                    if(Build.VERSION.SDK_INT < 28) {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                        bitmap = ImageDecoder.decodeBitmap(source);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(bitmap!=null){
                    Calendar c = Calendar.getInstance();
                    String new_Date = c.get(Calendar.DAY_OF_MONTH) + "-"
                            + ((c.get(Calendar.MONTH)) + 1) + "-"
                            + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR)
                            + "-" + c.get(Calendar.MINUTE) + "-"
                            + c.get(Calendar.SECOND);

                    try{
                        MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,new_Date,"image:"+new_Date);
                        Log.i(TAG, "onActivityResult: img saved");
                    }catch (Exception e){
                        Log.i(TAG, "onActivityResult: img cant saved");
                    }

                }
            });*/


}