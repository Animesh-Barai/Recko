package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.Company_InCategoryAndCompanyModel;
import com.example.neelanshsethi.recko.Model.ProductModel;
import com.example.neelanshsethi.recko.PaymentActivity;
import com.example.neelanshsethi.recko.ProductDetails;
import com.example.neelanshsethi.recko.ProductsInCategory;
import com.example.neelanshsethi.recko.ProductsInCompany;
import com.google.firebase.auth.FirebaseAuth;
import com.example.neelanshsethi.recko.R;


import java.io.Serializable;
import java.util.List;

import androidx.cardview.widget.CardView;

public class CompanyInGridAdapter extends BaseAdapter {
    private static final String TAG = CompanyInGridAdapter.class.getSimpleName();
    View v;
    LayoutInflater layoutInflater;

    private Context mctx;
    private Activity mActivity;
    private List companyList;

    public CompanyInGridAdapter(Context mctx, List companyList, Activity mActivity) {
        this.mctx = mctx;
        this.companyList = companyList;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        if (companyList!=null) {
            return companyList.size();
        } else
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
        layoutInflater = (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view==null) {
            v = new View(mctx);
            v = layoutInflater.inflate(R.layout.company_grid_card, viewGroup, false);
        } else {
            v = (View) view;
        }

        if (i >= companyList.size()) return v;

        TextView titlecompany= v.findViewById(R.id.titlecompany);
        ImageView imgcompany=v.findViewById(R.id.imgcompany);
        TextView amountcompany=v.findViewById(R.id.amountcompany);
        CardView cardView=v.findViewById(R.id.companycard);

        final Company_InCategoryAndCompanyModel companyModel = (Company_InCategoryAndCompanyModel) companyList.get(i);
        ((TextView)v.findViewById(R.id.amountcompany)).setText(
                Constants.fixDoubleString(companyModel.getMax_commisiion()));
        if (Constants.isValidURL(companyModel.getImage_url())) {
            Log.d(TAG, companyModel.getImage_url());
            Glide.with(mctx)
                    .load(companyModel.getImage_url())
                    .into(imgcompany);
        }

        titlecompany.setText(companyModel.getCompany_name());
        //amountcompany.setText(companyModel.getTotal_commission());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mctx,ProductsInCompany.class);
                intent.putExtra("product_list",(Serializable)companyModel.getList());
                intent.putExtra("company_name", companyModel.getCompany_name());
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        return v;
    }
}
