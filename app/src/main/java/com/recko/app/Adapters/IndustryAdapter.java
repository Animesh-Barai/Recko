package com.recko.app.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.recko.app.Industries;
import com.recko.app.Model.IndustryChipModel;
import com.recko.app.R;
import com.google.android.material.chip.Chip;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class IndustryAdapter extends BaseAdapter {


    Context mctx;
//    private final String industry_names;
//    private final Drawable industry_logo;
//    private final List<Boolean> isSelected;
    View v;
    LayoutInflater layoutInflater;
    List industryModellist;

    // This will be true if we are showing industries in user profile so we dont need to set on click listner.
    boolean is_user_industries;

    public IndustryAdapter(Context mctx, List industryModellist) {
        this.mctx = mctx;
        this.industryModellist=industryModellist;
        this.is_user_industries = false;
    }

    public IndustryAdapter(Context mctx, List industryModellist, boolean is_user_industries) {
        this.mctx = mctx;
        this.industryModellist=industryModellist;
        this.is_user_industries = is_user_industries;
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
//        Drawable drawable= mctx.getResources().getDrawable(industry_logo[i]);

        final IndustryChipModel industryChipModel = (IndustryChipModel) industryModellist.get(i);
        chip.setChipIcon(industryChipModel.getIndustry_logo());
        chip.setText(industryChipModel.getIndustry_name());
        if (is_user_industries) {
            chip.setChecked(false);
            setSelection(chip);
            return v;
        }

        final Industries industries=new Industries();
        chip.setChecked(industryChipModel.getSelected());
        setSelection(chip);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelection(chip);
                industryChipModel.setSelected(chip.isChecked());
                if(chip.isChecked()) {
                    industries.selectChips(chip);
                }
                else{
                    industries.removeChips(chip);
                }
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
}
