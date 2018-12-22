package com.example.neelanshsethi.sello;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.ls.LSInput;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ResultViewHolder> {

    private Context mctx;
    private Activity mActivity;
    private List<String> heading;
    private List<List<String>> categorynoofproduct;
    private List<List<String>> categorymaxcommisssion;
    private List<List<String>> categoryname;
    private List<List<String>> categoryindustry;
    private List<List<String>> categoryuuid;
    private List<List<String>> categoryimageurl;

    public CategoryListAdapter(Context mctx, List<String> heading, List<List<String>> categoryimageurl, List<List<String>> categoryindustry, List<List<String>> categorymaxcommisssion, List<List<String>> categoryname, List<List<String>> categorynoofproduct, List<List<String>> categoryuuid,Activity mActivity) {
        this.mctx = mctx;
        this.heading = heading;
        this.categorynoofproduct = categorynoofproduct;
        this.categorymaxcommisssion = categorymaxcommisssion;
        this.categoryname = categoryname;
        this.categoryindustry = categoryindustry;
        this.categoryuuid = categoryuuid;
        this.categoryimageurl = categoryimageurl;
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

        if(!heading.isEmpty()) {
            Log.d("zzz", heading.get(position));
            holder.textView.setText(heading.get(position));
            if (!categoryimageurl.isEmpty()) {
                Log.d("zzzz "+position,categoryimageurl.toString());
                CategoryAdapter categoryAdapter = new CategoryAdapter(mctx, categoryimageurl.get(position), categoryname.get(position), categorymaxcommisssion.get(position), categoryindustry.get(position), categorynoofproduct.get(position), categoryuuid.get(position),mActivity);
                holder.gridView.setAdapter(categoryAdapter);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (heading != null && heading.size() > 0) {
            return heading.size();
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
