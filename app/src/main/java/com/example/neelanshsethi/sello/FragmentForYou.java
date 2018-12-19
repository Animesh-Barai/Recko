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
 * {@link FragmentForYou.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentForYou#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentForYou extends androidx.fragment.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_videolist;
    private RecyclerView rv_categorylist;
    private RecyclerView rv_small_industry;
    private ViewPager carousel;
    private SliderAdapter sliderAdapter;
    private ImageView[] dots;
    private LinearLayout Dots;
    private DotsIndicator dotsIndicator;
    private VideoListAdapter videoListAdapter;
    private Activity thisActivity;
    private CategoryListAdapter categoryListAdapter;
    private IndustrySmallCardAdapter industrySmallCardAdapter;

    //    String[] list={"nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0"};
    private List<String> rv_videos= new ArrayList<String>();
    private List<String> rv_videos_title= new ArrayList<String>();
    int carousel_images[]={R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample};
    int[] rv_thumbnails={R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample};

    List<String> sampleimgurl= new ArrayList<String>();

    private List<String> heading= new ArrayList<String>();
    private List<String> heading2= new ArrayList<String>();

    private List<List<String>> imageurl= new ArrayList<List<String>>();
    private List<List<String>> categorytitle= new ArrayList<List<String>>();
    private List<List<String>> categoryamount= new ArrayList<List<String>>();

    public FragmentForYou() {
        // Required empty public constructor
    }

    public static FragmentForYou newInstance(String param1, String param2) {
        FragmentForYou fragment = new FragmentForYou();
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
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_for_you, container, false);
        RelativeLayout layout=v.findViewById(R.id.top_layout);
        thisActivity=(Activity)getActivity();
        rv_videolist=v.findViewById(R.id.rv_videolist);
        rv_categorylist=v.findViewById(R.id.rv_categorylist);
        rv_small_industry=v.findViewById(R.id.rv_small_industry);
        dotsIndicator = (DotsIndicator) v.findViewById(R.id.dots_indicator);
        carousel=v.findViewById(R.id.carousel);
        carousel.setPageTransformer(true, new DepthPageTransformer());
        Dots=v.findViewById(R.id.Dots);

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

        rv_videolist.setHasFixedSize(true);
        rv_videolist.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        videoListAdapter=new VideoListAdapter(getActivity(), rv_videos,temp1,rv_videos_title,thisActivity);
        get_rv_videolist();

        rv_categorylist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rv_categorylist.setLayoutManager(linearLayoutManager);
        categoryListAdapter =new CategoryListAdapter(getActivity(),heading,imageurl,categorytitle,categoryamount);
        rv_categorylist.setAdapter(categoryListAdapter);
        categoryListAdapter.notifyDataSetChanged();

        rv_small_industry.setHasFixedSize(true);
        rv_small_industry.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        industrySmallCardAdapter=new IndustrySmallCardAdapter(getActivity(),heading2,sampleimgurl);
        rv_small_industry.setAdapter(industrySmallCardAdapter);
        industrySmallCardAdapter.notifyDataSetChanged();

        layout.requestFocus();
        return v;




//!To initialize the Youtube Fragmnet in a activity
//        YouTubePlayerFragment youTubePlayerFragment =
//                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
//        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
//                                        boolean wasRestored) {
//        if (!wasRestored) {
//            player.cueVideo("nCgQDjiotG0");
//        }
//    }
//
//    @Override
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

    private void get_rv_videolist(){
        JSONObject json = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,APIURL.url+"video/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"video/list"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.d("zzzobjct",object.toString());
                                    String video_ID = object.getString("video_url");
                                    rv_videos.add(video_ID);

                                    String title = object.getString("title");
                                    rv_videos_title.add(title);

                                    Log.d("zzz id",video_ID +" "+title );

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            rv_videolist.setAdapter(videoListAdapter);
                            videoListAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
