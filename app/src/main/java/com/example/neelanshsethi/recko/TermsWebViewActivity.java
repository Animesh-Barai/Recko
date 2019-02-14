package com.example.neelanshsethi.recko;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;

import androidx.appcompat.app.AppCompatActivity;

public class TermsWebViewActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_webview_activity);
        WebView webView = (WebView) findViewById(R.id.webview);
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
        webView.loadUrl("https://www.paypal.com/in/webapps/mpp/ua/useragreement-full");
    }
}
