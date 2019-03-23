package com.recko.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.recko.app.Model.ManageLeadsModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class LeadInfoActivity extends AppCompatActivity {
    TextView client_name, product_name, discount_offered, followup_date, notes;
    androidx.appcompat.widget.Toolbar toolbar;
    ManageLeadsModel leadsModel;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat iso8061 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_info);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        leadsModel = (ManageLeadsModel) getIntent().getSerializableExtra("lead_model");
        client_name = findViewById(R.id.client_name);
        client_name.setText(leadsModel.getContact_name());

        discount_offered = findViewById(R.id.discount_offered);
        discount_offered.setText(leadsModel.getAmount_communicated());

        notes = findViewById(R.id.Notes);
        notes.setText(leadsModel.getComment());
        if (leadsModel.getComment().trim().length() == 0) notes.setText("None");

        product_name = findViewById(R.id.product_name);
        product_name.setText(leadsModel.getProduct_name());

        followup_date = findViewById(R.id.followup_date);
        String followupDate = leadsModel.getDeadline_time().split("T")[0];
        try {
            Date date = iso8061.parse(followupDate);
            followup_date.setText(simpleDateFormat2.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
