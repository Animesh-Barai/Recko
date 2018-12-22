package com.example.neelanshsethi.sello;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple YouTube Android API demo application which shows how to create a simple application that
 * shows a YouTube Video in a {@link YouTubePlayerFragment}.
 * <p>
 * Note, this sample app extends from {@link YouTubeFailureRecoveryActivity} to handle errors, which
 * itself extends {@link YouTubeBaseActivity}. However, you are not required to extend
 * {@link YouTubeBaseActivity} if using {@link YouTubePlayerFragment}s.
 */
public class ForYou extends AppCompatActivity {


    private RecyclerView rv_videolist;
    private RecyclerView rv_categorylist;
    private ViewPager carousel;
    private SliderAdapter sliderAdapter;
    private ImageView[] dots;
    private LinearLayout Dots;
    private DotsIndicator dotsIndicator;
    private VideoListAdapter videoListAdapter;
    private Activity thisActivity;
    private CategoryListAdapter categoryListAdapter;

    //    String[] list={"nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0","nCgQDjiotG0"};
    private List<String> rv_videos= new ArrayList<String>();
    private List<String> rv_videos_title= new ArrayList<String>();
    int carousel_images[]={R.drawable.sample,R.drawable.sample2,R.drawable.sample,R.drawable.sample};
    int[] rv_thumbnails={R.drawable.sample,R.drawable.sample2,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample,R.drawable.sample};

    List<String> sampleimgurl= new ArrayList<String>();

    private List<String> heading= new ArrayList<String>();
    private List<List<String>> imageurl= new ArrayList<List<String>>();
    private List<List<String>> categorytitle= new ArrayList<List<String>>();
    private List<List<String>> categoryamount= new ArrayList<List<String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_you);
        thisActivity=(Activity)this;
        rv_videolist=findViewById(R.id.rv_videolist);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        carousel=findViewById(R.id.carousel);
        carousel.setPageTransformer(true, new DepthPageTransformer());
        Dots=findViewById(R.id.Dots);

        sliderAdapter=new SliderAdapter(this,carousel_images);
        carousel.setAdapter(sliderAdapter);
        dotsIndicator.setViewPager(carousel);

        sampleimgurl.add("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        sampleimgurl.add("https://homepages.cae.wisc.edu/~ece533/images/cat.png");
        sampleimgurl.add("https://homepages.cae.wisc.edu/~ece533/images/lena.bmp");
        sampleimgurl.add("https://homepages.cae.wisc.edu/~ece533/images/tulips.png");

        rv_videolist.setHasFixedSize(true);
        rv_videolist.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        videoListAdapter=new VideoListAdapter(getApplicationContext(), rv_videos,sampleimgurl,rv_videos_title,thisActivity);
        get_rv_videolist();

        rv_categorylist=findViewById(R.id.rv_categorylist);
        heading.add("Popular Products for you");
        heading.add("Kamao BC Kamao");
        List<String > temp1= new ArrayList<String>();
        temp1.add("https://testimages.org/img/testimages_screenshot.jpg");
        temp1.add("https://homepages.cae.wisc.edu/~ece533/images/cat.png");
        temp1.add("https://homepages.cae.wisc.edu/~ece533/images/lena.bmp");
        temp1.add("https://homepages.cae.wisc.edu/~ece533/images/tulips.png");
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

        rv_categorylist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv_categorylist.setLayoutManager(linearLayoutManager);
//        categoryListAdapter =new CategoryListAdapter(this,heading,imageurl,categorytitle,categoryamount);
        rv_categorylist.setAdapter(categoryListAdapter);
        categoryListAdapter.notifyDataSetChanged();








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
                Toast.makeText(ForYou.this,"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ForYou.this);
        requestQueue.add(jsonObjectRequest);
    }


}

