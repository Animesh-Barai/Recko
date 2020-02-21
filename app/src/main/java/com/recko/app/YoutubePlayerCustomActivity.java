package com.recko.app;

import android.os.Bundle;
import android.util.Log;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.recko.app.Misc.Constants;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class YoutubePlayerCustomActivity extends AppCompatActivity {
    private static String TAG = YoutubePlayerCustomActivity.class.getSimpleName();
    YouTubePlayerView youTubePlayerView;
    String videoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player_custom);
        videoId = getIntent().getStringExtra("video_id");
        if (videoId == null || videoId.trim().equals("")) videoId = Constants.default_video;
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        Log.d(TAG, "playing: "  + videoId);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                Log.d(TAG, "playing when rdy: "  + videoId);
                if (videoId!=null && !videoId.trim().equals("")){
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.play();
                }
            }
        });
        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {
                Log.d(TAG, "playing when ready1: "  + videoId);
                if (videoId!=null && !videoId.trim().equals("")){
                    youTubePlayer.loadVideo(videoId, 0);
                }
            }
        });
        //youTubePlayerView.inflateCustomPlayerUi(R.layout.youtube_custom_layout);
    }
}
