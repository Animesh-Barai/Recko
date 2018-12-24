package com.example.neelanshsethi.sello;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.neelanshsethi.sello.Model.Category_InCategoryAndCompanyModel;

import java.util.List;

public class CategoryAndCompanyAdapter extends RecyclerView.Adapter<CategoryAndCompanyAdapter.ResultViewHolder> {

    Context mctx;
    List categorylist;
    Activity mActivity;

    public static final int VIEW_TYPE_NORMAL = 1;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private List<String> sampleimgurl;
    private Category_InCategoryAndCompanyModel category_inCategoryAndCompanyModel;

    public CategoryAndCompanyAdapter(Context mctx,List categorylist, Activity mActivity) {
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

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //yet to be decided

//                Intent intent= new Intent(mctx,YoutubePlayerActivity.class);
//                intent.putExtra("kuch ayega yahan",category_inCategoryAndCompanyModel.getCategory_uuid());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mctx.startActivity(intent);
//                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

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

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgcategory);
            textView=itemView.findViewById(R.id.titlecategory);
        }
    }


}
