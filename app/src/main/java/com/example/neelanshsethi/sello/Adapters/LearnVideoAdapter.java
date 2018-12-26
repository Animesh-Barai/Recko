package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.sello.Model.Category_InCategoryAndCompanyModel;
import com.example.neelanshsethi.sello.Model.VideosModel;
import com.example.neelanshsethi.sello.R;
import com.example.neelanshsethi.sello.YoutubePlayerActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LearnVideoAdapter extends RecyclerView.Adapter<LearnVideoAdapter.ResultViewHolder> {

    Context mctx;
    private List videolist;
    Activity mActivity;

    public static final int VIEW_TYPE_NORMAL = 1;
    private VideosModel videosModel;

    public LearnVideoAdapter(Context mctx,List videolist, Activity mActivity) {
        this.mctx = mctx;
        this.videolist=videolist;
        this.mActivity=mActivity;

    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.learn_video_card, null);
        return new ResultViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, final int position) {

        if (!videolist.isEmpty()) {
            videosModel = (VideosModel) videolist.get(position);
            Glide.with(mctx)
                    .load(videosModel.getThumbnail_url())
                    .into(holder.imageView);
            holder.textView.setText(videosModel.getTitle());


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mctx, YoutubePlayerActivity.class);
                    intent.putExtra("video_id", videosModel.getVideo_url());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mctx.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (videolist != null && videolist.size() > 0) {
            return videolist.size();
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
