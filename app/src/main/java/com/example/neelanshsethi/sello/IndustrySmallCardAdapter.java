package com.example.neelanshsethi.sello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IndustrySmallCardAdapter extends RecyclerView.Adapter<IndustrySmallCardAdapter.ResultViewHolder> {

    Context mctx;
    private List<String> industries_small_title;
    private List<String> img_url;
    public static final int VIEW_TYPE_NORMAL = 1;


    public IndustrySmallCardAdapter( Context mctx, List<String> industries_small,List<String> img_url) {
        this.mctx = mctx;
        this.industries_small_title = industries_small;
        this.img_url=img_url;
    }


    @NonNull
    @Override
    public IndustrySmallCardAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.industry_small_card, null);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IndustrySmallCardAdapter.ResultViewHolder holder, int position) {

        Glide.with(mctx)
                .load(img_url.get(position))
                .into(holder.imageView);
        holder.textView.setText(industries_small_title.get(position));
    }

    @Override
    public int getItemCount() {
        if (img_url != null && img_url.size() > 0) {
            return img_url.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_small_industry);
            textView=itemView.findViewById(R.id.title_small_industry);
        }
    }
}
