package com.example.neelanshsethi.sello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.sello.Adapters.Products_InCategoryAdapter;
import com.example.neelanshsethi.sello.Model.Category_InCategoryAndCompanyModel;
import com.example.neelanshsethi.sello.Model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsInCategory extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    RecyclerView rv_productslist;
    Products_InCategoryAdapter products_inCategoryAdapter;

    List productslist;
    private Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_in_category);

        toolbar=findViewById(R.id.toolbar);
        rv_productslist = findViewById(R.id.rv_productslist);
        thisActivity=(Activity)this;

        productslist = new ArrayList();

        rv_productslist.setHasFixedSize(true);
        rv_productslist.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        products_inCategoryAdapter =new Products_InCategoryAdapter(this,productslist,thisActivity);
        rv_productslist.setAdapter(products_inCategoryAdapter);
        get_products();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //can be modified
        toolbar.setTitle(this.getIntent().getStringExtra("category_name")+"'s Products");
    }

    private void get_products() {

        JSONObject json = new JSONObject();
        try {
            json.put("category_uuid",this.getIntent().getStringExtra("category_uuid"));
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"product/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"product/list"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());

                            List<ProductModel> temp = new ArrayList<ProductModel>();
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject productDetails = array.getJSONObject(i);
                                    Log.d("zzzobjct",productDetails.toString());

                                    String broucher = productDetails.getString("broucher");
                                    String video = productDetails.getString("video");
                                    String to_date = productDetails.getString("to_date");
                                    String good_time_to_sell = productDetails.getString("good_time_to_sell");
                                    String category = productDetails.getString("category");
                                    String title = productDetails.getString("title");
                                    String upfront_commission = productDetails.getString("upfront_commission");
                                    String from_date = productDetails.getString("from_date");
                                    String location_of_sell = productDetails.getString("location_of_sell");
                                    String target_customer = productDetails.getString("target_customer");
                                    String type = productDetails.getString("type");
                                    String price_on_x = productDetails.getString("price_on_x");
                                    String companyy_uuid = productDetails.getString("company_uuid");
                                    String total_commission = productDetails.getString("total_commission");
                                    String tips_to_sell = productDetails.getString("tips_to_sell");
                                    String customer_data_needed = productDetails.getString("customer_data_needed");
                                    String product_details = productDetails.getString("product_details");
                                    String payment_type = productDetails.getString("payment_type");
                                    String product_uuid = productDetails.getString("product_uuid");
                                    String mrp = productDetails.getString("mrp");

                                    ProductModel productModel = new ProductModel(broucher,video,to_date,good_time_to_sell,category,title,upfront_commission,from_date,location_of_sell,target_customer,type,price_on_x,companyy_uuid,total_commission,tips_to_sell,customer_data_needed,product_details,payment_type,product_uuid,mrp);
                                    productslist.add(productModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            products_inCategoryAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
