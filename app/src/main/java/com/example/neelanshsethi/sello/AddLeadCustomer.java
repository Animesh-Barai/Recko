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
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
    SimpleDateFormat iso8061 = new SimpleDateFormat("yyyy-MM-dd");
    private String contact_email;
    private String contact_number;
    private String product_uuid;
    List<Pair<String,String>> productslist = new ArrayList<Pair<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead_customer);
        save = findViewById(R.id.crm_save);
        materialSpinner = findViewById(R.id.spinner);

//        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        get_spinner_products();

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
                                    String iso = iso8061.format(date);
                                    Log.d("zzz",date.toString());
                                    Log.d("zzziso",iso);
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
            json.put("seller_uuid", mUser != null ? mUser.getUid() : null);
            json.put("deadline_time",iso8061.format(date));
            json.put("industry_uuid","");
            json.put("contact_name",client_details.getText().toString().trim());
            json.put("contact_no",contact_number);
            json.put("email",contact_email);
            json.put("comment",notes.getText().toString().trim());
            json.put("amount_communicated",price.getText().toString().trim());
            for(Pair <String,String> temp : productslist)
            {
                if(temp.first.equals(materialSpinner.getSelectedItem().toString())) {
                    product_uuid = temp.second;
                    Log.d("zzz prodid",product_uuid);
                    break;
                }
            }
            if(product_uuid!=null)
                json.put("product_uuid", product_uuid);

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
                                Intent intent=new Intent();
                                intent.putExtra("MESSAGE","hey");
                                setResult(RESULT_OK,intent);
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
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Error retrieving contact", Toast.LENGTH_LONG).show();
            client_details.setText("");
        }
    }

    private void get_spinner_data(List<Pair<String,String>> list) {
        //String[] options = {"Andha Paisa", "Andhi Daaru", "Sutta", "Maal", "Andhi Chaudh"};
        Collections.sort(list, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> stringStringPair, Pair<String, String> t1) {
                return stringStringPair.first.compareToIgnoreCase(t1.first);
            }
        });
        List<String> temp = new ArrayList<String>();
        for(int i = 0; i < list.size();i++) {
            temp.add(list.get(i).first);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        materialSpinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    private void get_spinner_products() {

        JSONObject json = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"product/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"product/list"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());

//                            List<ProductModel> temp = new ArrayList<ProductModel>();
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject productDetails = array.getJSONObject(i);
                                    Log.d("zzzobjct",productDetails.toString());

                                    String broucher = productDetails.getString("broucher");
                                    String video = productDetails.getString("video");
                                    String to_date = productDetails.getString("to_date");
                                    String good_time_to_sell = productDetails.getString("good_time_to_sell");
                                    String category = productDetails.getString("category");
                                    String title = productDetails.getString("title");
                                    String upfront_commission = productDetails.getString("upfront_commission");
                                    String from_date = productDetails.getString("from_date");
                                    String location_of_sell = productDetails.getString("location_of_sell");
                                    String target_customer = productDetails.getString("target_customer");
                                    String type = productDetails.getString("type");
                                    String price_on_x = productDetails.getString("price_on_x");
                                    String companyy_uuid = productDetails.getString("company_uuid");
                                    String total_commission = productDetails.getString("total_commission");
                                    String tips_to_sell = productDetails.getString("tips_to_sell");
                                    String customer_data_needed = productDetails.getString("customer_data_needed");
                                    String product_details = productDetails.getString("product_details");
                                    String payment_type = productDetails.getString("payment_type");
                                    String product_uuid = productDetails.getString("product_uuid");
                                    String mrp = productDetails.getString("mrp");

                                    //ProductModel productModel = new ProductModel(broucher,video,to_date,good_time_to_sell,category,title,upfront_commission,from_date,location_of_sell,target_customer,type,price_on_x,companyy_uuid,total_commission,tips_to_sell,customer_data_needed,product_details,payment_type,product_uuid,mrp);
                                    //productslist.add(productModel);
                                    productslist.add(new Pair<String,String>(title,product_uuid));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            get_spinner_data(productslist);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
