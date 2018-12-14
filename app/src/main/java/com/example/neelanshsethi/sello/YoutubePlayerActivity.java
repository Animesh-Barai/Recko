package com.example.neelanshsethi.sello;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by sonu on 10/11/17.
 * <p>
 * ### Here you need to extend the activity with YouTubeBaseActivity otherwise it will crash the app  ###
 */

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnFullscreenListener {
    private static final String TAG = YoutubePlayerActivity.class.getSimpleName();
    private String videoID;
    private YouTubePlayerView youTubePlayerView;
    private boolean b;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private static final int ANIMATION_DURATION_MILLIS = 300;
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;
    LinearLayout videoBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        //get the video id
        videoID = getIntent().getStringExtra("video_id");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        videoBox=findViewById(R.id.video_box);
        initializeYoutubePlayer();
    }

    /**
     * initialize the youtube player
     */
    private void initializeYoutubePlayer() {
        youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                                boolean wasRestored) {

                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //load the video
                    //youTubePlayer.loadVideo(videoID);

                    //OR

                    //cue the video
                    youTubePlayer.cueVideo(videoID);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //  youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    youTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                 /*  youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                //print or show error if initialization failed
                youTubePlayerView=null;
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Recreate the activity if user performed a recovery action
            recreate();
        }
    }

    @Override
    public void onFullscreen(boolean b) {
        this.b=b;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layout();
    }

    private void layout() {
        boolean isPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        if (b) {
//            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            setLayoutSize(youTubePlayerView, MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        } else if (isPortrait) {

            setLayoutSize(youTubePlayerView, MATCH_PARENT, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER_VERTICAL);
        } else {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
            setLayoutSize(youTubePlayerView, screenWidth / 4, MATCH_PARENT);
            int videoWidth = screenWidth - screenWidth / 3 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
            setLayoutSize(youTubePlayerView, videoWidth, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
    }


//    @TargetApi(16)
//    private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
//        if (Build.VERSION.SDK_INT >= 16) {
//            animator.withEndAction(runnable);
//        } else {
//            animator.setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    runnable.run();
//                }
//            });
//        }
//    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void setLayoutSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}

