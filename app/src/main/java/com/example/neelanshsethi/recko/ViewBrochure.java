package com.example.neelanshsethi.recko;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ViewBrochure extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    Chip whatsapp;
    ImageView brochure_thumbnail;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.parseColor("#000000"));
        }
        setContentView(R.layout.activity_view_brochure);
        toolbar=findViewById(R.id.toolbar);
        whatsapp = findViewById(R.id.chip_whatsapp);
        brochure_thumbnail = findViewById(R.id.brochure_thumbnail);
        toolbar.setTitle(getIntent().getStringExtra("product_name") + " Offer Pamphlet");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ADD FROM INTENT
                share_whatsapp();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                share_whatsapp();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void share_whatsapp(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }
        }

        String text = "Wowww";
        String link = "https://www.google.es/images/srpr/logo11w.png";

        Bundle bundle = new Bundle();
        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        bundle.putString("text", text);
        bundle.putString("link", link);
        mFirebaseAnalytics.logEvent("share_brochure_whatsapp", bundle);

        new LoadImage(text,link).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


    class LoadImage extends AsyncTask<Void, Void, Void> {

        private Bitmap bm;
        private String text;
        private String link;

        LoadImage(String text, String link){
            this.text = text;
            this.link = link;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Looper.prepare();
            try {
                bm = Glide.with(getApplicationContext()).
                        asBitmap().
                        load(link).
                        submit().
                        get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(bm!=null)
            {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bm, "Title", null);
                File cache = getApplicationContext().getExternalCacheDir();
                File sharefile = new File(cache, "sample.png");
                Log.d("share file type is", sharefile.getAbsolutePath());
                try {
                    FileOutputStream out = new FileOutputStream(sharefile);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    Log.e("ERROR", String.valueOf(e.getMessage()));
                }
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                whatsappIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, text);
                whatsappIntent.setType("text/plain");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage("com.whatsapp");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp has not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
