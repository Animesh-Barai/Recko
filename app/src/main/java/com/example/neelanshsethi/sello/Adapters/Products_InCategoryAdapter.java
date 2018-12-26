package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.sello.Model.Company_InCategoryAndCompanyModel;
import com.example.neelanshsethi.sello.Model.ProductModel;
import com.example.neelanshsethi.sello.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Products_InCategoryAdapter extends RecyclerView.Adapter<Products_InCategoryAdapter.ResultViewHolder> {

    Context mctx;
    List productlist;
    Activity mActivity;
    private ProductModel productModel;

    public static final int VIEW_TYPE_NORMAL = 1;

    public Products_InCategoryAdapter(Context mctx, List productlist, Activity mActivity) {
        this.mctx = mctx;
        this.productlist=productlist;
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

        if (!productlist.isEmpty()) {
            productModel = (ProductModel) productlist.get(position);
//            Glide.with(mctx)
//                    .load(productModel.getImage_url())
//                    .into(holder.imageView);
            holder.textView.setText(productModel.getTitle());
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
//                ProductModel productModel = company_inCategoryAndCompanyModel.getList().get(0);
//                Toast.makeText(mctx,productModel.getTitle()+productModel.getTotal_commission(),Toast.LENGTH_SHORT).show();

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

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgcategory);
            textView=itemView.findViewById(R.id.titlecategory);
        }
    }


}
