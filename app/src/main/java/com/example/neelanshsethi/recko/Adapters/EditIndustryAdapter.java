package com.example.neelanshsethi.recko.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.example.neelanshsethi.recko.Industries;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Model.IndustryChipModel;
import com.example.neelanshsethi.recko.R;
import com.google.android.material.chip.Chip;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditIndustryAdapter extends BaseAdapter {


    Context mctx;
    View v;
    LayoutInflater layoutInflater;
    List industryModellist;
    Map<Integer, Chip> chipIndexMap;

    public EditIndustryAdapter(Context mctx, List industryModellist) {
        this.mctx = mctx;
        this.industryModellist=industryModellist;
        chipIndexMap = new HashMap<>();
    }


    @Override
    public int getCount() {
        return  industryModellist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setAllSelectedChips() {
        for (int ii=0;ii<industryModellist.size();ii++) {
            IndustryChipModel industryChipModel = (IndustryChipModel) industryModellist.get(ii);
            if (Constants.getInstance().isUserSubscribedToIndustry(industryChipModel.getIndustry_uuid()))
            {

                Chip chip = chipIndexMap.get(ii);
                chip.performClick();
                setSelection(chip);
                industryChipModel.setSelected(chip.isChecked());
            }
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        Log.d("milatype",position%2+"");
//        return super.getItemViewType(position%2);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        Log.d("milacount",super.getViewTypeCount()+"");
//        return 1;
//    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view==null)
        {
            v=new View(mctx);
            v=layoutInflater.inflate(R.layout.industry_card,viewGroup,false);
        }
        else {
            v = (View) view;
        }
        final Chip chip = v.findViewById(R.id.chip);
        chipIndexMap.put(i, chip);
//        Drawable drawable= mctx.getResources().getDrawable(industry_logo[i]);

        final IndustryChipModel industryChipModel = (IndustryChipModel) industryModellist.get(i);
        chip.setChipIcon(industryChipModel.getIndustry_logo());
        chip.setText(industryChipModel.getIndustry_name());

        final Industries industries=new Industries();
        chip.setChecked(industryChipModel.getSelected());
        setSelection(chip);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(chip);
                industryChipModel.setSelected(chip.isChecked());
            }
        });
        return v;
    }

    private void setSelection(Chip chip){
        if(chip.isChecked()) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ff0070")));
            chip.setTextColor(Color.WHITE);
            chip.setCheckedIconVisible(false);

        }
        else{
            chip.setChipBackgroundColor((ColorStateList.valueOf(Color.parseColor("#EBEBEB"))));
            chip.setTextColor(Color.parseColor("#484848"));
            chip.setChipIconVisible(true);
            chip.setCheckedIconVisible(false);
        }
    }

    public JSONArray getSelectedJsonArray() {
        JSONArray data = new JSONArray();
        for (Object chip : industryModellist) {
            IndustryChipModel industry = (IndustryChipModel) chip;
            if (industry.getSelected()) data.put(industry.getIndustry_uuid());
        }
        return data;
    }
}
