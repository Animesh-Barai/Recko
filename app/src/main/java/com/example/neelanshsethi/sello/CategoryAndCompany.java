package com.example.neelanshsethi.sello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.neelanshsethi.sello.Adapters.ViewPagerAdapter;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

public class CategoryAndCompany extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    CustomViewPager viewPager;
    DachshundTabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_company);

        toolbar=findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        viewPager = (CustomViewPager) findViewById(R.id.fragment_container);
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager());
        adapter.addFragment(new FragmentCategory(), "Categories");
        adapter.addFragment(new FragmentCompany(), "Companies");


        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.category_and_company_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
