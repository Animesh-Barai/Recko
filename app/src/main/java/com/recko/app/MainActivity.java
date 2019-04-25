package com.recko.app;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recko.app.Misc.Constants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMobile;
    private boolean should_allow_navigation_to_next_page = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Button buttonContinue;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_main);
        editTextMobile = findViewById(R.id.phonenumber);

        buttonContinue = findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, VerifyPhoneNumber.class);
                intent.putExtra("mobile", mobile);
                if (should_allow_navigation_to_next_page) {
                    sendNumber(mobile);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Please wait trying to contact our servers",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !Constants.is_test_start) {
            // User is signed in
            //Intent intent=new Intent(this,NavigationDashboard.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //startActivity(intent);
            //finish();
            Log.d("zzz MainActivity","user is logged in");
            Bundle bundle = new Bundle();
            bundle.putString("user_id", user.getUid());
            mFirebaseAnalytics.logEvent("already_logged_in", bundle);
            Intent intent=new Intent(this,NavigationDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            //fetch_details();
        } else {
            should_allow_navigation_to_next_page = true;
           Log.d("zzz MainActivity", "user is not logged in");
        }

    }

    private void sendNumber(String mobile) {
        Map<String, Object> data = new HashMap<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("phone_numbers").document(mobile).set(data);
    }

    private void goto_navigation() {
        Intent intent=new Intent(this,NavigationDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/query", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz MainActivity", APIURL.url + "user/query" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONObject data = response.getJSONObject("data");
                                String location = "";
                                String name = "";
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
                                if (StringUtils.isAnyEmpty(name, location)){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    bundle.putInt("quit_before_user_info", 1);
                                    mFirebaseAnalytics.logEvent("quit_before_user_info", bundle);

                                    // We don't have name/location send him/her to UserInfo page.
                                    Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    JSONArray industries = null;
                                    try {
                                        industries =  data.getJSONArray("industries");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mFirebaseAnalytics.logEvent("relogin", bundle);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (industries!=null && industries.length() > 0) {
                                        goto_navigation();
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mFirebaseAnalytics.logEvent("quit_before_industries", bundle);

                                        // We don't have industry list for user send him/her to pick industries page.
                                        Intent intent = new Intent(getApplicationContext(), Industries.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
				Constants.logVolleyError(error);
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}


