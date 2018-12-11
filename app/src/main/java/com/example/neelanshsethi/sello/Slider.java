package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Slider extends AppCompatActivity {

    ViewPager slides;
    SliderAdapter adapter;
    Button get_started;
    LinearLayout Dots;
    TextView[] dots;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        slides=findViewById(R.id.slides);
        get_started=findViewById(R.id.get_started);
        Dots=findViewById(R.id.Dots);

        layouts=new int[]{R.layout.welcome1,R.layout.welcome2,R.layout.welcome3};
        adapter=new SliderAdapter(this,layouts);

        addBottomDots(0);
        slides.setAdapter(adapter);
        slides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("status",MODE_PRIVATE);
                pref.edit().putBoolean("first",false).apply();
                Intent i=new Intent(Slider.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void addBottomDots(int position)
    {
        dots = new TextView[layouts.length];
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,0,15,0);

        Dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setTextSize(35);
            dots[i].setLayoutParams(params);
            if(i==position) {
                dots[i].setText(Html.fromHtml("&#9679;"));
                dots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            else
                dots[i].setText(Html.fromHtml("&#9675;"));
            Dots.addView(dots[i]);
        }
    }
}
