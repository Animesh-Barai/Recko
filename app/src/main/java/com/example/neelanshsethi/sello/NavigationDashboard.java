package com.example.neelanshsethi.sello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;

import com.example.neelanshsethi.sello.Adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        adapter.addFragment(new FragmentLearn(), "Learn");
        adapter.addFragment(new FragmentForYou(), "Explore");
        adapter.addFragment(new FragmentForYou(), "ForYou");
        adapter.addFragment(new FragmentCRM(), "CRM");
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
