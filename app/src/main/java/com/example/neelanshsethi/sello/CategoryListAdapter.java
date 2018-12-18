package com.example.neelanshsethi.sello;

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
    private List<String> heading;
    private List<List<String>> imagesurl;
    private List<List<String>> categorytitle;
    private List<List<String>> categoryamount;

    public CategoryListAdapter(Context mctx, List<String> heading, List<List<String>> imagesurl, List<List<String>> categorytitle, List<List<String>> categoryamount) {
        this.mctx = mctx;
        this.heading = heading;
        this.imagesurl = imagesurl;
        this.categorytitle = categorytitle;
        this.categoryamount = categoryamount;
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
        Log.d("zzz",heading.get(position));
        holder.textView.setText(heading.get(position));
        CategoryAdapter categoryAdapter = new CategoryAdapter(mctx,imagesurl.get(position),categorytitle.get(position),categoryamount.get(position));
        holder.gridView.setAdapter(categoryAdapter);

    }

    @Override
    public int getItemCount() {
        return 2;
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
