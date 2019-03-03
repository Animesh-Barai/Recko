package com.example.neelanshsethi.recko;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

public class PaymentActivity extends Activity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private String productUUID;
    private String sellerUUID;
    private ProductModel productModel;
    androidx.appcompat.widget.Toolbar toolbar;
    private EditText end_user_name, end_user_mobile_no, end_user_email, amount_communicated;
    private boolean picked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_user_sell_info);

        picked = false;
        Intent intent = getIntent();
        sellerUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        productModel = (ProductModel) intent.getSerializableExtra("product_model");
        productUUID = productModel.getProduct_uuid();

        end_user_name = findViewById(R.id.end_user_name_text);
        end_user_mobile_no = findViewById(R.id.end_user_mobile_no);
        end_user_email = findViewById(R.id.end_user_email);
        amount_communicated = findViewById(R.id.amount_communicated);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        end_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                end_user_name.setOnClickListener(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                }
                else
                {
                    Uri uri = Uri.parse("content://contacts");
                    Intent intent = new Intent(Intent.ACTION_PICK, uri);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, Constants.pick_contact_request_id);
                }

            }
        });

        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = (Button) findViewById(R.id.btn_pay);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getEndUserAmountCommunicated().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(getEndUserName().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide valid name", Toast.LENGTH_SHORT).show();
                    return;
                } else if(getEndUserMobileNo().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please provide valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else
                 createInvoice();
            }
        });
    }

    public void createInvoice() {
        JSONObject json = new JSONObject();
        try {
            // '{"seller_uuid":"3493","customer_name":"Khagesh","customer_mobile_no":"9005799934","items":[{"amount":"100","product_uuid":"prod-ca6079b3386242c5aa860dbaae7c7ddc"}]}'
            json.put("seller_uuid", sellerUUID);
            json.put("customer_name", getEndUserName());
            json.put("customer_mobile_no", getEndUserMobileNo());
            json.put("customer_email", getEndUserEmail());
            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("amount", getEndUserAmountCommunicated());
            item.put("product_uuid", productUUID);
            items.put(item);
            json.put("items", items);
            Log.d("zzz PaymentAct json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"invoice/create", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz PaymentActivity", APIURL.url + "invoice/create" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONObject data = response.getJSONObject("data");
                                String invoice_id = data.getString("invoice_id");;
                                startPayment(invoice_id);
                            } else {
                                Toast.makeText(getApplicationContext(),"Oops! We encountered some error, please try letter.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Oops! We encountered some error, please try letter.",Toast.LENGTH_SHORT).show();
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

    public void startPayment(String invoice_id) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("invoice_id", invoice_id);
            options.put("ecod",true);
            options.put("name", "Recko");
            options.put("description", productModel.getTitle());
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", getEndUserAmountCommunicated());

            JSONObject preFill = new JSONObject();
            preFill.put("email", getEndUserEmail());
            preFill.put("contact", getEndUserMobileNo());
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private String getEndUserName() {return end_user_name.getText().toString().trim();}
    private String getEndUserMobileNo() {return end_user_mobile_no.getText().toString().trim();}
    private String getEndUserEmail() {return end_user_email.getText().toString().trim();}
    private String getEndUserAmountCommunicated() {return amount_communicated.getText().toString().trim();}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String contact_number = "", contact_email = "", contact_name = "";
            if (requestCode == Constants.pick_contact_request_id) {
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
                    end_user_mobile_no.setText(contact_number);
                    end_user_email.setText(contact_email);
                    end_user_name.setText(name);
                    //end_user_name.setFocusableInTouchMode(false);
                    end_user_name.clearFocus();
                    cursor.close();
                }
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error retrieving contact", Toast.LENGTH_LONG).show();
            end_user_name.setText("");
        }
    }

}