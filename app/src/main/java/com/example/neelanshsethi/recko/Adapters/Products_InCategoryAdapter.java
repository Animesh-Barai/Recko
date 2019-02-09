package com.example.neelanshsethi.recko.Adapters;

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
import com.example.neelanshsethi.recko.Model.ProductModel;
import com.example.neelanshsethi.recko.ProductDetails;
import com.example.neelanshsethi.recko.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Products_InCategoryAdapter extends RecyclerView.Adapter<Products_InCategoryAdapter.ResultViewHolder> {

    Context mctx;
    List productlist;
    Activity mActivity;
    private ProductModel productModel;

    public static final int VIEW_TYPE_NORMAL = 1;
    private FirebaseAnalytics mFirebaseAnalytics;

    public Products_InCategoryAdapter(Context mctx, List productlist, Activity mActivity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
        this.mctx = mctx;
        this.productlist=productlist;
        this.mActivity=mActivity;
        Log.d("zzzkkk ", productlist.size()>0?"non-empty":"empty");
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.category_and_company_card, null);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {

        Log.d("zzzk ", productlist.size()>0?"non-empty":"empty");

        if (!productlist.isEmpty()) {
            productModel = (ProductModel) productlist.get(position);
            holder.imageView.setImageResource(R.drawable.sample2);
            /*if (!StringUtils.isEmpty(productModel.getImage_url())) {
                Glide.with(mctx)
                        .load(productModel.getImage_url())
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.sample2);
            }*/
//            Glide.with(mctx)
//                    .load(productModel.getImage_url())
//                    .into(holder.imageView);
            holder.textView.setText(productModel.getTitle());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("zzzkk ", productlist.size()>0?"non-empty":"empty");
                ProductModel temp = (ProductModel) productlist.get(position);
                Intent intent= new Intent(mctx,ProductDetails.class);
                intent.putExtra("product_model",temp);

                Bundle bundle = new Bundle();
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("product_uuid",temp.getProduct_uuid());
                bundle.putString("product_title",temp.getTitle());
                mFirebaseAnalytics.logEvent("product_clicked_in_category", bundle);

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
            return 1;
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private CardView cardView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgcategory);
            textView=itemView.findViewById(R.id.titlecategory);
            cardView=itemView.findViewById(R.id.categorycard);
        }
    }


}
