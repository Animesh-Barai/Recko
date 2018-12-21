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

import java.util.List;

public class CategoryAndCompanyAdapter extends RecyclerView.Adapter<CategoryAndCompanyAdapter.ResultViewHolder> {

    Context mctx;
    List<String> rv_videos;
    Activity mActivity;
    int[] rv_thumbnails;
    List<String> rv_videos_title;
    public static final int VIEW_TYPE_NORMAL = 1;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private List<String> sampleimgurl;

    public CategoryAndCompanyAdapter(Context mctx, List<String> rv_videos, List<String> sampleimgurl,List<String> rv_videos_title, Activity mActivity) {
        this.mctx = mctx;
        this.rv_videos = rv_videos;
        this.mActivity=mActivity;
//        this.rv_thumbnails=rv_thumbnails;
        this.rv_videos_title=rv_videos_title;
        this.sampleimgurl=sampleimgurl;
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

//        final int id= rv_thumbnails[position];
//        xholder.imageView.setBackgroundResource(id);
        Glide.with(mctx)
                .load(sampleimgurl.get(position))
                .into(holder.imageView);
        holder.textView.setText(rv_videos_title.get(position));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mctx,YoutubePlayerActivity.class);
                intent.putExtra("video_id",rv_videos.get(position));
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
        if (rv_videos != null && rv_videos.size() > 0) {
            return rv_videos.size();
        } else {
            return 1;
        }
    }

    public void setItems(int[] list) {
        this.rv_videos = rv_videos;
        this.rv_thumbnails=rv_thumbnails;
        this.rv_videos_title=rv_videos_title;
        notifyDataSetChanged();
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
