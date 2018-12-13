package com.example.neelanshsethi.sello;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

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
    private String idToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        name=findViewById(R.id.name);
        location=findViewById(R.id.location);
        next=findViewById(R.id.Next);
        chip=findViewById(R.id.chip);




        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                             idToken = task.getResult().getToken();
                             Log.d("token",idToken);
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
                    json.put("seller_uuid","9999");
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



        String base64String = " data:image/png;base64,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";
        String ss="data:image\\/png;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAYAAAA6\\/NlyAAAABHNCSVQICAgIfAhkiAAAB9RJREFUaIHdm2lsFOcZx3+v9\\/BiYy4BdiHiSBygpFwhAoukatIq4mohRKExDW2DoC0iPUhBLSqlH1L1AyIiTZSGhjQpoqHlKgUijIuKEoXiFCsBUsxlIBQwYGMKsfGx18y\\/H9Y29nptvOsZG\\/qTVtqdnXne5z\\/v8z4z72VwEUmzgclADjAw7gNwLe5TDhwyxuxy0y\\/HkNRX0nxJ2yTdUurcarDxnKS+3a2rFZImSCrshMA7sVfSI92tE0m5DTVhuyi2EVvSVkkjukPoEEnrJUW6QGg8EUlvShrUVWJnSqrqBqHxVEma5bbYX6lrwrej2JJWuiE0IGlzN4trj3cl9eiIFtMBsTlAITCuszfOZY4AM40xV9s7qV3BkgLAR8B4Bx1zkyPAFGNMsK0T0u5gYCP3jliACcCG9k5oU7CkFcBcpz3qAp5t8D0hCUNa0jPAFu4cAXcrNrH2XBj\\/RyvBkrKAc8CALnDMTSqAEcaY6uYHE9XgSrpZ7IELFtM21jN7U31nzGQDv4g\\/2KKGJQ0BSoH0zpSUCmELCkqjrCuOkOk3PNDPcPiKzf4FHXq8tkUQGGqMudZ4IL6GX6KLxUqwrSTKyFdr2Xwsyuqp6eyYF+Br93udMB8gpqmJJquSvgjMd6KUjvLhBYslu0P0SofC7\\/RgZH9XcuQiSa8bY0qgmWDgFcCTqtUz\\/xWHyiw+Lbc4VmETjkJNWJy6btPTb8jtZxjRP40nhnuZN9bLjhNRlhaEeHlaOvljHKnNtvAALwPToKENN2Tm64A\\/WWv5W4NsPx6lf4ZhxggP43I8DO5l6J\\/R8gFQFRJFFy0KSi2y\\/FD0\\/YyE9g5ftRmfk8a+sxZr\\/hlm3awAj71Vy3cn+FgzNeXWFgYGNGVsSfmpvrX3+c0tVdR0vAN1o07KfKnl6M+Ja5Z+9veg7l9bowdfqdHNemlvaVRffadOknT4iqWHXqtN1cVG8uF20noq2VtWUaNU73YTlbVieWGICW\\/UcbFKbHg6wOmfZNIn4Eq50+C24OnJXj18bS0RK9WyYxRdsqisE8WLM\\/jL3ABfHurhSLmN3Yamy9Uib31dqsU9CeCVlAf0SvbqsEUrx0qu2ew6GeVYhc21ZjWRlQ5zRvt4fkLL5DR7lJfZo7ycrLT5+b4Q249H8RgoXpyZsMyQRWdu8iBJeV4gL2UTcawrjpDpg+kPehna53bSul4nqoKtq62yVqw+EOb1QxHmjPay4ekAjw3xYO7YS0+ZPC8wzClrv\\/t6clm0eUiPzY61rtPXbbeexwA5XmKzAo4QteFfZRYlFXaL5JKVbnjyAQ9jslsKaQzpiAUFpRZbSyIcvGhR9L3EjywHcFbwE+\\/UEbZg9MA0hvW5Le6zGzY7w2JMduvH\\/HunLZYWBJl0n4clk3z8cU7AzZAe5mhIH1iUXM28\\/UmEHSei\\/O1bPZpC2mW6N6QXTvSxcKKvKaS3HY+w+1SUM0t7OuVSPDleYl0oR3pInQlpgAUP+\\/h4cSb9OtUjbB8vsSnK3k4YSyWkf\\/1BmGWP+lk00UcPnxNetEt5o+CRTlhLNaQBqoKwpSTKqv0hjixxLUuXpxET7AjTNtazbG+IQ2UtX4c+u2HzRnEk4TUVNWL+9iCT36ylrNrmw4UZrXpaDtJUw47wj+eTa3y7TkVZsS\\/Ekkl+3pod6NKQdpSTla1Demx2Gr644YXGF4\\/mWDbsO2fx2kdhvM4\\/qcq9wFGnrC3aGWTj0Sj39TItsnRlnRiTncaf57bd77tUJf7wSYQNRyIMyjIsmuhj\\/jjHq\\/yoF9gPVOFApl46xc+rMwJkJjFuUlYtXiwIUXTJ4qdT\\/BT\\/IIPsnq604Wpgv9cYE5FUCDzbWYtfGtjxGKwKwtqiMH86GuHFKX7efSZAuqtDW+w1xkQaPdzpalFxhCwY9VotdRHx6QuZ\\/CjP57ZYaNDYWEwBsYGupAfx2iJiwdUaceWWCEZEnx6G8Tlp9PTHktWaqekM7xsL3cpaceaGCEdjia53IJYD+jr3xhUG9kCDYGNMtaT3gampWHvvVJTzN8XZGzbnb9pcrhaZfhicFQug\\/3xuc7FKjOyfxtRcD\\/PGellXHObjyzbVIdEr\\/XabrQmL45U2oWhspGR8jocpQ1IePW7kfWPMrSbBDSwnCcEZvthIxtRcL+duiGF9Dflj\\/Tw0IC1h964qCAcvWuw+FSV\\/a5AXJvlY\\/1SA3H5tJ6gLn4sPzlucqLT4ynAP1+tE70BKCW1Z45f4uaXNdDB5Td9YjycNlj+aXCuoCcM3t9RT8O3k43XFvhAzR3pZ9XhSZW4xxuQ3\\/ogXPAw4SwdmIMprxI\\/3hNhTGiUUTab81Bjcy7DqcT8LH\\/YlM0AQBEYZYy60eYak33d2xPsu4rfx+hJNiA8ELhCbebuXqQZyjTGVzQ+2elNomEv9YVd55SIL4sVCG2s4jDFvA6tdd8k9VhtjdiT6o83mL8kQW9hyr63k+Ssw1xiTcMLm\\/21h2r+BySkvTGu48BvAJYcdc4MyYEZ7YqED67CMMWXExrw2OeSYG2witkTpsqNWJa3U3bd8+JeOikwgepakTk\\/HO4D7C8SbiR6k2BtZd20BWC\\/pC10iNk74CElb1HWbPLZJyu1yoQmEP6LYVhu3KJQ0obt1tkKxjVrPybmNWvPl8EYt92ZiSWkr3lXgoDGmwC2f\\/gf2P5TuB3OweQAAAABJRU5ErkJggg==";
        String base64Image = ss.split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable d = new BitmapDrawable(getResources(),decodedByte);
        chip.setChipIcon(d);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(chip.isChecked()) {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ff0070")));
                        chip.setTextColor(Color.WHITE);
                        chip.setCheckedIconVisible(false);
                    }
                    else{
                        chip.setChipBackgroundColor((ColorStateList.valueOf(Color.parseColor("#EBEBEB"))));
                        chip.setTextColor(Color.parseColor("#484848"));
                        chip.setChipIconVisible(true);
                        chip.setCheckedIconVisible(false);
                    }
                }

            });

    }
}
