package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.recko.app.ExpandableHeightGridView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.recko.app.R;

public class CompanyListGridAdapter extends RecyclerView.Adapter<CompanyListGridAdapter.ResultViewHolder> {

    Context mctx;
    List companyList;
    Activity mActivity;

    public CompanyListGridAdapter(Context mctx, List companyList, Activity mActivity) {
        this.mctx = mctx;
        this.companyList = companyList;
        this.mActivity = mActivity;
    }


    @NonNull
    @Override
    public CompanyListGridAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.item_company_grid_list, null);
        return new CompanyListGridAdapter.ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyListGridAdapter.ResultViewHolder holder, int position) {
        CompanyInGridAdapter companyInGridAdapter = new CompanyInGridAdapter(mctx, companyList, mActivity);
        holder.gridView.setAdapter(companyInGridAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
        /*if (companyList != null && companyList.size() > 0) {
            return companyList.size();
        } else {
            return 1;
        }*/
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private GridView gridView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            gridView=(ExpandableHeightGridView)itemView.findViewById(R.id.companygridview);
            ((ExpandableHeightGridView) gridView).setExpanded(true);
            textView=itemView.findViewById(R.id.heading);
        }
    }
}
