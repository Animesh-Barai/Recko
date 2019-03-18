package com.example.neelanshsethi.recko;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.atomic.AtomicBoolean;

public class LearnHowItWorks extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer player;
    Button skip;
    ImageView skip_illustration;
    ImageView play;
    String videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    AtomicBoolean skip_clicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_how_it_works);
        playerView=findViewById(R.id.exoplayerview);
        skip=findViewById(R.id.Skip);
        skip_illustration=findViewById(R.id.skip_illustration);
        play=findViewById(R.id.play);

        skip_clicked = new AtomicBoolean();
        skip_clicked.set(false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=view.getContext();
                skip_illustration.setVisibility(View.INVISIBLE);
                play.setVisibility(View.INVISIBLE);
                playerView.setVisibility(View.VISIBLE);
                play(context);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!skip_clicked.compareAndSet(false, true)) return;
                if (player!=null) player.stop();
                Intent intent=new Intent(getApplicationContext(),NavigationDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
        findViewById(R.id.skip_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("zzkkk", "here1");
                if (!skip_clicked.compareAndSet(false, true)) return;
                Log.d("zzkkk", "here1");
                if (player!=null) player.stop();
                Log.d("zzkkk", "here1");
                Intent intent=new Intent(getApplicationContext(),NavigationDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Log.d("zzkkk", "here1");
                finish();
                Log.d("zzkkk", "here1");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public  void play(Context ctx)
    {
        findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.appbar).setVisibility(View.VISIBLE);
        skip.setVisibility(View.GONE);
        player=ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        Uri uri=Uri.parse(videoURL);
        playerView.setPlayer(player);

        try {
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(ctx, "Sello"));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
            // Prepare the player with the source.

            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            playerView.setControllerHideOnTouch(true);

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);



            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Toast.makeText(getApplicationContext(),"state changes",Toast.LENGTH_LONG);
                }
            });
        } catch (Exception e) {
            Log.e("MainAcvtivity"," exoplayer error "+ e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        release();
    }

    public void release()
    {
        if (player != null) {
            player.seekTo(0);
            player.release();
        }
    }
}
