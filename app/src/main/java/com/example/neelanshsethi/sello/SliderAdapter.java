package com.example.neelanshsethi.sello;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SliderAdapter extends PagerAdapter
{

    int[] layouts;
    Context mctx;
    LayoutInflater layoutInflater;
    int [] images;
    public SliderAdapter(Context mctx, int[] images)
    {
        this.mctx=mctx;
//        this.layouts=layouts;
        this.images=images;

    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.carousel_panel, container, false);

        assert view!=null;
        final ImageView imageView=(ImageView) view.findViewById(R.id.carousel_panel);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
