package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recko.app.Misc.Constants;
import com.recko.app.Model.ProductModel;
import com.recko.app.ProductDetails;
import com.recko.app.R;


import java.util.List;

import androidx.cardview.widget.CardView;

public class ProductInGridAdapter extends BaseAdapter {

    View v;
    LayoutInflater layoutInflater;

    private Context mctx;
    private Activity mActivity;
    private List productList;

    public ProductInGridAdapter(Context mctx, List productList, Activity mActivity) {
        this.mctx = mctx;
        this.productList = productList;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        if (productList!=null) {
            return productList.size();
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
            v = layoutInflater.inflate(R.layout.product_grid_card, viewGroup, false);
        } else {
            v = (View) view;
        }

        if (i >= productList.size()) return v;

        TextView titleproduct= v.findViewById(R.id.titleproduct);
        ImageView imgproduct=v.findViewById(R.id.imgproduct);
        TextView amountproduct=v.findViewById(R.id.amountproduct);
        CardView cardView=v.findViewById(R.id.productcard);

        final ProductModel productModel = (ProductModel) productList.get(i);
        if (productModel.getImg_url() != null && !productModel.getImg_url().trim().equals("")) {
            Glide.with(mctx)
                    .load(productModel.getImg_url())
                    .into(imgproduct);
        }

        titleproduct.setText(productModel.getProductDisplayNmae());
        amountproduct.setText(Constants.fixDoubleString(productModel.getTotal_commission()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mctx,ProductDetails.class);
                intent.putExtra("product_model",productModel);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        return v;
    }
}
