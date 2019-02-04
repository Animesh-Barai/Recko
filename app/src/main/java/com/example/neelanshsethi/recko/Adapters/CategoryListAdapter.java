package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.neelanshsethi.recko.ExpandableHeightGridView;
import com.example.neelanshsethi.recko.Model.CategoryListModel;
import com.example.neelanshsethi.recko.Model.CategoryModel;
import com.example.neelanshsethi.recko.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ResultViewHolder> {

    private Context mctx;
    private Activity mActivity;
    private List categorylist;
    private CategoryListModel categoryListModel;

    public CategoryListAdapter(Context mctx, List categorylist,Activity mActivity) {
        this.mctx = mctx;
        this.categorylist=categorylist;
        this.mActivity=mActivity;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.item_categories_list, null);
        return new CategoryListAdapter.ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ResultViewHolder holder, int position) {

        CategoryModel tmp=null;
        CategoryAdapter temp = new CategoryAdapter(mctx,tmp,mActivity);
        holder.gridView.setAdapter(temp);


        if(!categorylist.isEmpty()) {
            categoryListModel= (CategoryListModel) categorylist.get(position);
            Log.d("zzz", categoryListModel.getHeading());
            holder.textView.setText(categoryListModel.getHeading());

                CategoryModel categoryModel= new CategoryModel(categoryListModel.getCategoryimageurl(),categoryListModel.getCategoryname(),categoryListModel.getCategoryindustry(),categoryListModel.getCategorymaxcommission(),categoryListModel.getCategoryuuid(),categoryListModel.getCategorynoofproduct());
                Log.d("zzzz "+position,categoryListModel.getCategoryimageurl().toString());
                CategoryAdapter categoryAdapter = new CategoryAdapter(mctx,categoryModel,mActivity);
                holder.gridView.setAdapter(categoryAdapter);

        }
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

        private GridView gridView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            gridView=(ExpandableHeightGridView)itemView.findViewById(R.id.categorygridview);
            ((ExpandableHeightGridView) gridView).setExpanded(true);
            textView=itemView.findViewById(R.id.heading);
        }
    }
}
