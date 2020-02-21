package com.recko.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.recko.app.Adapters.CategoryListAdapter;
import com.recko.app.Adapters.IndustrySmallCardAdapter;
import com.recko.app.Adapters.SliderAdapter;
import com.recko.app.Adapters.VideoListAdapter;
import com.recko.app.Misc.Constants;
import com.recko.app.Misc.CustomJsonObjectRequest;
import com.recko.app.Model.CarouselModel;
import com.recko.app.Model.CategoryListModel;
import com.recko.app.Model.IndustryCardModel;
import com.recko.app.Model.VideosModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

    private DotsIndicator dotsIndicator;
    private VideoListAdapter videoListAdapter;
    private Activity thisActivity;
    private CategoryListAdapter categoryListAdapter;
    private IndustrySmallCardAdapter industrySmallCardAdapter;


    List<String> sampleimgurl= new ArrayList<String>();

    private List<String> heading2= new ArrayList<String>();


    private List categorylist;
    private List carousel_images;
    private List videoslist;
    private List cardindustrylist;
    private FirebaseAnalytics mFirebaseAnalytics;
    private SwipeRefreshLayout swipeRefreshLayout;

    private AtomicInteger remaining_callbacks;

    public FragmentForYou() {

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        remaining_callbacks = new AtomicInteger();
        remaining_callbacks.set(0);
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

        // Top down refresh
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                remaining_callbacks.set(4);
                get_smallcard_industry();
                get_carousel_images();
                get_rv_categorylist();
                get_rv_videolist();
                Log.d("zzz FragForYou", "pull down refresh");
            }
        });

        carousel_images=new ArrayList();
        categorylist=new ArrayList();
        videoslist=new ArrayList();
        cardindustrylist=new ArrayList();

        sliderAdapter=new SliderAdapter(getActivity(),carousel_images);
        carousel.setAdapter(sliderAdapter);
        get_carousel_images();
        dotsIndicator.setViewPager(carousel);

/*
        sampleimgurl.add("https://www.desktopbackground.org/download/2000x1500/2010/04/10/29_ultra-hd-4k-rain-wallpapers-hd-desktop-backgrounds-3840x2400_3840x2400_h.jpg");
        sampleimgurl.add("https://www.oneperiodic.com/products/photobatch/tutorials/img/scale_original.png");
        sampleimgurl.add("https://a-static.besthdwallpaper.com/above-the-clouds-sunset-wallpaper-2048x1536-4355_26.jpg");
        sampleimgurl.add("https://s4301.pcdn.co/wp-content/uploads/2012/11/20130312_10thExterior_night-desktop4x3.jpg");
*/

//        heading.add("Popular Products for you");
//        heading.add("Kamao BC Kamao");
//        heading.add("Kamao BC Kamao");
//        heading.add("Kamao BC Kamao");
        heading2.add("Education");
        heading2.add("Medical");
        heading2.add("Finance");
        heading2.add("Finance");
//        List<String > temp1= new ArrayList<String>();
//        temp1.add("https://cdn.tutsplus.com/photo/uploads/legacy/746_aspectratio/07.jpg");
//        temp1.add("https://g2e-gamers2mediasl.netdna-ssl.com/wp-content/uploads/2016/03/G2-Esports-3D-Grey-Logo-1200x600.jpg");
//        temp1.add("https://iacopodeenosee.files.wordpress.com/2013/06/abstract-circles-l.jpg");
//        temp1.add("https://g2e-gamers2mediasl.netdna-ssl.com/wp-content/uploads/2017/08/weareG2esports-1200x600.jpg");


        rv_categorylist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rv_categorylist.setLayoutManager(linearLayoutManager);
        categoryListAdapter =new CategoryListAdapter(getActivity(),categorylist,thisActivity);
        rv_categorylist.setAdapter(categoryListAdapter);
        get_rv_categorylist();

        rv_videolist.setHasFixedSize(true);
        rv_videolist.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        videoListAdapter=new VideoListAdapter(getActivity(),videoslist,thisActivity);
        rv_videolist.setAdapter(videoListAdapter);
        get_rv_videolist();



        rv_small_industry.setHasFixedSize(true);
        rv_small_industry.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        industrySmallCardAdapter=new IndustrySmallCardAdapter(getActivity(),cardindustrylist,thisActivity);
        rv_small_industry.setAdapter(industrySmallCardAdapter);
        get_smallcard_industry();


        layout.requestFocus();
        return v;

    }

    private void maybeStopRefresh() {
        if (remaining_callbacks.get() == 0) return;
        int remaining = remaining_callbacks.decrementAndGet();
        if (remaining == 0) swipeRefreshLayout.setRefreshing(false);
    }

    private void get_smallcard_industry() {

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,APIURL.url+"user/list_industries", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "user/list_industries" + "\nonResponse: " + response);
                        maybeStopRefresh();

                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            cardindustrylist.clear();
                            JSONArray array1 = response.getJSONArray("data");
                            Log.d("zzzarray", array1.toString());
                            for (int i = 0; i < array1.length(); i++) {
                                try {
                                    JSONObject object = array1.getJSONObject(i);
                                    String image_url = object.getString("image_url");
                                    String industry_uuid = object.getString("industry_uuid");
                                    String img_url_high_res = object.getString("image_url_high_res");
                                    String name = object.getString("name");

                                    Log.d("zzzarray", industry_uuid + ", " + name);
                                    Log.d("zzzarray", image_url);

                                    IndustryCardModel industryCardModel=new IndustryCardModel(image_url,name,industry_uuid);
                                    if (Constants.isValidString(img_url_high_res))
                                        industryCardModel.setImage_url(img_url_high_res);
                                    cardindustrylist.add(industryCardModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            industrySmallCardAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
				Constants.logVolleyError(error);
                maybeStopRefresh();
                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void get_carousel_images() {
        JSONObject json = new JSONObject();
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,APIURL.url+"carousel/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"carousel/list"+"\nonResponse: "+response);
                        maybeStopRefresh();

                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            carousel_images.clear();
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
				Constants.logVolleyError(error);
                maybeStopRefresh();
                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void get_rv_categorylist() {

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST,APIURL.url+"user/front_page_categories", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "user/front_page_categories" + "\nonResponse: " + response);
                        maybeStopRefresh();

                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            categorylist.clear();
                            JSONArray array1 = response.getJSONArray("data");
                            Log.d("zzzarray", array1.toString());
                            for (int i = 0; i < array1.length(); i++) {
                                try {
                                    JSONObject object = array1.getJSONObject(i);
                                    String Cat_title = object.getString("heading");

                                    List<String > temp1= new ArrayList<String>();
                                    List<String > temp2= new ArrayList<String>();
                                    List<String > temp3= new ArrayList<String>();
                                    List<String > temp4= new ArrayList<String>();
                                    List<String > temp5= new ArrayList<String>();
                                    List<String > temp6= new ArrayList<String>();

                                    Log.d("zzzarray", object.toString());
                                    JSONArray array2 = object.getJSONArray("data");
                                    for (int j = 0; j < array2.length(); j++) {

                                        try {
                                            JSONObject object2 = array2.getJSONObject(j);
                                            Log.d("zzzobjct", object2.toString());
                                            String max_commission = object2.getString("max_commission");
                                            String name = object2.getString("name");
                                            String industry = object2.getString("industry");
                                            String category_uuid = object2.getString("category_uuid");
                                            String no_of_products = String.valueOf(object2.getInt("no_of_product"));
                                            String image_url = object2.getString("image_url");

                                            // Try to remove decimal places from max_commission
                                            try {
                                                float tmp_max_commission = Float.parseFloat(max_commission.replaceAll("\\s+",""));
                                                max_commission = Integer.toString((int) Math.ceil(tmp_max_commission));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            temp1.add(max_commission);
                                            temp2.add(name);
                                            temp3.add(industry);
                                            temp4.add(category_uuid);
                                            temp5.add(image_url);
                                            temp6.add(no_of_products);

                                            Log.d("zzz id", max_commission + " " + name + " " + industry + " " + " " + category_uuid + " " + no_of_products + " " + image_url);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    CategoryListModel categoryListModel=new CategoryListModel(Cat_title,temp5,temp2,temp3,temp1,temp4,temp6);
                                    categorylist.add(categoryListModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            categoryListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
				Constants.logVolleyError(error);
                maybeStopRefresh();
                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


    private void get_rv_videolist(){
        JSONObject json = new JSONObject();
        CustomJsonObjectRequest jsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET,APIURL.url+"video/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("zzz", APIURL.url+"video/list"+"\nonResponse: "+response);
                        maybeStopRefresh();

                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            videoslist.clear();
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

                                    VideosModel videosModel = new VideosModel(thumbnail_url,video_url,video_uuid,title);
                                    videoslist.add(videosModel);
                                    Log.d("zzz id",video_url +" "+title );

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            videoListAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
				Constants.logVolleyError(error);
                maybeStopRefresh();
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
