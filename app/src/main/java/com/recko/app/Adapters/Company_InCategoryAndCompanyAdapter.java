package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recko.app.Misc.Constants;
import com.recko.app.Model.Company_InCategoryAndCompanyModel;
import com.recko.app.ProductsInCompany;
import com.recko.app.R;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Company_InCategoryAndCompanyAdapter extends RecyclerView.Adapter<Company_InCategoryAndCompanyAdapter.ResultViewHolder> {

    Context mctx;
    List companylist;
    Activity mActivity;

    public static final int VIEW_TYPE_NORMAL = 1;
    private Company_InCategoryAndCompanyModel company_inCategoryAndCompanyModel;

    public Company_InCategoryAndCompanyAdapter(Context mctx, List companylist, Activity mActivity) {
        this.mctx = mctx;
        this.companylist=companylist;
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

        if (!companylist.isEmpty()) {
            company_inCategoryAndCompanyModel = (Company_InCategoryAndCompanyModel) companylist.get(position);
            if (Constants.isValidURL(company_inCategoryAndCompanyModel.getImage_url())) {
                Glide.with(mctx)
                        .load(company_inCategoryAndCompanyModel.getImage_url())
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.industry1);
            }

            holder.textView.setText(company_inCategoryAndCompanyModel.getCompany_name());
            holder.amountcategory.setText(Constants.fixDoubleString((
                    company_inCategoryAndCompanyModel.getMax_commisiion())));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Company_InCategoryAndCompanyModel temp = (Company_InCategoryAndCompanyModel) companylist.get(position);
                List tempList = temp.getList();
                Intent intent= new Intent(mctx,ProductsInCompany.class);
                intent.putExtra("product_list", (Serializable) tempList);
                intent.putExtra("company_name", temp.getCompany_name());
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
        if (companylist != null && companylist.size() > 0) {
            return companylist.size();
        } else {
            return 1;
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
            amountcategory = itemView.findViewById(R.id.amountcategory);
        }
    }


}
