package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import com.google.android.material.chip.Chip;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo extends AppCompatActivity {

    EditText name,location;
    Button next;
    private String NAME, LOCATION;
    Chip chip;
    private int toggle=0;
    private String idToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name=findViewById(R.id.name);
        location=findViewById(R.id.location);
        next=findViewById(R.id.Next);
        chip=findViewById(R.id.chip);

        name.requestFocus();

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                             idToken = task.getResult().getToken();
                             Log.d("tokennn",idToken);
                            // Send token to your backend via HTTPS
                            // ...
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NAME=name.getText().toString().trim();
                LOCATION=location.getText().toString().trim();
                JSONObject json = new JSONObject();
                try {
                    json.put("seller_uuid",mUser.getUid());
                    json.put("seller_name",NAME);
                    json.put("seller_mobile_no",LOCATION);
                    json.put("firebase_token",idToken);
                    Log.d("zzz json", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/insert", json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("zzz", APIURL.url+"user/insert"+"\nonResponse: "+response);

                                try {
                                    name.setText(response.getString("msg") + response.get("code"));
                                    if(response.getString("code").equals("200"))
                                    {
                                        Intent intent=new Intent(UserInfo.this,Industries.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(UserInfo.this,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                    }
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

                RequestQueue requestQueue = Volley.newRequestQueue(UserInfo.this);
                requestQueue.add(jsonObjectRequest);
            }
        });

    }
}
