package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(@NonNull final ManageMissedLeadsAdapter.LeadsViewHolder holder, final int position) {

        if(manage_missed_followupsList!=null) {
            ManageLeadsModel manageLeadsModel = (ManageLeadsModel) manage_missed_followupsList.get(position);

            holder.name.setText(manageLeadsModel.getContact_name());
            holder.description.setText(manageLeadsModel.getProduct_name());
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
                                manage_missed_followupsList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,manage_missed_followupsList.size());
                                Toast.makeText(mctx,"Deleted",Toast.LENGTH_SHORT).show();
                                if(manage_missed_followupsList.isEmpty()) {
                                    textView = mActivity.findViewById(R.id.heading_missed_followups);
                                    textView.setVisibility(View.GONE);
                                }
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