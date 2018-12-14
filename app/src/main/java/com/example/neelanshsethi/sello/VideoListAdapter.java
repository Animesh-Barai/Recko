package com.example.neelanshsethi.sello;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ResultViewHolder> {

    Context mctx;
    int[] list;
    Activity mActivity;
    public static final int VIEW_TYPE_NORMAL = 1;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    public VideoListAdapter(Context mctx, int[] list, Activity mActivity) {
        this.mctx = mctx;
        this.list = list;
        this.mActivity=mActivity;
    }


    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.item_video_list, null);
        return new ResultViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {

        final int id= list[position];

        holder.imageView.setBackgroundResource(id);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(mctx,YoutubePlayerActivity.class);
                intent.putExtra("video_id","nCgQDjiotG0");
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
        if (list != null && list.length > 0) {
            return list.length;
        } else {
            return 1;
        }
    }

    public void setItems(int[] list) {
        this.list = list;
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
