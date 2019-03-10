package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.neelanshsethi.recko.ExpandableHeightGridView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.neelanshsethi.recko.R;

public class ProductListGridAdapter extends RecyclerView.Adapter<ProductListGridAdapter.ResultViewHolder> {

    Context mctx;
    List productList;
    Activity mActivity;

    public ProductListGridAdapter(Context mctx, List productList, Activity mActivity) {
        this.mctx = mctx;
        this.productList = productList;
        this.mActivity = mActivity;
    }


    @NonNull
    @Override
    public ProductListGridAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.item_product_grid_list, null);
        return new ProductListGridAdapter.ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListGridAdapter.ResultViewHolder holder, int position) {
        ProductInGridAdapter productInGridAdapter = new ProductInGridAdapter(mctx, productList, mActivity);
        holder.gridView.setAdapter(productInGridAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
        /*if (productList != null && productList.size() > 0) {
            return productList.size();
        } else {
            return 1;
        }*/
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private GridView gridView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            gridView=(ExpandableHeightGridView)itemView.findViewById(R.id.productgridview);
            ((ExpandableHeightGridView) gridView).setExpanded(true);
            textView=itemView.findViewById(R.id.heading);
        }
    }
}
