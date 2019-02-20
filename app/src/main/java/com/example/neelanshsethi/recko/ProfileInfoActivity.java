package com.example.neelanshsethi.recko;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.ManageLeadsModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileInfoActivity extends AppCompatActivity {
    TextView seller_name;
    TextView seller_mobile_no;
    TextView seller_email;
    TextView seller_location;
    ProgressBar progressBar;
    androidx.appcompat.widget.Toolbar toolbar;
    ImageView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        seller_name = findViewById(R.id.seller_name);
        seller_mobile_no = findViewById(R.id.seller_mobile_no);
        seller_location = findViewById(R.id.seller_location);
        seller_email = findViewById(R.id.seller_email);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        edit = findViewById(R.id.edit);
        fetch_details();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),EditUserInfoActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("seller_name", seller_name.getText().toString());
                intent.putExtra("seller_mobile_no", seller_mobile_no.getText().toString());
                intent.putExtra("seller_email", seller_email.getText().toString());
                intent.putExtra("seller_location", seller_location.getText().toString());
                startActivityForResult(intent, Constants.edit_user_info_request_id);
                //intent.putExtra("edit_lead_model", manageLeadsModel);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //if (fragment != null)
                //    fragment.startActivityForResult(intent, Constants.edit_missed_lead_request_id);
                //else
                //    ((Activity)mctx).startActivityForResult(intent, Constants.edit_missed_lead_request_id);
            }
        });
    }

    private void fetch_details() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid", mUser != null ? mUser.getUid() : null);
            Log.d("zzz MainActivity json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        seller_mobile_no.setText(mUser.getPhoneNumber());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/query", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz ProfileInfoActivity", APIURL.url + "user/query" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONObject data = response.getJSONObject("data");
                                String location = "";
                                String name = "";
                                String email = "";
                                try {
                                    location = data.getString("seller_location");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    name =  data.getString("seller_name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.d("zzz profile info act", email);
                                    email = data.getString("seller_email");
                                    Log.d("zzz profile info act1", email);
                                    email = Constants.fixNullString(email);
                                    Log.d("zzz profile info act2", email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("zzz profile info act", email);
                                seller_name.setText(name);
                                seller_location.setText(location);
                                seller_email.setText(email);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("zzz profile info act", "get called");
        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == Constants.edit_user_info_request_id) {
                seller_email.setText(data.getStringExtra("seller_email"));
                seller_location.setText(data.getStringExtra("seller_location"));
            }

        }
    }
}
