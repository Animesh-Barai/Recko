package com.example.neelanshsethi.recko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditUserInfoActivity extends AppCompatActivity {
    ProgressBar progressBar;
    String sellerName;
    String sellerMobileNo;
    String sellerEmail;
    String sellerLocation;
    TextView seller_name, seller_mobile_no, seller_email, seller_location;
    FirebaseAnalytics mFirebaseAnalytics;
    Button save;
    androidx.appcompat.widget.Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.GONE);
        seller_name = findViewById(R.id.seller_name);
        seller_mobile_no = findViewById(R.id.seller_mobile_no);
        seller_email = findViewById(R.id.seller_email);
        seller_location = findViewById(R.id.seller_location);

        Intent intent = getIntent();
        sellerName = intent.getStringExtra("seller_name");
        sellerMobileNo = intent.getStringExtra("seller_mobile_no");
        sellerEmail = intent.getStringExtra("seller_email");
        Log.d("zzk seller email", sellerEmail);
        sellerLocation = intent.getStringExtra("seller_location");
        seller_name.setText(sellerName);
        seller_mobile_no.setText(sellerMobileNo);
        seller_email.setText(sellerEmail);
        seller_location.setText(sellerLocation);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Constants.validateEmail(seller_email.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Please provide valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(seller_location.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide your location", Toast.LENGTH_SHORT).show();
                    return;
                } else
                    save_user_change();
            }
        });
    }

    protected void save_user_change(String auth_token) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("firebase_token",auth_token);
            json.put("seller_email",seller_email.getText().toString());
            json.put("seller_location",seller_location.getText().toString());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/update", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "user/update" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                Intent intent=new Intent();
                                intent.putExtra("seller_email",seller_email.getText().toString());
                                intent.putExtra("seller_location",seller_location.getText().toString());
                                Log.d("zzz user update ", "done");
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    protected void save_user_change() {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = new Bundle();
        bundle.putString("user_id", mUser.getUid());
        bundle.putString("seller_email",seller_email.getText().toString());
        bundle.putString("seller_mobile_no",seller_mobile_no.getText().toString());
        mFirebaseAnalytics.logEvent("user_info_edited", bundle);

        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            save_user_change(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
