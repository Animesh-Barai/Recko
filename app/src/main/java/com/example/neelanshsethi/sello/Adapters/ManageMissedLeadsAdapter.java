package com.example.neelanshsethi.sello.Adapters;

import android.content.Context;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.neelanshsethi.sello.Model.ManageLeadsModel;
import com.example.neelanshsethi.sello.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ManageMissedLeadsAdapter extends RecyclerView.Adapter<ManageMissedLeadsAdapter.LeadsViewHolder>
{
    private Context mctx;
    private List manageleadsList;


    public ManageMissedLeadsAdapter(Context mctx, List manageleadsList, Display disp) {
        this.mctx = mctx;
        this.manageleadsList = manageleadsList;

    }

    @NonNull
    @Override
    public ManageMissedLeadsAdapter.LeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        View view;
        switch (viewType)
        {
            case 0:inflater=LayoutInflater.from(mctx);
                view=inflater.inflate(R.layout.crm_rv_manageleads_dates,parent,false);
                return new ManageMissedLeadsAdapter.LeadsViewHolder(view,viewType);
            default:inflater=LayoutInflater.from(mctx);
                view=inflater.inflate(R.layout.missed_follow_ups_card,parent,false);
                return new ManageMissedLeadsAdapter.LeadsViewHolder(view,viewType);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ManageMissedLeadsAdapter.LeadsViewHolder holder, int position) {
        ManageLeadsModel manageLeadsModel = (ManageLeadsModel) manageleadsList.get(position);


        if(position==0)
        {
            holder.missedFollowUpHeading.setText("Missed Follow-ups");
        }
        else
        {
            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());
        }
    }

    @Override
    public int getItemCount() {
        return manageleadsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType=1;
        if(position==0)
        {
            viewType=0;
        }
        return viewType;
    }

    class LeadsViewHolder extends RecyclerView.ViewHolder
    {

        TextView missedFollowUpHeading;
        TextView name,description;

        public LeadsViewHolder(View itemView,int viewType) {
            super(itemView);

            if(viewType==0)
            {
                missedFollowUpHeading=itemView.findViewById(R.id.crm_manage_leads_date);
            }
            else
            {
                name=itemView.findViewById(R.id.name);
                description=itemView.findViewById(R.id.description);
            }
        }
    }
}