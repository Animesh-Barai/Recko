package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recko.app.Misc.Constants;
import com.recko.app.Model.ProductModel;
import com.recko.app.ProductDetails;
import com.recko.app.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Products_InCompanyAdapter extends RecyclerView.Adapter<Products_InCompanyAdapter.ResultViewHolder> {

    Context mctx;
    List productlist;
    Activity mActivity;
    private ProductModel productModel;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static final int VIEW_TYPE_NORMAL = 1;

    public Products_InCompanyAdapter(Context mctx, List productlist, Activity mActivity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
        this.mctx = mctx;
        this.productlist=productlist;
        this.mActivity=mActivity;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.product_large_card, null);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {

        if (!productlist.isEmpty()) {
            productModel = (ProductModel) productlist.get(position);
            if (Constants.isValidString(productModel.getImg_url()))
                Glide.with(mctx)
                        .load(productModel.getImg_url())
                        .into(holder.imageView);
            holder.textView.setText(productModel.getProductDisplayNmae());
            holder.amountcategory.setText(
                    Constants.fixDoubleString(productModel.getTotal_commission()));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //yet to be decided
                ProductModel temp = (ProductModel) productlist.get(position);
                Intent intent= new Intent(mctx,ProductDetails.class);
                intent.putExtra("product_model",temp);

                Bundle bundle = new Bundle();
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("product_uuid",temp.getProduct_uuid());
                bundle.putString("product_title",temp.getTitle());
                mFirebaseAnalytics.logEvent("product_clicked_in_company", bundle);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (productlist != null && productlist.size() > 0) {
            return productlist.size();
        } else {
            return 0;
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private CardView cardView;
        private TextView amountcategory;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgcategory);
            textView=itemView.findViewById(R.id.titlecategory);
            cardView=itemView.findViewById(R.id.categorycard);
            amountcategory=itemView.findViewById(R.id.amountcategory);
        }
    }


}
