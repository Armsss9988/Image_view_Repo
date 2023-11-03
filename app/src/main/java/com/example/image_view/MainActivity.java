package com.example.image_view;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.image_view.Adapter.GalleryListAdapter;
import com.example.image_view.Adapter.ImageAdapter;
import com.example.image_view.Adapter.ResourceListAdapter;
import com.example.image_view.ImageView.GalleryView;
import com.example.image_view.ImageView.ResourceView;

public class MainActivity extends AppCompatActivity{

    public ViewPager viewPager;
    public NextPevFragment fragmentScrollBtn;
    public FragmentManager fm;
    public FragmentTransaction transaction;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String permissionString;
    public MainViewModel mainViewModel;
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        permissionString = Build.VERSION.SDK_INT >= 33 ?  READ_MEDIA_IMAGES : READ_EXTERNAL_STORAGE;
        viewPager = findViewById(R.id.viewPagerMain);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        viewPager.setAdapter(new ImageAdapter(this,ResourceView.getInstance(this).GetImages()));
        liveDataObserve();
        if(fm.findFragmentById(R.id.fragmentContainerView2) == null){
            Log.i(TAG, "onCreate: no fragment");
            fragmentScrollBtn = new NextPevFragment();
            transaction.add(R.id.fragmentContainerView2, fragmentScrollBtn);
        }
        else{
            Log.i(TAG, "onCreate: have fragment");
            fragmentScrollBtn = (NextPevFragment) fm.findFragmentById(R.id.fragmentContainerView2);
        }
        transaction.commit();

    }
    void liveDataObserve(){
        mainViewModel.getAdapter().observe(this, adapter -> {
            if(adapter instanceof ResourceListAdapter){
                viewPager.setAdapter(new ImageAdapter(this,ResourceView.getInstance(this).GetImages()));
                viewPager.setCurrentItem(mainViewModel.getCurrentResourcePos().getValue());
            }
            else {
                viewPager.setAdapter(new ImageAdapter(this,GalleryView.getInstance(this).GetImages()));
                viewPager.setCurrentItem(mainViewModel.getCurrentGalleryPos().getValue());
            }
        });
        mainViewModel.getCurrentResourcePos().observe(this, val ->{
            viewPager.setCurrentItem(val,true);
        });
        mainViewModel.getCurrentGalleryPos().observe(this, val ->{
            viewPager.setCurrentItem(val,true);
        });
        mainViewModel.getImageAdapter().observe(this, val -> {
            viewPager.setAdapter(imageAdapter);
        });
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
                mainViewModel.setAdapter(new GalleryListAdapter(this,GalleryView.getInstance(this).GetImages(), mainViewModel));
                fragmentScrollBtn.btnSwitchView.setText(R.string.resource);
            }else{
                Toast.makeText(this,"You have dined the permission", Toast.LENGTH_LONG).show();
            }
        }else{

        }
    }

    public static class NextPevFragment extends DialogFragment {
        public Button btnLeft, btnRight, btnSwitchView;
        public RecyclerView recyclerView;
        public LinearLayoutManager linearLayoutManager;
        public LinearLayout listPanel;
        public MainViewModel mainViewModel;

        public void prev() {
            if(recyclerView.getAdapter() instanceof ResourceListAdapter){
                mainViewModel.setPreviousResourcePos(mainViewModel.getCurrentResourcePos().getValue());
                mainViewModel.setCurrentResourcePos(mainViewModel.getCurrentResourcePos().getValue()-1);
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getCurrentResourcePos().getValue());
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getPreviousResourcePos().getValue());
            }
            else{
                mainViewModel.setPreviousGalleryPos(mainViewModel.getCurrentGalleryPos().getValue());
                mainViewModel.setCurrentGalleryPos(mainViewModel.getCurrentGalleryPos().getValue()-1);
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getCurrentGalleryPos().getValue());
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getPreviousGalleryPos().getValue());
            }
        }
        public void next() {
            if(recyclerView.getAdapter() instanceof ResourceListAdapter){
                mainViewModel.setPreviousResourcePos(mainViewModel.getCurrentResourcePos().getValue());
                mainViewModel.setCurrentResourcePos(mainViewModel.getCurrentResourcePos().getValue()+1);
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getCurrentResourcePos().getValue());
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getPreviousResourcePos().getValue());
            }
            else{
                mainViewModel.setPreviousGalleryPos(mainViewModel.getCurrentGalleryPos().getValue());
                mainViewModel.setCurrentGalleryPos(mainViewModel.getCurrentGalleryPos().getValue()+1);
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getCurrentGalleryPos().getValue());
                recyclerView.getAdapter().notifyItemChanged(mainViewModel.getPreviousGalleryPos().getValue());
            }
        }
        void FixSizeView(View view , int width, int height){
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
        public void ScrollCenterItem(int itemToScroll) {
            View mainView = this.getActivity().getWindow().getDecorView();
            mainView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mainView.removeOnLayoutChangeListener(this);
                    try{
                        int centerOfScreen;
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                            centerOfScreen = mainView.getWidth() / 2 - (mainView.getWidth()/10);
                        }
                        else{
                            centerOfScreen = mainView.getWidth() / 2 - (mainView.getWidth()/20);
                        }

                        linearLayoutManager.scrollToPositionWithOffset(itemToScroll, centerOfScreen);
                    }
                    catch (Exception e){
                        Log.i(TAG, "ScrollCenterItem: cant scroll");
                    }

                }
            });
            
        }


        void GetAppSize(View view){
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    view.removeOnLayoutChangeListener(this);
                    FixSizeView(btnLeft,view.getWidth()/4,view.getHeight()/4);
                    FixSizeView(btnRight,view.getWidth()/4,view.getHeight()/4);
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        FixSizeView(listPanel,view.getWidth(),view.getHeight()/17);
                        FixSizeView(recyclerView,view.getWidth(),view.getHeight()/19);
                    }
                    else{
                        FixSizeView(listPanel,view.getWidth(),view.getHeight()/7);
                        FixSizeView(recyclerView,view.getWidth(),view.getHeight()/9);
                    }

                }
            });
        }
        public void ViewModelObserve(){
            mainViewModel.getAdapter().observe(getViewLifecycleOwner(), adapter -> {
                recyclerView.setAdapter(adapter);
                if(adapter instanceof ResourceListAdapter){
                    btnSwitchView.setText(R.string.gallery);
                    Log.i(TAG, "ViewModelObserve: Adapter Resource");
                    ScrollCenterItem(mainViewModel.getCurrentResourcePosValue());
                    Log.i(TAG, "ViewModelObserve: Scroll to " + mainViewModel.currentResourcePos.getValue());
                }
                else {
                    btnSwitchView.setText(R.string.resource);
                    Log.i(TAG, "ViewModelObserve: Adapter Galley");
                    Log.i(TAG, "ViewModelObserve: Scroll to " + mainViewModel.currentGalleryPos.getValue());
                    ScrollCenterItem(mainViewModel.getCurrentGalleryPos().getValue());

                }
                Log.i(TAG, "ViewModelObserve: Adapter changed");
            });
            mainViewModel.getCurrentResourcePos().observe(getViewLifecycleOwner(),  val -> {
                if(recyclerView.getAdapter() instanceof ResourceListAdapter){
                    ScrollCenterItem(val);
                    Log.i(TAG, "ViewModelObserve: current Res change " + val);
                }
            });
            mainViewModel.getCurrentGalleryPos().observe(getViewLifecycleOwner(), val -> {
                if(recyclerView.getAdapter() instanceof GalleryListAdapter){
                    ScrollCenterItem(val);
                    Log.i(TAG, "ViewModelObserve: current Res change " + val);
                }
            });
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.next_prev_btn,container,false);
            MainActivity main = (MainActivity) container.getContext();
            View mainView = this.getActivity().getWindow().getDecorView();
            mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            btnLeft = v.findViewById(R.id.btn_left);
            btnRight = v.findViewById(R.id.btn_right);
            recyclerView = v.findViewById(R.id.list_item);
            btnSwitchView = v.findViewById(R.id.btn_view_gallery);
            listPanel = v.findViewById(R.id.list_panel);
            GetAppSize(mainView);
            linearLayoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            if(savedInstanceState == null || mainViewModel.getAdapter().getValue() instanceof ResourceListAdapter){
                mainViewModel.setAdapter(new ResourceListAdapter(requireActivity(),ResourceView.getInstance(this.requireActivity()).GetImages(),mainViewModel));
            }
            else mainViewModel.setAdapter(new GalleryListAdapter(this.requireActivity(),GalleryView.getInstance(this.requireActivity()).GetImages(),mainViewModel));
            ViewModelObserve();
            btnLeft.setOnClickListener(view -> {
                prev();
                ScrollCenterItem(main.viewPager.getCurrentItem());
            });
            btnRight.setOnClickListener(view -> {
                next();
                ScrollCenterItem(main.viewPager.getCurrentItem());
            });
            btnSwitchView.setOnClickListener(view -> {
                if(btnSwitchView.getText() == getResources().getString(R.string.gallery)){
                    if(!main.isHasPermission()){
                        main.checkPermissions();
                    }
                    else{
                        btnSwitchView.setText(R.string.resource);
                        mainViewModel.setAdapter(new GalleryListAdapter(main,GalleryView.getInstance(this.requireActivity()).GetImages(),mainViewModel));
                    }
                }
                else{
                    btnSwitchView.setText(R.string.gallery);
                    mainViewModel.setAdapter(new ResourceListAdapter(main,ResourceView.getInstance(this.requireActivity()).GetImages(),mainViewModel));
                }

            });
            return v;
        }



    }


}