package com.example.image_view;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity{



    public static class NextPevFragment extends DialogFragment {
    Button btnLeft, btnRight;
        public void prev(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        }

        public void next(ViewPager viewPager) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.next_prev_btn,container,false);
            btnLeft = v.findViewById(R.id.btn_left);
            btnRight = v.findViewById(R.id.btn_right);
            MainActivity main = (MainActivity) this.getContext();

            
            btnLeft.setOnClickListener(view -> {
                assert main != null;
                prev(main.viewPager);
            });
            btnRight.setOnClickListener(view -> {
                assert main != null;
                next(main.viewPager);
            });
            return v;
        }

    }
    ViewPager viewPager;
    Fragment fragmentScrollBtn;
    FragmentManager fm;

    //images array
    int[] images = {R.drawable.sc2, R.drawable.sc3, R.drawable.sc4, R.drawable.sc5,
            R.drawable.sc6, R.drawable.clone, R.drawable.diagram, R.drawable.preset};
    int[] allImage = GetImageArray();
    int[] GetImageArray(){
        Field[] ID_Fields = R.drawable.class.getDeclaredFields();
        int[] resArray = new int[ID_Fields.length];
        for(int i = 0; i < ID_Fields.length; i++) {
            try {
                resArray[i] = ID_Fields[i].getInt(null);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resArray;
    }


    //Creating Object of ViewPagerAdapter
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing the ViewPager Object
        viewPager = (ViewPager)findViewById(R.id.viewPagerMain);

        //Initializing the ViewPagerAdapter
        imageAdapter = new ImageAdapter(MainActivity.this, allImage);

        //Adding the Adapter to the ViewPager
        viewPager.setAdapter(imageAdapter);
        fragmentScrollBtn = new NextPevFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContainerView2, fragmentScrollBtn).detach(fragmentScrollBtn).commit();

    }
}