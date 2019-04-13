package com.recko.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.recko.app.LearnHowItWorks;
import com.recko.app.Model.VideosModel;
import com.recko.app.R;
import com.recko.app.YoutubePlayerActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class LearnVideoAdapter extends RecyclerView.Adapter<LearnVideoAdapter.ResultViewHolder> {

    Context mctx;
    private List videolist;
    Activity mActivity;

    private static String TAG = LearnVideoAdapter.class.getSimpleName();

    public static final int VIEW_TYPE_NORMAL = 1;
    private FirebaseAnalytics mFirebaseAnalytics;

    public LearnVideoAdapter(Context mctx,List videolist, Activity mActivity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
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
            final VideosModel videosModel = (VideosModel) videolist.get(position);
            Log.d("zzzz: ", "setting video in learn");
            if (!StringUtils.isEmpty(videosModel.getThumbnail_url())) {
                Glide.with(mctx)
                        .load(videosModel.getThumbnail_url())
                        .into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.video_icon_work);
            }
            holder.textView.setText(videosModel.getTitle());
            holder.thumbnailText.setText(videosModel.getTitle());
            holder.flavour_text.setText(videosModel.getFlavour_text());

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "aa");
                    Log.d(TAG, videosModel.getVideo_url());
                    Intent intent = new Intent(mctx, YoutubePlayerActivity.class);
                    intent.putExtra("video_id", videosModel.getVideo_url());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mctx.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                }
            });*/

            if (holder.mainViewHolder != null) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                bundle.putString("video_id",videosModel.getVideo_url());
                mFirebaseAnalytics.logEvent("click_video_from_learn", bundle);
                Log.d(TAG, "Callback set");
                holder.mainViewHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "aa");
                        Log.d(TAG, videosModel.getVideo_url());
                        Intent intent = new Intent(mctx, YoutubePlayerActivity.class);
                        intent.putExtra("video_id", videosModel.getVideo_url());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mctx.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                });
            }

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
        private TextView textView, thumbnailText, flavour_text;
        private ConstraintLayout mainViewHolder;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgcategory);
            textView=itemView.findViewById(R.id.titlecategory);
            mainViewHolder = itemView.findViewById(R.id.learnVideoCardHolder);
            thumbnailText = itemView.findViewById(R.id.video_thumbnail_text_top);
            flavour_text = itemView.findViewById(R.id.flavour_text);
        }
    }


}
