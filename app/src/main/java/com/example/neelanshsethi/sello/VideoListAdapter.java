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

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ResultViewHolder> {

    Context mctx;
    List<String> rv_videos;
    Activity mActivity;
    int[] rv_thumbnails;
    List<String> rv_videos_title;
    public static final int VIEW_TYPE_NORMAL = 1;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private List<String> sampleimgurl;

    public VideoListAdapter(Context mctx, List<String> rv_videos, List<String> sampleimgurl,List<String> rv_videos_title, Activity mActivity) {
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
        View v = inflater.inflate(R.layout.item_video_list, null);
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
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

            }
        });

//        holder.youTubeThumbnailView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                youTubeThumbnailLoader.setVideo(string);
//
//                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                    @Override
//                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                        youTubeThumbnailLoader.release();
//                    }
//
//                    @Override
//                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                        Log.d("zzz", "Youtube Thumbnail Error");
//                    }
//                });
//            }



//            @Override
//            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                if (youTubeInitializationResult.isUserRecoverableError()) {
//                    youTubeInitializationResult.getErrorDialog((Activity)mctx , RECOVERY_DIALOG_REQUEST).show();
//                } else {
//                    String errorMessage = String.format(mctx.getString(R.string.error_player), youTubeInitializationResult.toString());
//                    Toast.makeText(mctx, errorMessage, Toast.LENGTH_LONG).show();
//                }
//            }
//        });


//        holder.youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if (!b) {
//                    youTubePlayer.cueVideo("nCgQDjiotG0");
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                if (youTubeInitializationResult.isUserRecoverableError()) {
//                    youTubeInitializationResult.getErrorDialog((Activity)mctx , RECOVERY_DIALOG_REQUEST).show();
//                } else {
//                    String errorMessage = String.format(mctx.getString(R.string.error_player), youTubeInitializationResult.toString());
//                    Toast.makeText(mctx, errorMessage, Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });

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
            imageView=itemView.findViewById(R.id.video_thumbnail);
            textView=itemView.findViewById(R.id.video_thumbnail_text);
        }
    }


    }
