package com.recko.app.Misc;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCustom extends RecyclerView {
    private int maxHeight;

    public RecyclerViewCustom(Context ctx) {
        super(ctx);
    }

    public RecyclerViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int dpsToPixels(int dp) {
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight>0) heightSpec = MeasureSpec.makeMeasureSpec(dpsToPixels(maxHeight), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
