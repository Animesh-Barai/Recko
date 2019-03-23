package com.recko.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.recko.app.Misc.Constants;
import com.recko.app.Model.AccountModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    TextView payee_name;
    TextView account_no;
    TextView bank_name;
    TextView ifsc_code;
    androidx.appcompat.widget.Toolbar toolbar;
    ProgressBar progressBar;
    ImageView edit;
    AccountModel accountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        progressBar = findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.GONE);
        payee_name = findViewById(R.id.payee_name);
        account_no = findViewById(R.id.account_no);
        bank_name = findViewById(R.id.bank_name);
        ifsc_code = findViewById(R.id.ifsc_code);
        accountModel = new AccountModel();

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
                Intent intent= new Intent(getApplicationContext(),AccountEditActivity.class);
                intent.putExtra("account", accountModel);
                startActivityForResult(intent, Constants.edit_account_info_request_id);
            }
        });
    }

    protected void fetch_details(String auth_code) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid", mUser != null ? mUser.getUid() : null);
            json.put("firebase_token",auth_code);
            Log.d("zzz AccountAct json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"account/fetch", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz AccountAct", APIURL.url + "account/fetch" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONObject data = response.getJSONObject("data");
                                accountModel.populateUsingJson(data);
                                payee_name.setText(accountModel.getPayee_name());
                                account_no.setText(accountModel.getAccount_no());
                                bank_name.setText(accountModel.getBank_name());
                                ifsc_code.setText(accountModel.getIfsc_code());
                                progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    protected void fetch_details() {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            fetch_details(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("zzz account", "get called");
        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == Constants.edit_account_info_request_id) {
                accountModel = (AccountModel) data.getSerializableExtra("account");
                boolean updated = data.getBooleanExtra("updated", false);
                if (updated) {
                    payee_name.setText(accountModel.getPayee_name());
                    account_no.setText(accountModel.getAccount_no());
                    bank_name.setText(accountModel.getBank_name());
                    ifsc_code.setText(accountModel.getIfsc_code());
                } else {
                    Toast.makeText(getApplicationContext(),"Update request submitted we will look into it",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
