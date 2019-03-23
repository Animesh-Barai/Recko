package com.recko.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.recko.app.Adapters.Company_InCategoryAndCompanyAdapter;
import com.recko.app.Model.Company_InCategoryAndCompanyModel;
import com.recko.app.Model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCompany.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCompany#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCompany extends androidx.fragment.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentCategory.OnFragmentInteractionListener mListener;

    private RecyclerView rv_companylist;
    private Company_InCategoryAndCompanyAdapter companyInCategoryAndCompanyAdapter;

    private Activity thisActivity;
    List companylist;


    public FragmentCompany() {
        // Required empty public constructor
    }

    public static FragmentCategory newInstance(String param1, String param2) {
        FragmentCategory fragment = new FragmentCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_company, container, false);
        rv_companylist=v.findViewById(R.id.rv_companylist);
        thisActivity=(Activity)getActivity();
        companylist = new ArrayList();

        rv_companylist.setHasFixedSize(true);
        rv_companylist.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        companyInCategoryAndCompanyAdapter =new Company_InCategoryAndCompanyAdapter(getActivity(), companylist,thisActivity);
        rv_companylist.setAdapter(companyInCategoryAndCompanyAdapter);
        get_rv_companylist();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void get_rv_companylist(){
        JSONObject json = new JSONObject();
        try {
            json.put("industry_uuid",getActivity().getIntent().getStringExtra("industry_uuid"));
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"product/list_grouped_by_industry", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"product/list_grouped_by_industry"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    JSONObject company_detail = object.getJSONObject("company");
                                    Log.d("zzzobjct companydetails",company_detail.toString());

                                    Company_InCategoryAndCompanyModel company_inCategoryAndCompanyModel;

                                    String max_commission = company_detail.getString("max_commission");
                                    String company_uuid = company_detail.getString("company_uuid");
                                    String image_url = company_detail.getString("image_url");
                                    String company_name = company_detail.getString("company_name");
                                    String no_of_products = company_detail.getString("no_of_products");

                                    JSONArray products = object.getJSONArray("products");
                                    Log.d("zzzarray products",products.toString());
                                    
                                    List<ProductModel> temp = new ArrayList<ProductModel>();
                                    for(int j = 0; j < products.length(); j++)
                                    {
                                        JSONObject productDetails = products.getJSONObject(j);
                                        Log.d("zzz productdetails",productDetails.toString());

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

                                        ProductModel productModel = new ProductModel(broucher,video,to_date,good_time_to_sell,category,title,upfront_commission,from_date,location_of_sell,target_customer,type,price_on_x,companyy_uuid,total_commission,tips_to_sell,customer_data_needed,product_details,payment_type,product_uuid,  mrp);
                                        temp.add(productModel);

                                    }
                                    company_inCategoryAndCompanyModel = new Company_InCategoryAndCompanyModel(max_commission,company_uuid,image_url,company_name,no_of_products,temp);
                                    companylist.add(company_inCategoryAndCompanyModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            companyInCategoryAndCompanyAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } 
                    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }
}