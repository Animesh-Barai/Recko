package com.example.neelanshsethi.sello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.view.FrameMetrics.ANIMATION_DURATION;

public class NavigationDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CustomViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dashboard);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        viewPager = (CustomViewPager) findViewById(R.id.fragment_container);
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager());
        adapter.addFragment(new FragmentForYou(), "title");
        adapter.addFragment(new FragmentExplore(), "title");
        adapter.addFragment(new FragmentForYou(), "title");
        adapter.addFragment(new FragmentForYou(), "title");
        adapter.addFragment(new FragmentForYou(), "title");

        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        bottomNavigationView.setSelectedItemId(R.id.action_for_you);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;

            switch (item.getItemId()){
                case R.id.action_learn:
//                    fragment=new FragmentForYou();
                    viewPager.setCurrentItem(0, false);
                    break;
                case R.id.action_explore:
//                    fragment=new FragmentExplore();
                            viewPager.setCurrentItem(1, false);
                    break;
                case R.id.action_for_you:
//                    fragment=new FragmentForYou();
                    viewPager.setCurrentItem(2, false);
                    break;
                case R.id.action_crm:
//                    fragment=new FragmentForYou();
                            viewPager.setCurrentItem(3, false);
                    break;
                case R.id.action_settings:
//                    fragment=new FragmentForYou();
                    viewPager.setCurrentItem(4, false);
                    break;

            }

//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
    };



}
