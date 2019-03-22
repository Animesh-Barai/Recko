package com.example.neelanshsethi.recko;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Adapters.EditIndustryAdapter;
import com.example.neelanshsethi.recko.Adapters.IndustryAdapter;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.IndustryChipModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class IndustryEditActivity extends AppCompatActivity {
    private static String TAG = IndustryEditActivity.class.getSimpleName();
    GridView gridView;
    Button save;
    List industryModellist;
    EditIndustryAdapter editIndustryAdapter;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_industries);
        save = findViewById(R.id.save);
        gridView = findViewById(R.id.gridview);

        industryModellist = new ArrayList();
        editIndustryAdapter= new EditIndustryAdapter(this,industryModellist);
        gridView.setAdapter(editIndustryAdapter);
        getdata();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editIndustryAdapter.setAllSelectedChips();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editIndustryAdapter.getSelectedJsonArray().length() ==0) {
                        Toast.makeText(getApplicationContext(),"Please select atleast one industry",Toast.LENGTH_SHORT).show();
                    } else
                        send_industries();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getdata(){
        JSONObject json = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,APIURL.url+"industry/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"industry/list"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.d("zzzobjct",object.toString());
                                    String pic_base64 = object.getString("image_url");
                                    String name = object.getString("name");
                                    Log.d("zzz",pic_base64);
                                    String uuid=object.getString("industry_uuid");
                                    if(pic_base64.contains("data")) {

                                        //for(int j=0;j<18;j++) {

                                        Log.d("zzz j", String.valueOf(" " + i));
                                        String base64Image = pic_base64.split(",")[1];
                                        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        Drawable d = new BitmapDrawable(getResources(), decodedByte);

                                        IndustryChipModel industryChipModel =new IndustryChipModel(d,name,uuid);
                                        industryModellist.add(industryChipModel);
                                        //}

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            editIndustryAdapter.notifyDataSetChanged();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void send_industries(String firebase_token)
    {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        final JSONArray selectedChips = editIndustryAdapter.getSelectedJsonArray();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("industry_uuids",editIndustryAdapter.getSelectedJsonArray());
            json.put("firebase_token",firebase_token);
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/insert_industries", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"user/insert_industries"+"\nonResponse: "+response);

                        try {
                            if(response.getString("code").equals("200"))
                            {
                                Log.d(TAG, "clearing industry cache");
                                Volley.newRequestQueue(getApplicationContext()).getCache().remove(APIURL.url+"industry/list");
                                List uuids = new ArrayList();
                                for (int ii=0;ii<selectedChips.length();ii++) uuids.add(selectedChips.getString(ii));
                                Constants.getInstance().setIndustryUUids(uuids);
                                Intent intent=new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Oooops! Please try again later",Toast.LENGTH_SHORT).show();
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

    private void send_industries() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            send_industries(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
