package com.example.neelanshsethi.recko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.neelanshsethi.recko.Adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CustomViewPager viewPager;
    ViewPagerAdapter viewpageradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dashboard);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        viewPager = (CustomViewPager) findViewById(R.id.fragment_container);
        viewpageradapter = new ViewPagerAdapter (getSupportFragmentManager());
        viewpageradapter.addFragment(new FragmentLearn(), "Learn");
        viewpageradapter.addFragment(new FragmentExplore(), "Explore");
        viewpageradapter.addFragment(new FragmentForYou(), "ForYou");
        viewpageradapter.addFragment(new FragmentCRM(), "CRM");
        viewpageradapter.addFragment(new FragmentSettings(), "Settings");

        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewpageradapter);
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

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("zzz paused","paused");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("zzz resume","resumed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("zzz restart","restart");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("zzz navigation activity", "got activity result");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
