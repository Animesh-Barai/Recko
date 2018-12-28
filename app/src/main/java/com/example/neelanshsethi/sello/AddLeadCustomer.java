package com.example.neelanshsethi.sello;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.sello.Model.Company_InCategoryAndCompanyModel;
import com.example.neelanshsethi.sello.Model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AddLeadCustomer extends Activity {

    androidx.appcompat.widget.Toolbar toolbar;
    private EditText client_details;
    private EditText price;
    private EditText followup_date;
    private EditText notes;
    private MaterialSpinner materialSpinner;
    private static int PICK_CONTACT=1;
    private Date date;
    Calendar c = Calendar.getInstance();
    Button save;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat iso8061 = new SimpleDateFormat("yyyyMMdd");
    private String contact_email;
    private String contact_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead_customer);
        save = findViewById(R.id.crm_save);
        materialSpinner = findViewById(R.id.spinner);

//        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        get_spinner_data();

        client_details=findViewById(R.id.crm_name_text);
        price=findViewById(R.id.crm_price_text);
        followup_date=findViewById(R.id.crm_followup_date_text);
        notes=findViewById(R.id.crm_notes_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            client_details.setShowSoftInputOnFocus(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            followup_date.setShowSoftInputOnFocus(false);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(client_details.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Client Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(materialSpinner.getSelectedItem()==null) {
                    Toast.makeText(getApplicationContext(), "Select Product Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (price.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Select Price", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (followup_date.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Select Follow-up Date",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    save_details();

            }
        });


        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

        followup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeadCustomer.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, final int year,
                                                  final int monthOfYear, final int dayOfMonth) {

                                final int newmonth = monthOfYear+1;
                                try {
                                    date = simpleDateFormat.parse(dayOfMonth+" "+newmonth+" "+year);
                                    Log.d("zzz",date.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                followup_date.setText(simpleDateFormat2.format(date));
                                followup_date.clearFocus();
                                followup_date.setFocusableInTouchMode(false);
                                notes.requestFocus(View.FOCUS_DOWN);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!price.getText().toString().trim().equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        price.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading_dark, getTheme())));
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    price.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading_lighter, getTheme())));

                }
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void save_details() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("deadline_time",iso8061.format(date));
            json.put("industry_uuid","");
            json.put("contact_name",client_details.getText().toString().trim());
            json.put("contact_no",contact_number);
            json.put("email",contact_email);
//            json.put("product_uuid","");
            json.put("comment",notes.getText().toString().trim());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"lead/insert", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url + "lead/insert" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            String msg = response.getString("msg");
                            if (code.equals("200") && msg.equals("Done")) {
                                Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();
                                finish();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
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
                                contact_number=number;
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
                            contact_email=email;
                        }
                    }
                    emailCur.close();

                    //Do something with number
                    client_details.setText(name + " " + number + " " + email);
                    client_details.setFocusableInTouchMode(false);
                    client_details.clearFocus();
                    //materialSpinner.requestFocus(View.FOCUS_DOWN);
                    materialSpinner.performClick();
                    cursor.close();
                }
                }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error retreiving contact", Toast.LENGTH_LONG).show();
            client_details.setText("");
        }
    }

    private void get_spinner_data() {
        String[] options = {"Andha Paisa", "Andhi Daaru", "Sutta", "Maal", "Andhi Chaudh"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        materialSpinner.setAdapter(adapter);

        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                price.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

}
