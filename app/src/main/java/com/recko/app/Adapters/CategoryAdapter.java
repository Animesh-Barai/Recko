package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recko.app.Model.CategoryModel;
import com.recko.app.ProductsInCategory;
import com.recko.app.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import androidx.cardview.widget.CardView;

public class CategoryAdapter extends BaseAdapter {

    View v;
    LayoutInflater layoutInflater;

    private Context mctx;
    private Activity mActivity;
    private CategoryModel categoryModel;
    private FirebaseAnalytics mFirebaseAnalytics;

    public CategoryAdapter(Context mctx, CategoryModel categoryModel, Activity mActivity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
        this.mctx = mctx;
        this.categoryModel=categoryModel;
        this.mActivity=mActivity;
    }

    @Override
    public int getCount() {
        if(categoryModel!=null && categoryModel.getCategoryimageurl().size()>0)
            return categoryModel.getCategoryimageurl().size();
        else
            return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view==null)
        {
            v=new View(mctx);
            v=layoutInflater.inflate(R.layout.category_card,viewGroup,false);
        }
        else {
            v = (View) view;
        }

        TextView titlecategory= v.findViewById(R.id.titlecategory);
        ImageView imgcategory=v.findViewById(R.id.imgcategory);
        TextView amountcategory=v.findViewById(R.id.amountcategory);
        CardView cardView=v.findViewById(R.id.categorycard);

        if(categoryModel!=null) {
            if (!categoryModel.getCategoryimageurl().isEmpty()) {
                Glide.with(mctx)
                        .load(categoryModel.getCategoryimageurl().get(i))
                        .into(imgcategory);
                titlecategory.setText(categoryModel.getCategoryname().get(i));
                amountcategory.setText(categoryModel.getCategorymaxcommission().get(i));
            }
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("category_uuid",categoryModel.getCategoryuuid().get(i));
                bundle.putString("category_name",categoryModel.getCategoryname().get(i));
                mFirebaseAnalytics.logEvent("click_category_from_main_page", bundle);

                Intent intent= new Intent(mctx,ProductsInCategory.class);
                intent.putExtra("category_uuid",categoryModel.getCategoryuuid().get(i));
                intent.putExtra("category_name",categoryModel.getCategoryname().get(i));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        return v;
    }


}
