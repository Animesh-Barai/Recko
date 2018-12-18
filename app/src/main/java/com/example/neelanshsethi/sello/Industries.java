package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.google.android.material.chip.Chip;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Industries extends AppCompatActivity {


    GridView gridView;
//    String [] industry_names={"Managements","Finance","Medical","Lorem Ipsum","Neelansh","Abhishek","Chinmay","Khagesh"};
//  int [] industry_logos={R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education,R.drawable.chip_education};
    private Drawable [] industry_logos=new Drawable[30];
//    String [] industry_names = new String[20];
    private List<String> industry_names = new ArrayList<String>();
//    List<String > industry_logos = new ArrayList<String>();
    IndustryAdapter industryAdapter;
    Button next;
     private static List<String> selectedChips = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industries);

        next= findViewById(R.id.Next);
        gridView=findViewById(R.id.gridview);
        industryAdapter= new IndustryAdapter(this, industry_names, industry_logos);
        gridView.setAdapter(industryAdapter);

        getdata();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(InternetConnection.checkConnection(Industries.this)) {
                    Intent intent = new Intent(Industries.this, ForYou.class);
                    startActivity(intent);
                    Log.d("zzz", selectedChips.toString());
                }
                else
                {
                    Intent intent = new Intent(Industries.this, AwwSnap.class);
                    startActivity(intent);
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
                                    if(pic_base64.contains("data")) {

                                        for (int j = 0; j < 8; j++) {
                                            Log.d("zzz j",String.valueOf(j+" " +i));
                                            String base64Image = pic_base64.split(",")[1];
                                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                            Drawable d = new BitmapDrawable(getResources(), decodedByte);
                                            industry_logos[j]=d;
                                            industry_names.add(j,name);
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            industryAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(Industries.this,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Industries.this);
        requestQueue.add(jsonObjectRequest);
    }

    void selectChips(Chip chip)
    {
        selectedChips.add(chip.getText().toString());
        Log.d("zzz added",chip.getText().toString() +" "+selectedChips.toString());
    }

    void removeChips(Chip chip)
    {
        selectedChips.remove(chip.getText().toString());
        Log.d("zzz remove",chip.getText().toString() +" "+selectedChips.toString());
    }

    void clearchips()
    {
        selectedChips.clear();
    }
}
