package com.example.neelanshsethi.sello.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.neelanshsethi.sello.CategoryAndCompany;
import com.example.neelanshsethi.sello.Model.IndustryCardModel;
import com.example.neelanshsethi.sello.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IndustrySmallCardAdapter extends RecyclerView.Adapter<IndustrySmallCardAdapter.ResultViewHolder> {

    Context mctx;
    private List cardindustrylist;
    private Activity mActivity;
    private IndustryCardModel industryCardModel;

    public static final int VIEW_TYPE_NORMAL = 1;


    public IndustrySmallCardAdapter( Context mctx, List cardindustrylist,Activity mActivity) {
        this.mctx = mctx;
        this.cardindustrylist=cardindustrylist;
        this.mActivity=mActivity;
    }


    @NonNull
    @Override
    public IndustrySmallCardAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mctx);
        View v = inflater.inflate(R.layout.industry_small_card, null);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IndustrySmallCardAdapter.ResultViewHolder holder, final int position) {

        if (!cardindustrylist.isEmpty()) {
            industryCardModel = (IndustryCardModel) cardindustrylist.get(position);
            Glide.with(mctx)
                    .load(industryCardModel.getImage_url())
                    .into(holder.imageView);
            holder.textView.setText(industryCardModel.getName());
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                IndustryCardModel temp = (IndustryCardModel) cardindustrylist.get(position);
                Intent intent= new Intent(mctx,CategoryAndCompany.class);
                intent.putExtra("industry_uuid",temp.getIndustry_uuid());
                intent.putExtra("industry_name",temp.getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cardindustrylist != null && cardindustrylist.size() > 0) {
            return cardindustrylist.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ResultViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_small_industry);
            textView=itemView.findViewById(R.id.title_small_industry);
        }
    }
}
