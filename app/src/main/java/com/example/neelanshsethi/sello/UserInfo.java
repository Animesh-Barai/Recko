package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.chip.Chip;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends AppCompatActivity {

    EditText name,location;
    Button next;
    private String NAME, LOCATION;
    Chip chip;
    private int toggle=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name=findViewById(R.id.name);
        location=findViewById(R.id.location);
        next=findViewById(R.id.Next);
        chip=findViewById(R.id.chip);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UserInfo.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        NAME=name.getText().toString().trim();
        LOCATION=location.getText().toString().trim();

        JSONObject json = new JSONObject();
        try {
            json.put("name",NAME);
            json.put("location",LOCATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,APIURL.url+"industry/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.i("zzz", APIURL.url+"industry/list"+"\nonResponse: "+response);

                        try {
                            name.setText(response.getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                                            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(UserInfo.this,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
//        {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String ,String > head=new HashMap<>();
//                head.put("x-auth",us.getUserDetails().get("token"));
//                return  head;
//            }
//        };

        RequestQueue requestQueue = Volley.newRequestQueue(UserInfo.this);
        requestQueue.add(jsonObjectRequest);



        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(toggle%2==0) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ff0070")));
                    chip.setTextColor(Color.WHITE);
                }
                else{
                    chip.setChipBackgroundColor((ColorStateList.valueOf(Color.parseColor("#808080"))));
                    chip.setTextColor(Color.parseColor("#484848"));
                    chip.setChipIconVisible(false);

                }

                toggle=(toggle+1)%2;

            }
        });



    }



}
