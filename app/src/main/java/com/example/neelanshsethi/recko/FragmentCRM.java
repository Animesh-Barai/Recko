package com.example.neelanshsethi.recko;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.Adapters.ActiveLeadsAdapter;
import com.example.neelanshsethi.recko.Adapters.ManageMissedLeadsAdapter;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.IndustryCardModel;
import com.example.neelanshsethi.recko.Model.ManageLeadsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCRM.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCRM#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCRM extends androidx.fragment.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button addlead;

    private List miised_follow_ups_list;
    private List active_leads_list;
    private ManageMissedLeadsAdapter manageMissedLeadsAdapter;
    private ActiveLeadsAdapter activeLeadsAdapter;
    private RecyclerView rv_missed_followups;
    private RecyclerView rv_active_leads;
    private Activity thisActivity;

    TextView heading_missed_followups;
    TextView heading_upcoming_followups;
    ConstraintLayout cv;

    FloatingActionButton floatingActionButton;
    private FirebaseAnalytics mFirebaseAnalytics;

    public FragmentCRM() {
        // Required empty public constructor
    }

    public static FragmentCRM newInstance(String param1, String param2) {
        FragmentCRM fragment = new FragmentCRM();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_crm, container, false);
        addlead = v.findViewById(R.id.addFirstLead);
        rv_missed_followups=v.findViewById(R.id.rv_missedfollowups);
        rv_active_leads=v.findViewById(R.id.rv_active_leads);
        thisActivity=(Activity)getActivity();
        cv = v.findViewById(R.id.cv);
        heading_missed_followups = v.findViewById(R.id.heading_missed_followups);
        heading_upcoming_followups = v.findViewById(R.id.heading_active_leads);
        miised_follow_ups_list = new ArrayList();
        active_leads_list = new ArrayList();

        floatingActionButton = v.findViewById(R.id.floatingActionButton);

//        rv_missed_followups.setHasFixedSize(true);
        rv_missed_followups.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        manageMissedLeadsAdapter=new ManageMissedLeadsAdapter(getActivity(),miised_follow_ups_list,thisActivity);
        rv_missed_followups.setAdapter(manageMissedLeadsAdapter);

//        rv_active_leads.setHasFixedSize(true);
        rv_active_leads.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        activeLeadsAdapter=new ActiveLeadsAdapter(getActivity(),active_leads_list,thisActivity,this);
        rv_active_leads.setAdapter(activeLeadsAdapter);

        get_followups();
        if(!miised_follow_ups_list.isEmpty() || !active_leads_list.isEmpty())
            cv.setVisibility(View.GONE);

        addlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddLeadCustomer.class);

//                this statement caused error
//                activity gives result before starting
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,Constants.add_lead_request_id);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),AddLeadCustomer.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, Constants.add_lead_request_id);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("zzz activity result", "get called");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.edit_active_lead_request_id) {
            activeLeadsAdapter.onActivityResult(requestCode, resultCode, data);
        }

        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == Constants.add_lead_request_id) {
                cv.setVisibility(View.GONE);
                ManageLeadsModel lead = (ManageLeadsModel) data.getSerializableExtra("added_lead");
                active_leads_list.add(lead);
                activeLeadsAdapter.notifyDataSetChanged();
                //get_followups();
                Log.d("zzz activity result", "Zzzzzzzzzzzzz in fragment");
                //Toast.makeText(getContext(),"Zzzzzzzzzzzzzzzz",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void get_followups(String auth_token) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        mFirebaseAnalytics.logEvent("fetch_followups_called", bundle);

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("firebase_token",auth_token);
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"lead/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "lead/list" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray missed = data.getJSONArray("missed");
                                JSONArray active = data.getJSONArray("active");
                                miised_follow_ups_list.clear();
                                active_leads_list.clear();

                                for (int i = 0; i < missed.length(); i++) {
                                    miised_follow_ups_list.add(new ManageLeadsModel(missed.getJSONObject(i)));
                                }

                                for (int i = 0; i < active.length(); i++) {
                                    active_leads_list.add(new ManageLeadsModel(active.getJSONObject(i)));
                                }

                                if (miised_follow_ups_list.isEmpty())
                                    heading_missed_followups.setVisibility(View.GONE);
                                if (active_leads_list.isEmpty())
                                    heading_upcoming_followups.setVisibility(View.GONE);

                                if (active_leads_list.isEmpty() && miised_follow_ups_list.isEmpty())
                                    cv.setVisibility(View.VISIBLE);

                                manageMissedLeadsAdapter.notifyDataSetChanged();
                                activeLeadsAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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

    private void get_followups() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            get_followups(idToken);
                        } else {
                            Toast.makeText(getContext(),"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

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
}
