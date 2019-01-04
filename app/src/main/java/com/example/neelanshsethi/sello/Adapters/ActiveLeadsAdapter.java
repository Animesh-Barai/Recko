package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neelanshsethi.sello.Model.ManageLeadsModel;
import com.example.neelanshsethi.sello.R;
import com.google.android.material.chip.Chip;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ActiveLeadsAdapter extends RecyclerView.Adapter<ActiveLeadsAdapter.LeadsViewHolder>
{
    private Context mctx;
    private List activeleadsList;
    Activity mActivity;
    public static final int VIEW_TYPE_NORMAL = 1;


    public ActiveLeadsAdapter(Context mctx, List activeleadsList, Activity mActivity) {
        this.mctx = mctx;
        this.activeleadsList = activeleadsList;
        this.mActivity=mActivity;

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
            ManageLeadsModel manageLeadsModel = (ManageLeadsModel) activeleadsList.get(position);

            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());

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
