package com.example.neelanshsethi.recko.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neelanshsethi.recko.AddLeadCustomer;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.ManageLeadsModel;
import com.example.neelanshsethi.recko.R;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;



public class ActiveLeadsAdapter extends RecyclerView.Adapter<ActiveLeadsAdapter.LeadsViewHolder>
{
    private Context mctx;
    private List activeleadsList;
    Activity mActivity;
    public static final int VIEW_TYPE_NORMAL = 1;
    private Fragment fragment;

    public ActiveLeadsAdapter(Context mctx, List activeleadsList, Activity mActivity) {
        this.mctx = mctx;
        this.activeleadsList = activeleadsList;
        this.mActivity=mActivity;
    }

    public ActiveLeadsAdapter(Context mctx, List activeleadsList, Activity mActivity, Fragment fragment) {
        this.mctx = mctx;
        this.activeleadsList = activeleadsList;
        this.mActivity=mActivity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ActiveLeadsAdapter.LeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.active_leads_card, null);
        return new LeadsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ActiveLeadsAdapter.LeadsViewHolder holder, int position) {

        if(activeleadsList!=null) {
            final ManageLeadsModel manageLeadsModel = (ManageLeadsModel) activeleadsList.get(position);

            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(mctx,AddLeadCustomer.class);
                    intent.putExtra("edit_lead_model", manageLeadsModel);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (fragment != null)
                        fragment.startActivityForResult(intent, Constants.edit_active_lead_request_id);
                    else
                        ((Activity)mctx).startActivityForResult(intent, Constants.edit_active_lead_request_id);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (activeleadsList != null && activeleadsList.size() > 0) {
            return activeleadsList.size();
        } else {
            return 0;
        }
    }

    private void updateLead(ManageLeadsModel updated) {
        for (int i=0;i<activeleadsList.size();i++) {
            ManageLeadsModel tmp = (ManageLeadsModel) activeleadsList.get(i);
            if (tmp.getLead_uuid().equals(updated.getLead_uuid())) {
                activeleadsList.set(i, updated);
                break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("zzz active lead adapter", "get called");
        if(resultCode==Activity.RESULT_OK)
        {
            if (requestCode == Constants.edit_active_lead_request_id) {
                Log.d("zzz active lead adapter", "edit succeeded");
                ManageLeadsModel updated_lead = (ManageLeadsModel) data.getSerializableExtra("added_lead");
                updateLead(updated_lead);
                notifyDataSetChanged();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    class LeadsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,description;
        Chip call,whatsapp;
        ImageView edit;

        public LeadsViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            call = itemView.findViewById(R.id.chip_call);
            whatsapp = itemView.findViewById(R.id.chip_whatsapp);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
