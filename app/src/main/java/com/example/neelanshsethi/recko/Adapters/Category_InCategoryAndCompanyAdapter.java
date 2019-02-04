package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.recko.Model.Category_InCategoryAndCompanyModel;
import com.example.neelanshsethi.recko.ProductsInCategory;
import com.example.neelanshsethi.recko.R;

import java.util.List;

public class Category_InCategoryAndCompanyAdapter extends RecyclerView.Adapter<Category_InCategoryAndCompanyAdapter.ResultViewHolder> {

    Context mctx;
    List categorylist;
    Activity mActivity;

    public static final int VIEW_TYPE_NORMAL = 1;
    private Category_InCategoryAndCompanyModel category_inCategoryAndCompanyModel;

    public Category_InCategoryAndCompanyAdapter(Context mctx, List categorylist, Activity mActivity) {
        this.mctx = mctx;
        this.categorylist=categorylist;
        this.mActivity=mActivity;

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

        if (!categorylist.isEmpty()) {
            category_inCategoryAndCompanyModel = (Category_InCategoryAndCompanyModel) categorylist.get(position);
            Glide.with(mctx)
                    .load(category_inCategoryAndCompanyModel.getImage_url())
                    .into(holder.imageView);
            holder.textView.setText(category_inCategoryAndCompanyModel.getName());
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //yet to be decided

                Log.d("zzz position",position+"");

                Category_InCategoryAndCompanyModel temp = (Category_InCategoryAndCompanyModel) categorylist.get(position);
                Intent intent= new Intent(mctx,ProductsInCategory.class);
                intent.putExtra("category_uuid", temp.getCategory_uuid());
                intent.putExtra("category_name",temp.getName());
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
        if (categorylist != null && categorylist.size() > 0) {
            return categorylist.size();
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
