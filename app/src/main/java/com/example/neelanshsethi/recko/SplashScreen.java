package com.example.neelanshsethi.recko;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkRecur();
    }

    private void checkRecur()
    {
        if(!InternetConnection.checkConnection(SplashScreen.this)) {

            Snackbar.make(findViewById(android.R.id.content),"No Internet Connection. Please connect to Internet to proceed!",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkRecur();
                        }
                    }).show();

        }
        else
        {
            proceed();
        }
    }
//    protected void onStart() {
//        super.onStart();
//
//        if (user != null) {
//            // User is signed in
//            Intent intent = new Intent(this, ProfileActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//        } else {
//            // No user is signed in
//        }
//    }

    private void proceed()
    {
        SharedPreferences pref=getSharedPreferences("status",MODE_PRIVATE);
        final boolean test=pref.getBoolean("first",true);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (test) {
                    fetch_details();
                }
                else {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    /*Intent i = new Intent(SplashScreen.this, GetStarted.class);
                    startActivity(i);
                    finish();*/
                }

            }
        },3000);


    }

    private void goto_navigation() {
        Intent intent=new Intent(this,NavigationDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}

