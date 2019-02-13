package com.example.neelanshsethi.recko;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ReferActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    private EditText client_details;
    private static int PICK_CONTACT=1;
    private static String mobile_no;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        toolbar=findViewById(R.id.toolbar);
        client_details=findViewById(R.id.crm_name_text);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        save = findViewById(R.id.crm_save);

        client_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                }
                else
                {
                    Uri uri = Uri.parse("content://contacts");
                    Intent intent = new Intent(Intent.ACTION_PICK, uri);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_CONTACT);
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isEmpty(mobile_no)) {
                    Toast.makeText(getApplicationContext(), "Please select a contact", Toast.LENGTH_SHORT).show();
                    return;
                }
                get_refer_link();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_CONTACT) {
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    String number = "";
                    String name = "";
                    String email = "";
                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                    cursor.moveToFirst();
                    String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    if (hasPhone.equals("1")) {
                        Cursor phones = getContentResolver().query
                                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + contactId, null, null);
                        if (phones != null) {
                            while (phones.moveToNext()) {
                                number = phones.getString(phones.getColumnIndex
                                        (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                                name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            }
                        }
                        phones.close();
                    } else {
                        Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                    }

                    Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contactId}, null);
                    if (emailCur != null) {
                        while (emailCur.moveToNext()) {
                            email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        }
                    }
                    emailCur.close();

                    //Do something with number
                    mobile_no = number;
                    client_details.setText(String.format("%s %s %s", name, number, email));
                    client_details.setFocusableInTouchMode(false);
                    client_details.clearFocus();
                    cursor.close();
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error retrieving contact", Toast.LENGTH_LONG).show();
            client_details.setText("");
        }
    }

    public void send_whatsapp(String msg) {

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            PackageManager pm=getPackageManager();
            String url = "https://api.whatsapp.com/send?phone="+ mobile_no +"&text=" + URLEncoder.encode(msg, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(pm) != null) {
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Whatsapp has not been installed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Whatsapp has not been installed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void get_refer_link(String auth_token) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("firebase_token",auth_token);
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"refer/get_link", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "refer/get_link" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                String refer_link = response.getString("referral_link");
                                send_whatsapp(refer_link);
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

    public void get_refer_link() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            get_refer_link(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
