package com.example.neelanshsethi.sello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_dashboard);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;

            switch (item.getItemId()){

                case R.id.action_for_you:
                    fragment=new FragmentForYou();
                    break;
                case R.id.action_explore:
                    fragment=new FragmentExplore();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
    };
}
