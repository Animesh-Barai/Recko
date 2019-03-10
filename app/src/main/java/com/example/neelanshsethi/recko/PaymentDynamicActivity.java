package com.example.neelanshsethi.recko;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.IndustryCardModel;
import com.example.neelanshsethi.recko.Model.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementTextEmail;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextPhone;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class PaymentDynamicActivity  extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    FormBuilder mFormBuilder;
    ProductModel productModel;
    AtomicBoolean fields_fetched, fields_fetching;
    ProgressBar progressBar;
    Button send;
    int noOfFields;
    Switch behalf_switch;
    androidx.appcompat.widget.Toolbar toolbar;

    Map<String, Integer> tagToIndexMap;

    private String productUUID, sellerUUID;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_dynamic);
        productModel = (ProductModel) getIntent().getSerializableExtra("product_model");
        productUUID = productModel.getProduct_uuid();
        sellerUUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tagToIndexMap = new HashMap<String, Integer>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, mRecyclerView);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        fields_fetched = new AtomicBoolean();
        fields_fetched.set(false);
        fields_fetching = new AtomicBoolean();
        fields_fetching.set(false);

        send = findViewById(R.id.send);

        behalf_switch = findViewById(R.id.switch_one);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fields_fetched.get() == false) {
                    fetch_fields();
                    return;
                }

                for (int ii=0;ii<noOfFields;++ii) {
                    BaseFormElement baseFormElement = mFormBuilder.getFormElement(ii);
                    if (baseFormElement.isRequired() && TextUtils.isEmpty(baseFormElement.getValue())) {
                        Toast.makeText(getApplicationContext(), "Please " + baseFormElement.getHint(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (baseFormElement.getType() == BaseFormElement.TYPE_EDITTEXT_EMAIL) {
                        if (!TextUtils.isEmpty(baseFormElement.getValue()) &&
                                !Constants.validateEmail(baseFormElement.getValue())) {
                            Toast.makeText(getApplicationContext(), "Please provide valid " + baseFormElement.getTitle(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (baseFormElement.getType() == BaseFormElement.TYPE_EDITTEXT_PHONE) {
                        if (!TextUtils.isEmpty(baseFormElement.getValue()) &&
                                !Constants.validIndianNumber(baseFormElement.getValue())) {
                            Toast.makeText(getApplicationContext(), "Please provide valid " + baseFormElement.getTitle(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                createInvoice();
            }
            });

        //mFormBuilder.refreshView();
        fetch_fields();


    }

    public void createInvoice(String auth_token) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject json = new JSONObject();
        try {
            // '{"seller_uuid":"3493","customer_name":"Khagesh","customer_mobile_no":"9005799934","items":[{"amount":"100","product_uuid":"prod-ca6079b3386242c5aa860dbaae7c7ddc"}]}'
            json.put("seller_uuid", sellerUUID);
            json.put("firebase_token", auth_token);
            JSONObject user_data = new JSONObject();
            for (int ii=0;ii<noOfFields;++ii) {
                BaseFormElement baseFormElement = mFormBuilder.getFormElement(ii);
                user_data.put(getTagAtInd(ii), baseFormElement.getValue());
            }
            json.put("user_data", user_data);
            JSONArray items = new JSONArray();
            JSONObject item = new JSONObject();
            item.put("amount", getEndUserAmountCommunicated());
            item.put("product_uuid", productUUID);
            items.put(item);
            json.put("items", items);
            json.put("payed_by_seller", behalf_switch.isChecked());
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
                                String invoice_id = data.getString("invoice_id");
                                progressBar.setVisibility(View.GONE);
                                startPayment(invoice_id);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Oops! We encountered some error, please try letter.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Oops! We encountered some error, please try letter.",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void createInvoice() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            createInvoice(idToken);
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private String getEndUserAmountCommunicated() {
        return getValueAtTag("amount");
    }

    private String getEndUserEmail() {
        return getValueAtTag("email");
    }
    private String getEndUserMobileNo() {
        return getValueAtTag("mobile_number");
    }

    public void fetch_fields() {
        if (fields_fetched.get() == true) return;
        if (!fields_fetching.compareAndSet(false, true)) return;

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("product_uuid",productModel.getProduct_uuid());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"fields/fetch", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "fields/fetch" + "\nonResponse: " + response);
                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            fields_fetched.set(true);
                            JSONObject data = response.getJSONObject("data");
                            JSONArray fields = data.getJSONArray("fields");
                            populateFields(fields);
                            fetch_fields_done();
                        } catch (JSONException e) {
                            fetch_fields_done();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fetch_fields_done();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void fetch_fields_done() {
        progressBar.setVisibility(View.GONE);
        fields_fetching.set(false);
    }

    private void populateFields(JSONArray fields) throws JSONException {
        // mobile, single_text, multi_text, email, number
        // title, hint, type, required
        List<BaseFormElement> formItems = new ArrayList<>();
        noOfFields = fields.length();
        for (int ii=0;ii<fields.length();++ii) {
            JSONObject field = fields.getJSONObject(ii);
            String title = field.getString("title");
            String hint = field.getString("hint");
            String type = field.getString("type");
            String tag = field.getString("tag");
            tagToIndexMap.put(tag, ii);
            boolean required = field.getBoolean("required");
            if (type.equals("email")) {
                formItems.add(FormElementTextEmail.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            } else if (type.equals("single_text")) {
                formItems.add(FormElementTextSingleLine.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            } else if (type.equals("multi_text")) {
                formItems.add(FormElementTextMultiLine.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            } else if (type.equals("email")) {
                formItems.add(FormElementTextEmail.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            } else if (type.equals("number")) {
                formItems.add(FormElementTextNumber.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            } else if (type.equals("mobile")) {
                formItems.add(FormElementTextPhone.createInstance().setTitle(title).setHint(hint).setTag(ii).setRequired(required));
            }
            mFormBuilder.addFormElements(formItems);
        }

    }

    public String getValueAtTag(String tag) {
        Integer ind = tagToIndexMap.get(tag);
        BaseFormElement baseFormElement = mFormBuilder.getFormElement(ind);
        return baseFormElement.getValue();
    }

    public String getTagAtInd(int ind) {
        for (Map.Entry<String, Integer> entry : tagToIndexMap.entrySet()) {
            if (ind ==  entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }
}
