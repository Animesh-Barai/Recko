package com.example.neelanshsethi.sello;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentExplore.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentExplore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentExplore extends androidx.fragment.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_videolist;
    private RecyclerView rv_categorylist1;
    private RecyclerView rv_categorylist2;
    private RecyclerView rv_small_industry1;
    private RecyclerView rv_small_industry2;
    private ViewPager carousel;
    private SliderAdapter sliderAdapter;
    private ImageView[] dots;
    private LinearLayout Dots;
    private DotsIndicator dotsIndicator;
    private Activity thisActivity;
    private CategoryListAdapter categoryListAdapter1;
    private CategoryListAdapter categoryListAdapter2;
    private IndustrySmallCardAdapter industrySmallCardAdapter1;
    private IndustrySmallCardAdapter industrySmallCardAdapter2;

    //    String[] list={"nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0"};
    private List<String> rv_videos= new ArrayList<String>();
    private List<String> rv_videos_title= new ArrayList<String>();
    int[] rv_thumbnails={R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample};

    List<String> sampleimgurl= new ArrayList<String>();

    private List<String> heading= new ArrayList<String>();
    private List<String> heading2= new ArrayList<String>();

    private List<List<String>> imageurl= new ArrayList<List<String>>();
    private List<List<String>> categorytitle= new ArrayList<List<String>>();
    private List<List<String>> categoryamount= new ArrayList<List<String>>();

    List carousel_images;




    public FragmentExplore() {
        // Required empty public constructor
    }

    public static FragmentExplore newInstance(String param1, String param2) {
        FragmentExplore fragment = new FragmentExplore();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_explore, container, false);

        thisActivity=(Activity)getActivity();
        rv_videolist=v.findViewById(R.id.rv_videolist);
        rv_categorylist1=v.findViewById(R.id.rv_categorylist1);
        rv_categorylist2=v.findViewById(R.id.rv_categorylist2);
        rv_small_industry1=v.findViewById(R.id.rv_small_industry1);
        rv_small_industry2=v.findViewById(R.id.rv_small_industry2);
        RelativeLayout layout = v.findViewById(R.id.top_layout);
        carousel_images=new ArrayList();


        dotsIndicator = (DotsIndicator) v.findViewById(R.id.dots_indicator);
        carousel=v.findViewById(R.id.carousel);
        carousel.setPageTransformer(true, new DepthPageTransformer());
//        Dots=v.findViewById(R.id.Dots);

        sliderAdapter=new SliderAdapter(getActivity(),carousel_images);
        carousel.setAdapter(sliderAdapter);
        dotsIndicator.setViewPager(carousel);

        sampleimgurl.add("https://www.desktopbackground.org/download/2000x1500/2010/04/10/29_ultra-hd-4k-rain-wallpapers-hd-desktop-backgrounds-3840x2400_3840x2400_h.jpg");
        sampleimgurl.add("https://www.oneperiodic.com/products/photobatch/tutorials/img/scale_original.png");
        sampleimgurl.add("https://a-static.besthdwallpaper.com/above-the-clouds-sunset-wallpaper-2048x1536-4355_26.jpg");
        sampleimgurl.add("http://s4301.pcdn.co/wp-content/uploads/2012/11/20130312_10thExterior_night-desktop4x3.jpg");

        heading.add("Popular Products for you");
        heading.add("Kamao BC Kamao");
        heading.add("Kamao BC Kamao");
        heading.add("Kamao BC Kamao");
        heading2.add("Education");
        heading2.add("Mediacal");
        heading2.add("Finance");
        heading2.add("Finance");
        List<String > temp1= new ArrayList<String>();
        temp1.add("https://cdn.tutsplus.com/photo/uploads/legacy/746_aspectratio/07.jpg");
        temp1.add("https://g2e-gamers2mediasl.netdna-ssl.com/wp-content/uploads/2016/03/G2-Esports-3D-Grey-Logo-1200x600.jpg");
        temp1.add("https://iacopodeenosee.files.wordpress.com/2013/06/abstract-circles-l.jpg");
        temp1.add("https://g2e-gamers2mediasl.netdna-ssl.com/wp-content/uploads/2017/08/weareG2esports-1200x600.jpg");
        imageurl.add(temp1);
        imageurl.add(temp1);
        List<String > temp2= new ArrayList<String>();
        temp2.add("sub heading 1");
        temp2.add("sub heading 2");
        temp2.add("sub heading 3");
        temp2.add("sub heading 4");
        categorytitle.add(temp2);
        categorytitle.add(temp2);
        List<String > temp3= new ArrayList<String>();
        temp3.add("10000");
        temp3.add("20000");
        temp3.add("30000");
        temp3.add("40000");
        categoryamount.add(temp3);
        categoryamount.add(temp3);

        rv_categorylist1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rv_categorylist1.setLayoutManager(linearLayoutManager);
//        categoryListAdapter1 =new CategoryListAdapter(getActivity(),heading,imageurl,categorytitle,categoryamount);
//        rv_categorylist1.setAdapter(categoryListAdapter1);
//        categoryListAdapter1.notifyDataSetChanged();

        rv_small_industry1.setHasFixedSize(true);
        rv_small_industry1.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        industrySmallCardAdapter1=new IndustrySmallCardAdapter(getActivity(),heading2,sampleimgurl);
        rv_small_industry1.setAdapter(industrySmallCardAdapter1);
        industrySmallCardAdapter1.notifyDataSetChanged();

        rv_categorylist2.setHasFixedSize(true);
        rv_categorylist2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
//        categoryListAdapter2 =new CategoryListAdapter(getActivity(),heading,imageurl,categorytitle,categoryamount);
        rv_categorylist2.setAdapter(categoryListAdapter1);
        categoryListAdapter2.notifyDataSetChanged();

        rv_small_industry2.setHasFixedSize(true);
        rv_small_industry2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        industrySmallCardAdapter2=new IndustrySmallCardAdapter(getActivity(),heading2,sampleimgurl);
        rv_small_industry2.setAdapter(industrySmallCardAdapter2);
        industrySmallCardAdapter2.notifyDataSetChanged();

        layout.requestFocus();


























        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
