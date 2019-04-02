package com.recko.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.recko.app.Adapters.CategoryListAdapter;
import com.recko.app.Adapters.LearnVideoAdapter;
import com.recko.app.Adapters.SliderAdapter;
import com.recko.app.Model.CarouselModel;
import com.recko.app.Model.VideosModel;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


public class FragmentLearn extends androidx.fragment.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView rv_videolist;
    private List videoslist;
    private ViewPager carousel;
    private SliderAdapter sliderAdapter;

    private LearnVideoAdapter learnVideoAdapter;
    private Activity thisActivity;
    private DotsIndicator dotsIndicator;
    private CategoryListAdapter categoryListAdapter;

    private List carousel_images;


    public FragmentLearn() {
        // Required empty public constructor
    }

    public static FragmentLearn newInstance(String param1, String param2) {
        FragmentLearn fragment = new FragmentLearn();
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
        View v = inflater.inflate(R.layout.fragment_learn, container, false);
        thisActivity=(Activity)getActivity();
        rv_videolist=v.findViewById(R.id.rv_videolist);

        dotsIndicator = (DotsIndicator) v.findViewById(R.id.dots_indicator);
        carousel=v.findViewById(R.id.carousel);
        carousel.setPageTransformer(true, new DepthPageTransformer());
        carousel_images = new ArrayList();
        sliderAdapter=new SliderAdapter(getActivity(),carousel_images);
        carousel.setAdapter(sliderAdapter);
        get_carousel_images();
        dotsIndicator.setViewPager(carousel);


        videoslist=new ArrayList();

        rv_videolist.setHasFixedSize(true);
        rv_videolist.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        learnVideoAdapter=new LearnVideoAdapter(getActivity(),videoslist,thisActivity);
        rv_videolist.setAdapter(learnVideoAdapter);
        get_rv_videolist();

        return v;
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
                                    String video_url = object.getString("video_url");
                                    String video_uuid = object.getString("video_uuid");
                                    String thumbnail_url = object.getString("thumbnail_url");
                                    String title = object.getString("title");
                                    String flavour_text = object.getString("flavour_text");

                                    VideosModel videosModel = new VideosModel(thumbnail_url,video_url,video_uuid,title);
                                    videosModel.setFlavour_text(flavour_text);
                                    videoslist.add(videosModel);
                                    Log.d("zzz id",video_url +" "+title );

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            learnVideoAdapter.notifyDataSetChanged();
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

    private void get_carousel_images() {
        JSONObject json = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"carousel/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"carousel/list"+"\nonResponse: "+response);

                        try {
                            JSONArray array= response.getJSONArray("data");
                            Log.d("zzzarray",array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    JSONObject object = array.getJSONObject(i);
                                    Log.d("zzzobjct",object.toString());
                                    String image_url = object.getString("image_url");
                                    String web_link = object.getString("web_link");
                                    String carousel_id = object.getString("carousel_uuid");

                                    CarouselModel carouselModel= new CarouselModel(carousel_id,image_url,web_link);
                                    carousel_images.add(carouselModel);
                                    Log.d("zzz id",image_url +" "+web_link+" " +carousel_id );

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            sliderAdapter.notifyDataSetChanged();
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
