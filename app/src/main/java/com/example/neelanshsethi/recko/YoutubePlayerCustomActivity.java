package com.example.neelanshsethi.recko;

import android.os.Bundle;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class YoutubePlayerCustomActivity extends AppCompatActivity {

    YouTubePlayerView youTubePlayerView;
    String videoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player_custom);
        videoId = getIntent().getStringExtra("video_id");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if (videoId!=null && !videoId.trim().equals("")){
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.play();
                }
            }
        });
        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {
                if (videoId!=null && !videoId.trim().equals("")){
                    youTubePlayer.loadVideo(videoId, 0);
                }
            }
        });
        //youTubePlayerView.inflateCustomPlayerUi(R.layout.youtube_custom_layout);
    }
}
