package com.example.neelanshsethi.sello;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.example.neelanshsethi.sello.Model.ProductModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class ProductDetails extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    Chip whatsapp,brochure;
    ProductModel productModel;

    ImageView video_thumbnail;
    TextView product_name;
    TextView brochure_subheading;

    TextView actual_price;
    TextView offer_price;
    TextView final_price;
    TextInputEditText offer_discount_price;
    Chip chip_discount1;
    Chip chip_discount2;
    Chip chip_discount3;
    Chip chip_discount4;
    Button earn_button;
    ImageView brochure_thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        toolbar=findViewById(R.id.toolbar);
        whatsapp = findViewById(R.id.chip_whatsapp);
        brochure = findViewById(R.id.chip_brochure);
        video_thumbnail = findViewById(R.id.video_thumbnail);
        product_name = findViewById(R.id.product_name);
        brochure_subheading = findViewById(R.id.brochure_subheading);

        actual_price = findViewById(R.id.actual_price);
        offer_price = findViewById(R.id.offer_price);
        final_price = findViewById(R.id.final_price);
        offer_discount_price = findViewById(R.id.offer_discount_price);
        chip_discount1 = findViewById(R.id.chip_discount1);
        chip_discount2 = findViewById(R.id.chip_discount2);
        chip_discount3 = findViewById(R.id.chip_discount3);
        chip_discount4 = findViewById(R.id.chip_discount4);
        earn_button = findViewById(R.id.earn_button);
        brochure_thumbnail= findViewById(R.id.brochure_thumbnail);

        productModel = (ProductModel) this.getIntent().getSerializableExtra("product_model");


//        Glide.with(this)
//                    .load(productModel.getImage_url())
//                    .into(video_thumbnail);
        product_name.setText(productModel.getTitle());
        brochure_subheading.setText("Get to know more\n" + "about all the products\n" + "from " + productModel.getTitle());
        Glide.with(this)
                .load("https://storage.googleapis.com/ehimages/2018/1/7/img_8a9f448b813cf35915bc0bfd1355d281_1515313874521_original.jpg")
                .into(brochure_thumbnail);
        brochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")));
            }
        });

//        download_link.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!InternetConnection.checkConnection(ProductDetails.this)) {
//                    Snackbar.make(findViewById(android.R.id.content), "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
//                } else
//                {
//                    Toast.makeText(getApplicationContext(), "Downloading the brochure", Toast.LENGTH_SHORT).show();
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(productModel.getBroucher()));
//                    request.allowScanningByMediaScanner();
//                    request.setDescription("Downloading...");
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                    downloadManager.enqueue(request);
//                }
//            }
//        });

        actual_price.setText(productModel.getMrp());
        offer_price.setText(productModel.getPrice_on_x());

        final_price.setText(productModel.getPrice_on_x());

        offer_discount_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (offer_discount_price.getText().toString().trim().equals(""))
                    final_price.setText(productModel.getPrice_on_x());
                else {
                    if (Integer.parseInt(productModel.getPrice_on_x()) - Integer.parseInt(charSequence.toString()) > Integer.parseInt(productModel.getPrice_on_x()))
                        final_price.setText(Integer.parseInt(productModel.getPrice_on_x()) - Integer.parseInt(charSequence.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        earn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Yo Man!",Toast.LENGTH_SHORT).show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
        }

        //for FileUriExposedException
        //can be looked into
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

    class LoadImage extends AsyncTask<Void, Void, Void>{

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
        new LoadImage(text,link).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
