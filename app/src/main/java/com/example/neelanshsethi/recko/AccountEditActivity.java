package com.example.neelanshsethi.recko;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Model.AccountModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AccountEditActivity extends AppCompatActivity {
    ProgressBar progressBar;
    AccountModel accountModel;

    EditText payee_name;
    EditText account_no;
    EditText bank_name;
    EditText ifsc_code;

    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_details);
        progressBar = findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.GONE);

        accountModel = (AccountModel) getIntent().getSerializableExtra("account");
        payee_name = findViewById(R.id.payee_name);
        account_no = findViewById(R.id.account_no);
        bank_name = findViewById(R.id.bank_name);
        ifsc_code = findViewById(R.id.ifsc_code);

        payee_name.setText(accountModel.getPayee_name());
        account_no.setText(accountModel.getAccount_no());
        bank_name.setText(accountModel.getBank_name());
        ifsc_code.setText(accountModel.getIfsc_code());

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(payee_name.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Provide payee name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(account_no.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Provide account no", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(bank_name.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Provide bank name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(ifsc_code.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Provide ifsc code", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    save_details();
            }
        });
    }

    protected void save_details(String auth_token) {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid", mUser != null ? mUser.getUid() : null);
            json.put("firebase_token",auth_token);
            json.put("payee_name", payee_name.getText().toString());
            json.put("ifsc_code", ifsc_code.getText().toString());
            json.put("bank_name", bank_name.getText().toString());
            json.put("account_no", account_no.getText().toString());
            Log.d("zzz AccountAct json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"account/add", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz AccountAct", APIURL.url + "account/add" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                boolean updated = response.getBoolean("updated");
                                Intent intent=new Intent();
                                intent.putExtra("account",accountModel);
                                intent.putExtra("updated",updated);

                                if (updated) {
                                    accountModel.setPayee_name(payee_name.getText().toString());
                                    accountModel.setAccount_no(account_no.getText().toString());
                                    accountModel.setBank_name(bank_name.getText().toString());
                                    accountModel.setIfsc_code(ifsc_code.getText().toString());
                                }
                                progressBar.setVisibility(View.GONE);

                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
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

    protected void save_details() {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            save_details(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
