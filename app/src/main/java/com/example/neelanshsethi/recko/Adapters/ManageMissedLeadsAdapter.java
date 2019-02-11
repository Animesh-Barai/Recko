package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.neelanshsethi.recko.APIURL;
import com.example.neelanshsethi.recko.AddLeadCustomer;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.ManageLeadsModel;
import com.example.neelanshsethi.recko.R;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ManageMissedLeadsAdapter extends RecyclerView.Adapter<ManageMissedLeadsAdapter.LeadsViewHolder>
{
    private Context mctx;
    private List manage_missed_followupsList;
    Activity mActivity;
    public static final int VIEW_TYPE_NORMAL = 1;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Fragment fragment;

    public ManageMissedLeadsAdapter(Context mctx, List manage_missed_followupsList, Activity mActivity) {
        this.mctx = mctx;
        this.manage_missed_followupsList = manage_missed_followupsList;
        this.mActivity=mActivity;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
    }

    public ManageMissedLeadsAdapter(Context mctx, List manage_missed_followupsList, Activity mActivity, Fragment fragment) {
        this.mctx = mctx;
        this.manage_missed_followupsList = manage_missed_followupsList;
        this.mActivity=mActivity;
        this.fragment = fragment;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mctx);
    }

    @NonNull
    @Override
    public ManageMissedLeadsAdapter.LeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.missed_follow_ups_card, null);
        return new LeadsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ManageMissedLeadsAdapter.LeadsViewHolder holder, final int position) {

        if(manage_missed_followupsList!=null) {
            final ManageLeadsModel manageLeadsModel = (ManageLeadsModel) manage_missed_followupsList.get(position);

            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(mctx,AddLeadCustomer.class);
                    intent.putExtra("edit_lead_model", manageLeadsModel);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (fragment != null)
                        fragment.startActivityForResult(intent, Constants.edit_missed_lead_request_id);
                    else
                        ((Activity)mctx).startActivityForResult(intent, Constants.edit_missed_lead_request_id);
                }
            });
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(mctx);


                builder.setCancelable(false);
                builder.setTitle("Delete lead")
                        .setMessage("Are you sure you want to delete this lead?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            TextView textView;
                            public void onClick(DialogInterface dialog, int which) {
                                delete_lead(position);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mctx,"Cancelled",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void updateLead(ManageLeadsModel updated) {
        for (int i=0;i<manage_missed_followupsList.size();i++) {
            ManageLeadsModel tmp = (ManageLeadsModel) manage_missed_followupsList.get(i);
            if (tmp.getLead_uuid().equals(updated.getLead_uuid())) {
                manage_missed_followupsList.set(i, updated);
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("zzz missed lead adapter", "get called");
        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == Constants.edit_missed_lead_request_id) {
                Log.d("zzz missed lead adapter", "edit succeeded");
                ManageLeadsModel updated_lead = (ManageLeadsModel) data.getSerializableExtra("added_lead");
                updateLead(updated_lead);
                notifyDataSetChanged();
            }

        }
    }

    private void delete_lead(int position_arg, String auth_token) {
        final int position = position_arg;
        ManageLeadsModel manageLeadsModel = (ManageLeadsModel) manage_missed_followupsList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        bundle.putString("lead_uuid", manageLeadsModel.getLead_uuid());
        mFirebaseAnalytics.logEvent("lead_deleted", bundle);

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("firebase_token",auth_token);
            json.put("lead_uuid", manageLeadsModel.getLead_uuid());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"lead/delete", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "lead/delete" + "\nonResponse: " + response);

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                Log.d("zzz del_missed_lead", "Here1");
                                manage_missed_followupsList.remove(position);
                                Log.d("zzz del_missed_lead", "Here2");
                                notifyItemRemoved(position);
                                Log.d("zzz del_missed_lead", "Here3");
                                notifyItemRangeChanged(position,manage_missed_followupsList.size());
                                Log.d("zzz del_missed_lead", "Here4");
                                Toast.makeText(mctx,"Deleted",Toast.LENGTH_SHORT).show();
                                if(manage_missed_followupsList.isEmpty()) {
                                    TextView textView = mActivity.findViewById(R.id.heading_missed_followups);
                                    textView.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(mctx,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(mctx,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mctx);
        requestQueue.add(jsonObjectRequest);
    }

    private void delete_lead(int position) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        final int position_copy = position;
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            delete_lead(position_copy, idToken);
                        } else {
                            Toast.makeText(mctx,"Oops! Please check internet connectivity. Our authentication servers are not responding.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (manage_missed_followupsList != null && manage_missed_followupsList.size() > 0) {
            return manage_missed_followupsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    class LeadsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,description;
        ImageView edit,delete;

        public LeadsViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}