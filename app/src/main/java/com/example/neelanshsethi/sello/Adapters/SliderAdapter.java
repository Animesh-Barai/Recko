package com.example.neelanshsethi.sello.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.sello.Model.CarouselModel;
import com.example.neelanshsethi.sello.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter
{

    Context mctx;
    LayoutInflater layoutInflater;
    List carousel_images;
    public SliderAdapter(Context mctx, List carousel_images)
    {
        this.mctx=mctx;
        this.carousel_images=carousel_images;

    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.carousel_panel, container, false);

        assert view!=null;
        CarouselModel carouselModel = (CarouselModel) carousel_images.get(position);
        final ImageView imageView=(ImageView) view.findViewById(R.id.carousel_panel);
        Glide.with(mctx)
                .load(carouselModel.getImage_url())
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return carousel_images.size();
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
