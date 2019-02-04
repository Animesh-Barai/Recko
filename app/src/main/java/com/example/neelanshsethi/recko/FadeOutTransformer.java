package com.example.neelanshsethi.recko;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class FadeOutTransformer implements ViewPager.PageTransformer{
    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-position*page.getWidth());

        page.setAlpha(1-Math.abs(position));


    }
}