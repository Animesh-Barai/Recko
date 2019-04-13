package com.recko.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;

import android.graphics.Canvas;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.recko.app.Misc.Constants;
import com.recko.app.Model.ProductModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProductDetails extends AppCompatActivity {

    private static String TAG = ProductDetails.class.getSimpleName();
    androidx.appcompat.widget.Toolbar toolbar;
    Chip whatsapp,brochure;
    ProductModel productModel;

    ImageView video_thumbnail;
    TextView product_name;
    TextView brochure_subheading;

    TextView actual_price, video_thumbnail_text;
    TextView offer_price;
    TextView final_price;
    //TextInputEditText offer_discount_price;
    EditText offer_discount_price;
//    Chip chip_discount1;
//    Chip chip_discount2;
//    Chip chip_discount3;
//    Chip chip_discount4;
    Button earn_button;
    ImageView brochure_thumbnail;
    View translucent, video_thumbnail_container;

    TextView seller_commission_footer;

    int actual_price_val;
    View flyerView;

    View spinner_flyer;

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

        spinner_flyer = findViewById(R.id.spin_kit);
        spinner_flyer.setVisibility(View.GONE);

        actual_price = findViewById(R.id.actual_price);
        offer_price = findViewById(R.id.offer_price);
        final_price = findViewById(R.id.final_price);
        offer_discount_price = findViewById(R.id.offer_discount_price);
//        chip_discount1 = findViewById(R.id.chip_discount1);
//        chip_discount2 = findViewById(R.id.chip_discount2);
//        chip_discount3 = findViewById(R.id.chip_discount3);
//        chip_discount4 = findViewById(R.id.chip_discount4);
        earn_button = findViewById(R.id.earn_button);
        brochure_thumbnail= findViewById(R.id.brochure_thumbnail);
        flyerView = findViewById(R.id.custom_image_root);
        // This is used to make foreground slightly translucent.
        translucent = findViewById(R.id.translucent);
        translucent.getBackground().setAlpha(128);

        TextView name_pamplet = findViewById(R.id.name_pamplet);
        name_pamplet.setText(Constants.seller_name);
        TextView number_pamplet = findViewById(R.id.number_pamplet);
        number_pamplet.setText(Constants.seller_mobile_no);

        productModel = (ProductModel) this.getIntent().getSerializableExtra("product_model");

        // Set text over thumbnail
        video_thumbnail_text = findViewById(R.id.video_thumbnail_text);
        video_thumbnail_text.setText(productModel.getTitle() + " " + "Product Video");

        seller_commission_footer = findViewById(R.id.seller_commission_footer);
        seller_commission_footer.setText(getString(R.string.your_commission_is, productModel.commissionAfterDiscount()));

        video_thumbnail_container = findViewById(R.id.cardView);
        video_thumbnail_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoId = "";
                if (productModel.getYoutube_video_id()!=null && !productModel.getYoutube_video_id().trim().equals("")){
                    videoId = productModel.getYoutube_video_id();
                }

                Intent i = new Intent(ProductDetails.this, YoutubePlayerCustomActivity.class);
                Log.d("zzzkkk", videoId);
                i.putExtra("video_id", videoId);
                startActivity(i);
            }
        });


        toolbar.setTitle(productModel.getTitle());
//        Glide.with(this)
//                    .load(productModel.getImage_url())
//                    .into(video_thumbnail);
        product_name.setText(productModel.getTitle());
        brochure_subheading.setText(String.format("Get to know more\nabout all the products\nfrom %s", productModel.getTitle()));
        Glide.with(this)
                .load("https://storage.googleapis.com/ehimages/2018/1/7/img_8a9f448b813cf35915bc0bfd1355d281_1515313874521_original.jpg")
                .into(brochure_thumbnail);
        brochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, TermsWebViewActivity.class);
                Log.d(TAG, productModel.getBroucher());
                //intent.putExtra("url", "http://soluciones.toshiba.com/media/downloads/products/4555c-5055cBrochure.pdf");
                intent.putExtra("url", productModel.getBroucher());
                intent.putExtra("should_use_docs", true);
                startActivity(intent);
            }
        });

        brochure_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, ViewBrochure.class);
                intent.putExtra("product_name",productModel.getTitle());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ProductDetails.this,brochure_thumbnail,"brochure_thumbnail");
                startActivity(intent, options.toBundle());
            }
        });


///////        to download the pdf to system////////

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

        actual_price.setText(getString(R.string.rstart_template, Constants.fixDoubleString(productModel.getMrp())));
        String discountOnRecko = Constants.fixDoubleString(productModel.getDiscount_byRecko());
        if (discountOnRecko.equals("0"))
            offer_price.setText(getString(R.string.rstart_template,discountOnRecko));
        else
            offer_price.setText("-" + getString(R.string.rstart_template,discountOnRecko));
        final_price.setText(getString(R.string.rstart_template, Constants.fixDoubleString(productModel.getPrice_on_x())) );

//        chip_discount1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                offer_discount_price.setText(
//                        Constants.percentageValueDiscount(productModel.getTotal_commission(),
//                                getString(R.string.dis100_val)));
//                //offer_discount_price.setText(getString(R.string.dis100_val));
//            }
//        });
//
//        chip_discount2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                offer_discount_price.setText(
//                        Constants.percentageValueDiscount(productModel.getTotal_commission(),
//                                getString(R.string.dis200_val)));
//                //offer_discount_price.setText(getString(R.string.dis200_val));
//            }
//        });
//
//        chip_discount3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                offer_discount_price.setText(
//                        Constants.percentageValueDiscount(productModel.getTotal_commission(),
//                                getString(R.string.dis500_val)));
//                //offer_discount_price.setText(getString(R.string.dis500_val));
//            }
//        });
//
//        chip_discount4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                offer_discount_price.setText(
//                        Constants.percentageValueDiscount(productModel.getTotal_commission(),
//                                getString(R.string.dis1000_val)));
//                //offer_discount_price.setText(getString(R.string.dis1000_val));
//            }
//        });

        offer_discount_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String discount_offered = offer_discount_price.getText().toString().trim();
                if (!discount_offered.equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        offer_discount_price.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading_dark, getTheme())));
                    }

                    if (Double.parseDouble(discount_offered)>Double.parseDouble(productModel.getTotal_commission())) {
                        offer_discount_price.setText(Constants.fixDoubleString(productModel.getTotal_commission()));
                        Toast.makeText(getApplicationContext(), "Sorry you cant give discount more then commission", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!productModel.setDiscount(discount_offered)) {
                        Toast.makeText(getApplicationContext(), "Please enter valid number", Toast.LENGTH_SHORT).show();
                    }
                } else  {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        offer_discount_price.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading_lighter, getTheme())));
                    productModel.setDiscount(0);
                }
                final_price.setText(getString(R.string.rstart_template, Constants.fixDoubleString(productModel.getUserPriceString())) );
                String earn_text = getString(R.string.earn_dash_on_this_product, productModel.commissionAfterDiscount());
                earn_button.setText(earn_text);
                seller_commission_footer.setText(getString(R.string.your_commission_is, productModel.commissionAfterDiscount()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        String productTotalCommission  = productModel.getTotal_commission();
        try {
            float tmp_max_commission = Float.parseFloat(productTotalCommission.replaceAll("\\s+",""));
            productTotalCommission = Integer.toString((int) Math.ceil(tmp_max_commission));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String earn_text = getString(R.string.earn_dash_on_this_product, productTotalCommission);
        earn_button.setText(earn_text);
        earn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Yo Man!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProductDetails.this, PaymentDynamicActivity.class);
                //Intent intent = new Intent(ProductDetails.this, PaymentActivity.class);
                intent.putExtra("product_model",productModel);
                startActivity(intent);
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

        LoadImage(String text, String link, Bitmap bm){
            this.text = text;
            this.link = link;
            this.bm = bm;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Looper.prepare();
            try {
                Log.d("kkk ", "load image");
                //LayoutInflater  mInflater = (LayoutInflater)getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
                //RelativeLayout view = new RelativeLayout(getApplicationContext());
                //View view = mInflater.inflate(R.layout.custom_image_view, (ViewGroup) findViewById(R.id.product_root), true);
                //View view = view_group.findViewById(R.id.custom_image_root);
                //view.setVisibility(View.INVISIBLE);
                //View view = mInflater.inflate(R.layout.custom_image_view, new LinearLayout(getApplicationContext()), true);

                //Provide it with a layout params. It should necessarily be wrapping the
                //content as we not really going to have a parent for it.
                //view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                //        RelativeLayout.LayoutParams.WRAP_CONTENT));
                //view.measure();
                //Pre-measure the view so that height and width don't remain null.
                //view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                 //       View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                //Log.d("kkk ",  Integer.toString(view.getMeasuredHeight()));
                //Log.d("kkk ", Integer.toString(view.getMeasuredWidth()));

                //Assign a size and position to the view and all of its descendants
                 //view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


                //bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                //Canvas c = new Canvas(bm);

                //view.draw(c);

                //view = findViewById(R.id.cardView);
                //view.setDrawingCacheEnabled(true);
                 //       view.buildDrawingCache();
                 //       bm = Bitmap.createBitmap(view.getDrawingCache());
                //view.setDrawingCacheEnabled(false);

                /*bm = Glide.with(getApplicationContext()).
                        asBitmap().
                        load(link).
                        submit().
                        get();*/
            } catch (Exception e) { //(InterruptedException e) {
                e.printStackTrace();}
            /*} catch (ExecutionException e) {
                e.printStackTrace();
            }*/
            return null;
        }

        @SuppressLint("WrongThread")
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
                    spinner_flyer.setVisibility(View.GONE);
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

        spinner_flyer.setVisibility(View.VISIBLE);


        View view = flyerView;

        Log.d("kkk ",  Integer.toString(view.getMeasuredHeight()));
        Log.d("kkk ", Integer.toString(view.getMeasuredWidth()));

        //View view = view_group.findViewById(R.id.custom_image_root);
        //view.setVisibility(View.INVISIBLE);
        Bitmap bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        view.draw(c);
        //view.setDrawingCacheEnabled(true);
        //view.buildDrawingCache();
        //Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        //view.setDrawingCacheEnabled(false);

        new LoadImage(text,link, bm).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
