package com.recko.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TermsWebViewActivity extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static String TAG = TermsWebViewActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_webview_activity);
        final WebView webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //hide loading image
                progressBar.setVisibility(View.GONE);
                //show webview
                //findViewById(R.id.webView1).setVisibility(View.VISIBLE);
            }
        });

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        Intent intent = getIntent();
        boolean should_use_docs = intent.getBooleanExtra("should_use_docs", true);

        webView.getSettings().setBuiltInZoomControls(true);
        if (intent.hasExtra("url")) {
            String url = intent.getStringExtra("url");
            if (should_use_docs && url.endsWith(".pdf"))
                    url = "http://docs.google.com/gview?embedded=true&url=" + url;
            webView.getSettings().setJavaScriptEnabled(true);
            //webView.getSettings().setLoadWithOverviewMode(true);
            //webView.getSettings().setUseWideViewPort(true);
            webView.loadUrl(url);
        } else {
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                Log.d(TAG, "Config params updated: " + updated);

                            } else {
                                Log.d(TAG, "Config params updated failed");
                            }
                            Log.d(TAG, "http://docs.google.com/gview?embedded=true&url=" +
                                    mFirebaseRemoteConfig.getString("privacy_policy"));
                            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +
                                    mFirebaseRemoteConfig.getString("privacy_policy"));
                        }
                    });
        }
    }
}
