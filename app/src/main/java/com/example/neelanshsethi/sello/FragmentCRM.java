package com.example.neelanshsethi.sello;

import android.app.Activity;
import android.content.Context;
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

import com.example.neelanshsethi.sello.Adapters.ManageMissedLeadsAdapter;
import com.example.neelanshsethi.sello.Adapters.VideoListAdapter;
import com.example.neelanshsethi.sello.Model.ManageLeadsModel;

import java.util.ArrayList;
import java.util.List;

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
    private ManageMissedLeadsAdapter manageMissedLeadsAdapter;
    private RecyclerView rv_missed_followups;
    private RecyclerView rv_active_leads;
    private Activity thisActivity;

    TextView heading_missed_followups;
    ConstraintLayout cv;

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
        miised_follow_ups_list = new ArrayList();
        rv_missed_followups.setHasFixedSize(true);
        rv_missed_followups.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        manageMissedLeadsAdapter=new ManageMissedLeadsAdapter(getActivity(),miised_follow_ups_list,thisActivity);
        rv_missed_followups.setAdapter(manageMissedLeadsAdapter);
        get_followups();
        if(!miised_follow_ups_list.isEmpty())
            cv.setVisibility(View.GONE);

        addlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddLeadCustomer.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,7);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK)
        {
            cv.setVisibility(View.GONE);
            get_followups();
            Log.d("zzz activity result","Zzzzzzzzzzzzz in fragment");
            Toast.makeText(getContext(),"Zzzzzzzzzzzzzzzz",Toast.LENGTH_SHORT).show();

        }
    }

    private void get_followups() {
        ManageLeadsModel manageLeadsModel = new ManageLeadsModel("haha","haha","haha","haha","haha","haha","haha","haha","haha","haha");
        miised_follow_ups_list.clear();
        miised_follow_ups_list.add(manageLeadsModel);
        miised_follow_ups_list.add(manageLeadsModel);


        if(miised_follow_ups_list.isEmpty())
            heading_missed_followups.setVisibility(View.GONE);

        manageMissedLeadsAdapter.notifyDataSetChanged();
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
