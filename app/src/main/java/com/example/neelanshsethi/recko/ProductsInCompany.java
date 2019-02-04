package com.example.neelanshsethi.recko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.neelanshsethi.recko.Adapters.Products_InCompanyAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductsInCompany extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    RecyclerView rv_productslist;
    Products_InCompanyAdapter products_inCompanyAdapter;

    List productslist;
    private Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_in_company);

        toolbar=findViewById(R.id.toolbar);
        rv_productslist = findViewById(R.id.rv_productslist);
        thisActivity=(Activity)this;

        productslist = new ArrayList();

        productslist = (List) this.getIntent().getSerializableExtra("product_list");

        rv_productslist.setHasFixedSize(true);
        rv_productslist.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        products_inCompanyAdapter =new Products_InCompanyAdapter(this,productslist,thisActivity);
        rv_productslist.setAdapter(products_inCompanyAdapter);


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
        toolbar.setTitle(this.getIntent().getStringExtra("company_name")+"'s Products");
    }
//
//    private void get_products() {
//
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
