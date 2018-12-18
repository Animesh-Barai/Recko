package com.example.neelanshsethi.sello;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    View v;
    LayoutInflater layoutInflater;

    private Context mctx;
    private List<String> imageurl;
    private List<String> categorytitle;
    private List<String> getCategoryamount;

    public CategoryAdapter(Context mctx, List<String> imageurl, List<String> categorytitle, List<String> getCategoryamount) {
        this.mctx = mctx;
        this.imageurl = imageurl;
        this.categorytitle = categorytitle;
        this.getCategoryamount = getCategoryamount;
    }


    @Override
    public int getCount() {
        return imageurl.size();
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
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater= (LayoutInflater) mctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view==null)
        {
            v=new View(mctx);
            v=layoutInflater.inflate(R.layout.category_card,viewGroup,false);
        }
        else {
            v = (View) view;
        }

        TextView titlecategory= v.findViewById(R.id.titlecategory);
        ImageView imgcategory=v.findViewById(R.id.imgcategory);
        TextView amountcategory=v.findViewById(R.id.amountcategory);

        Glide.with(mctx)
                .load(imageurl.get(i))
                .into(imgcategory);
        titlecategory.setText(categorytitle.get(i));
        amountcategory.setText(getCategoryamount.get(i));
        return v;
    }


}
