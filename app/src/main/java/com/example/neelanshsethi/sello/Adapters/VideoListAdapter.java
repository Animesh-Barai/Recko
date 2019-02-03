package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.sello.Model.VideosModel;
import com.example.neelanshsethi.sello.R;
import com.example.neelanshsethi.sello.YoutubePlayerActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ResultViewHolder> {

    Context mctx;
    Activity mActivity;
    List videoslist;
    VideosModel videosModel;
    public static final int VIEW_TYPE_NORMAL = 1;


    public VideoListAdapter(Context mctx, List videoslist, Activity mActivity) {
        this.mctx = mctx;
        this.mActivity=mActivity;
        this.videoslist=videoslist;
    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.item_video_list, null);
        return new ResultViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {

        if(!videoslist.isEmpty()) {
            videosModel= (VideosModel) videoslist.get(position);
            if (!StringUtils.isEmpty(videosModel.getThumbnail_url())) {
                Glide.with(mctx)
                        .load(videosModel.getThumbnail_url())
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.sample2);
            }

            holder.textView.setText(videosModel.getTitle());
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mctx,YoutubePlayerActivity.class);
                intent.putExtra("video_id",videosModel.getVideo_url());
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
        if (videoslist != null && videoslist.size() > 0) {
            return videoslist.size();
        } else {
            return 1;
        }
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.video_thumbnail);
            textView=itemView.findViewById(R.id.video_thumbnail_text);
        }
    }


}
