package com.example.neelanshsethi.sello;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.chip.Chip;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;

import java.util.List;

public class IndustryAdapter extends BaseAdapter {


    Context mctx;
    private final List<String> industry_names;
    private final Drawable [] industry_logo;
    View v;
    LayoutInflater layoutInflater;

    int toggle=0;

    public IndustryAdapter(Context mctx, List<String> industry_names, Drawable[] industry_logo) {
        this.mctx = mctx;
        this.industry_names = industry_names;
        this.industry_logo = industry_logo;
    }

    @Override
    public int getCount() {
        return  industry_names.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("milatype",position%2+"");
        return super.getItemViewType(position%2);
    }

    @Override
    public int getViewTypeCount() {
        Log.d("milacount",super.getViewTypeCount()+"");
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
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
        chip.setChipIcon(industry_logo[i]);
        chip.setText(industry_names.get(i));
        final Industries industries=new Industries();

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chip.isChecked()) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#ff0070")));
                    chip.setTextColor(Color.WHITE);
                    chip.setCheckedIconVisible(false);
                    industries.selectChips(chip);

                }
                else{
                    chip.setChipBackgroundColor((ColorStateList.valueOf(Color.parseColor("#EBEBEB"))));
                    chip.setTextColor(Color.parseColor("#484848"));
                    chip.setChipIconVisible(true);
                    chip.setCheckedIconVisible(false);
                    industries.removeChips(chip);
                }
            }
        });
        return v;
    }
}
