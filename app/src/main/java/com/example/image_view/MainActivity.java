package com.example.image_view;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.Adapter.ImageListAdapter;
import com.example.image_view.ImageView.GalleryView;
import com.example.image_view.ImageView.ResourceView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    public ViewPager viewPager;
    public NextPevFragment fragmentScrollBtn;
    public FragmentManager fm;
    public FragmentTransaction transaction;
    public ResourceView resourceView;
    public GalleryView galleryView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String permissionString;


    //Creating Object of ViewPagerAdapter
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionString = Build.VERSION.SDK_INT >= 33 ?  READ_MEDIA_IMAGES : READ_EXTERNAL_STORAGE;
        //Initializing the ViewPager Object
        viewPager = findViewById(R.id.viewPagerMain);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        galleryView = new GalleryView(this,imageAdapter,viewPager);
        resourceView = new ResourceView(this,imageAdapter,viewPager);
        resourceView.GetImageResource();
        if(fm.findFragmentById(R.id.fragmentContainerView2) == null){
            Log.i(TAG, "onCreate: no fragment");
            fragmentScrollBtn = new NextPevFragment();
            transaction.add(R.id.fragmentContainerView2, fragmentScrollBtn);
        }
        else{
            Log.i(TAG, "onCreate: have fragment");
            fragmentScrollBtn = (NextPevFragment) fm.findFragmentById(R.id.fragmentContainerView2);
            /*transaction.remove(fragmentScrollBtn);
            transaction.add(R.id.fragmentContainerView2, fragmentScrollBtn);*/
        }
        transaction.commit();

    }
    private void checkPermissions() {
        String permissionString = Build.VERSION.SDK_INT >= 33 ?  READ_MEDIA_IMAGES : READ_EXTERNAL_STORAGE;
        if(isHasPermission()){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionString)) {
                Toast.makeText(this,"Rational",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{permissionString},PERMISSION_REQUEST_CODE);
            }
            else{
                Toast.makeText(this,"Request Second Permission",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{permissionString},PERMISSION_REQUEST_CODE);
            }
        }
        else{
            Toast.makeText(this,"Request New Permission",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{permissionString},PERMISSION_REQUEST_CODE);
        }
    }
    public boolean isHasPermission(){
      return ContextCompat.checkSelfPermission(this,permissionString) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            boolean accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(accepted){
                Toast.makeText(this,"You have accepted the permission", Toast.LENGTH_LONG).show();
                galleryView.GetGallery();
                fragmentScrollBtn.btnSwitchView.setText(R.string.resource);
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
    public static class NextPevFragment extends DialogFragment {
        public Button btnLeft, btnRight, btnSwitchView;
        RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        LinearLayout listPanel;
        private final String BUTTON_VALUE_KEY = "Btn";
        Parcelable listViewState = null;
        ImageListAdapter adapterView = null;
        private final String LIST_VIEW_KEY = "listKey";
        private final String ADAPTER_VIEW_KEY = "adapterKey";
        public void prev(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }

        public void next(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            Log.i(TAG, "next: " + viewPager.getCurrentItem());
        }

        void FixSizeView(View view , int width, int height){
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            Log.i(TAG, "onSave: firing");
            outState.putString(BUTTON_VALUE_KEY,btnSwitchView.getText().toString());
                listViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                outState.putParcelable(LIST_VIEW_KEY,listViewState);

            super.onSaveInstanceState(outState);
        }

        @Override
        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
            Log.i(TAG, "onRestore: firing ");
            if(savedInstanceState != null){
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    listViewState = savedInstanceState.getParcelable(LIST_VIEW_KEY);
                    Log.i(TAG, "onRestore: get Layout");
                }
            }
        }


        @Override
        public void onResume() {
            super.onResume();
                Log.i(TAG, "onResume: firing");
                if(listViewState != null){
                    Log.i(TAG, "onResume: find list view");
                    recyclerView.getLayoutManager().onRestoreInstanceState(listViewState);
                }
                else Log.i(TAG, "onResume: cant find listview");
        }

        void GetAppSize(View view){
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    view.removeOnLayoutChangeListener(this);
                    FixSizeView(btnLeft,view.getWidth()/4,view.getHeight()/4);
                    FixSizeView(btnRight,view.getWidth()/4,view.getHeight()/4);
                    FixSizeView(listPanel,view.getWidth(),view.getHeight()/9);
                }
            });
        };


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.next_prev_btn,container,false);
            MainActivity main = (MainActivity) container.getContext();
            View mainView = main.getWindow().getDecorView();
            btnLeft = v.findViewById(R.id.btn_left);
            btnRight = v.findViewById(R.id.btn_right);
            recyclerView = v.findViewById(R.id.list_item);
            btnSwitchView = v.findViewById(R.id.btn_view_gallery);
            listPanel = v.findViewById(R.id.list_panel);
            GetAppSize(mainView);
            linearLayoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new ImageListAdapter(main,main.resourceView.GetImages()));
            if(savedInstanceState != null) {
                btnSwitchView.setText(savedInstanceState.getString(BUTTON_VALUE_KEY));
                if ((btnSwitchView.getText().toString().equals(getResources().getString(R.string.gallery)))) {
                    Log.i(TAG, "onCreateView: show Recourse");
                    main.resourceView.GetImageResource();
                    recyclerView.setAdapter(new ImageListAdapter(main,main.resourceView.GetImages()));
                } else {
                    Log.i(TAG, "onCreateView: show Gallery");
                    main.galleryView.GetGallery();
                    recyclerView.setAdapter(new ImageListAdapter(main,main.galleryView.GetImages()));
                }
            }

            /*            btnGallery = v.findViewById(R.id.btn_gallery);*/
            btnLeft.setOnClickListener(view -> prev(main.viewPager));
            btnRight.setOnClickListener(view -> next(main.viewPager));
/*            btnGallery.setOnClickListener(view -> {
                main.mGetContent.launch("image/*");

            });*/
            btnSwitchView.setOnClickListener(view -> {
                if(btnSwitchView.getText() == getResources().getString(R.string.gallery)){
                    if(!main.isHasPermission()){
                        main.checkPermissions();
                    }
                    else{
                        main.galleryView.GetGallery();
                        btnSwitchView.setText(R.string.resource);
                        recyclerView.setAdapter(new ImageListAdapter(main,main.galleryView.GetImages()));
                    }
                }
                else{
                    main.resourceView.GetImageResource();
                    btnSwitchView.setText(R.string.gallery);
                    recyclerView.setAdapter(new ImageListAdapter(main,main.resourceView.GetImages()));
                }

            });
            return v;
        }


    }


}