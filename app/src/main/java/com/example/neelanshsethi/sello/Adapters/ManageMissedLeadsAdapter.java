package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;

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
    private List manage_missed_followupsList;
    Activity mActivity;
    public static final int VIEW_TYPE_NORMAL = 1;


    public ManageMissedLeadsAdapter(Context mctx, List manage_missed_followupsList, Activity mActivity) {
        this.mctx = mctx;
        this.manage_missed_followupsList = manage_missed_followupsList;
        this.mActivity=mActivity;

    }

    @NonNull
    @Override
    public ManageMissedLeadsAdapter.LeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.missed_follow_ups_card, null);
        return new LeadsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ManageMissedLeadsAdapter.LeadsViewHolder holder, int position) {

        if(manage_missed_followupsList!=null) {
            ManageLeadsModel manageLeadsModel = (ManageLeadsModel) manage_missed_followupsList.get(position);

            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());

        }
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

        public LeadsViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);

        }
    }
}