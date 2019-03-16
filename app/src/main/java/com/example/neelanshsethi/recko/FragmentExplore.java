package com.example.neelanshsethi.recko;

import android.app.Activity;
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
import com.example.neelanshsethi.recko.Adapters.CategoryListAdapter;
import com.example.neelanshsethi.recko.Adapters.CompanyListGridAdapter;
import com.example.neelanshsethi.recko.Adapters.Company_InCategoryAndCompanyAdapter;
import com.example.neelanshsethi.recko.Adapters.IndustrySmallCardAdapter;
import com.example.neelanshsethi.recko.Adapters.ProductListGridAdapter;
import com.example.neelanshsethi.recko.Adapters.Products_InCategoryAdapter;
import com.example.neelanshsethi.recko.Adapters.SliderAdapter;
import com.example.neelanshsethi.recko.Misc.Constants;
import com.example.neelanshsethi.recko.Misc.RecyclerViewCustom;
import com.example.neelanshsethi.recko.Model.CarouselModel;
import com.example.neelanshsethi.recko.Model.CategoryListModel;
import com.example.neelanshsethi.recko.Model.Company_InCategoryAndCompanyModel;
import com.example.neelanshsethi.recko.Model.IndustryCardModel;
import com.example.neelanshsethi.recko.Model.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

    List productslist;
    private RecyclerView rv_videolist;
    private RecyclerView rv_productlist1;
    private RecyclerView rv_categorylist2;
    private RecyclerView rv_companyexp1;
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
    private CompanyListGridAdapter compnayListGridAdapter;
    private List companylist;
    private List cardindustrylist;

    private Products_InCategoryAdapter products_inCategoryAdapter;
    private ProductListGridAdapter productListGridAdapter;

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

    private List categorylist1;
    private List categorylist2;
    private List carousel_images;
    private List small_card_list1;
    private List small_card_list2;


    private SwipeRefreshLayout swipeRefreshLayout;
    private AtomicInteger remaining_callbacks;

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
        remaining_callbacks = new AtomicInteger();
        remaining_callbacks.set(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_explore, container, false);

        thisActivity=(Activity)getActivity();
        rv_videolist=v.findViewById(R.id.rv_videolist);
        rv_productlist1=v.findViewById(R.id.rv_productlist1);
        rv_companyexp1=v.findViewById(R.id.rv_companyexp1);
        rv_small_industry2=v.findViewById(R.id.rv_small_industry2);
        RelativeLayout layout = v.findViewById(R.id.top_layout);

        // Top down refresh
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                remaining_callbacks.set(3);
                get_rv_categorylist();
                get_carousel_images();
                get_smallcard_industry();
                Log.d("zzz FragForYou", "pull down refresh");
            }
        });

        categorylist1 = new ArrayList();
        categorylist2 = new ArrayList();
        carousel_images = new ArrayList();
        small_card_list1 = new ArrayList();
        small_card_list2 = new ArrayList();
        productslist = new ArrayList();
        companylist = new ArrayList();
        cardindustrylist = new ArrayList();

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

        rv_productlist1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rv_productlist1.setLayoutManager(linearLayoutManager);
        productListGridAdapter =new ProductListGridAdapter(getActivity(),productslist,getActivity());
        rv_productlist1.setAdapter(productListGridAdapter);


        rv_companyexp1.setHasFixedSize(true);
        rv_companyexp1.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        compnayListGridAdapter = new CompanyListGridAdapter(getActivity(), companylist, getActivity());
        rv_companyexp1.setAdapter(compnayListGridAdapter);

        rv_small_industry2.setHasFixedSize(true);
        rv_small_industry2.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        industrySmallCardAdapter2=new IndustrySmallCardAdapter(getActivity(),cardindustrylist,getActivity());
        rv_small_industry2.setAdapter(industrySmallCardAdapter2);

        get_rv_categorylist();
        get_carousel_images();
        get_smallcard_industry();
        layout.requestFocus();
        return v;
    }

    private void maybeStopRefresh() {
        if (remaining_callbacks.get() == 0) return;
        int remaining = remaining_callbacks.decrementAndGet();
        if (remaining == 0) swipeRefreshLayout.setRefreshing(false);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"user/explore_page_categories", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "user/explore_page_categories" + "\nonResponse: " + response);
                        maybeStopRefresh();

                        try {
                            String code = response.getString("code");
                            if (!code.equals("200")) {
                                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONArray array1 = response.getJSONArray("data");
                            Log.d("zzzarray", array1.toString());
                            for (int i = 0; i < array1.length(); i++) {
                                try {
                                    JSONObject object = array1.getJSONObject(i);
                                    maybe_fill_product_model(object);
                                    maybe_fill_companies(object);
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
                                   // categorylist.add(categoryListModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //categoryListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                maybeStopRefresh();
                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


    private void maybe_fill_product_model(JSONObject data) {
        try {
            String heading = data.has("heading") ? data.getString("heading") : null;
            // We are only filling products in this method.
            if (!heading.equals("Newly Launched Products")) return;

            Log.d("zzz explore2", "here 1");
            JSONArray array = data.has("data")?data.getJSONArray("data"):null;
            if (array == null) return;

            productslist.clear();
            Log.d("zzz explore2", "here 2");
            for (int i = 0; i<array.length(); i++) {
                JSONObject productDetails = array.getJSONObject(i);
                Log.d("zzzobjct", productDetails.toString());

                String broucher =  !productDetails.has("broucher")?"null":productDetails.getString("broucher");
                String video = !productDetails.has("video")?"null":productDetails.getString("video");
                String to_date = !productDetails.has("to_date")?"null":productDetails.getString("to_date");
                String good_time_to_sell = !productDetails.has("good_time_to_sell")?"null":productDetails.getString("good_time_to_sell");
                String category = !productDetails.has("category")?"null":productDetails.getString("category");
                String title = !productDetails.has("title")?"null":productDetails.getString("title");
                String upfront_commission = !productDetails.has("upfront_commission")?"null":productDetails.getString("upfront_commission");
                String from_date = !productDetails.has("from_date")?"null":productDetails.getString("from_date");
                String location_of_sell = !productDetails.has("location_of_sell")?"null":productDetails.getString("location_of_sell");
                String target_customer = !productDetails.has("target_customer")?"null":productDetails.getString("target_customer");
                String type = !productDetails.has("type")?"null":productDetails.getString("type");
                String price_on_x = !productDetails.has("price_on_x")?"null":productDetails.getString("price_on_x");
                String companyy_uuid = !productDetails.has("company_uuid")?"null":productDetails.getString("company_uuid");
                String total_commission = !productDetails.has("total_commission")?"null":productDetails.getString("total_commission");
                String tips_to_sell = !productDetails.has("tips_to_sell")?"null":productDetails.getString("tips_to_sell");
                String customer_data_needed = !productDetails.has("customer_data_needed")?"null":productDetails.getString("customer_data_needed");
                String product_details = !productDetails.has("product_details")?"null":productDetails.getString("product_details");
                String payment_type = !productDetails.has("payment_type")?"null":productDetails.getString("payment_type");
                String product_uuid =!productDetails.has("product_uuid")?"null": productDetails.getString("product_uuid");
                String mrp = !productDetails.has("mrp")?"null":productDetails.getString("mrp");

                ProductModel productModel = new ProductModel(broucher, video, to_date, good_time_to_sell, category, title, upfront_commission, from_date, location_of_sell, target_customer, type, price_on_x, companyy_uuid, total_commission, tips_to_sell, customer_data_needed, product_details, payment_type, product_uuid, mrp);
                Log.d("zzz explore1", "adding 1 model");
                productslist.add(productModel);
            }

            Log.d("zzz explore2", "invoking data changegd");
            productListGridAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_carousel_images() {
        JSONObject json = new JSONObject();
        try {
            json.put("TAG", "explore_fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"carousel/list", json,
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
                maybeStopRefresh();
                error.printStackTrace();
                Toast.makeText(getActivity(),"Oops! Please try again later",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void maybe_fill_companies(JSONObject data) throws JSONException {
        String heading = data.has("heading") ? data.getString("heading") : null;
        // We are only filling companies in this method.
        if (!heading.equals("Newly Added Companies")) return;

        JSONArray array= data.getJSONArray("data");
        Log.d("zzzarray",array.toString());
        companylist.clear();
        for (int i = 0; i < array.length(); i++) {
            try {

                JSONObject object = array.getJSONObject(i);
                JSONObject company_detail = object.getJSONObject("company");
                Log.d("zzzobjct companydetails",company_detail.toString());

                Company_InCategoryAndCompanyModel company_inCategoryAndCompanyModel;

                String max_commission = company_detail.getString("max_commission");
                String company_uuid = company_detail.getString("company_uuid");
                String image_url = company_detail.getString("image_url");
                String company_name = company_detail.getString("company_name");
                String no_of_products = company_detail.getString("no_of_products");

                JSONArray products = object.getJSONArray("products");
                Log.d("zzzarray products",products.toString());

                List<ProductModel> temp = new ArrayList<ProductModel>();
                for(int j = 0; j < products.length(); j++)
                {
                    JSONObject productDetails = products.getJSONObject(j);
                    Log.d("zzz productdetails",productDetails.toString());

                    String broucher = productDetails.getString("broucher");
                    String video = productDetails.getString("video");
                    String to_date = productDetails.getString("to_date");
                    String good_time_to_sell = productDetails.getString("good_time_to_sell");
                    String category = productDetails.getString("category");
                    String title = productDetails.getString("title");
                    String upfront_commission = productDetails.getString("upfront_commission");
                    String from_date = productDetails.getString("from_date");
                    String location_of_sell = productDetails.getString("location_of_sell");
                    String target_customer = productDetails.getString("target_customer");
                    String type = productDetails.getString("type");
                    String price_on_x = productDetails.getString("price_on_x");
                    String companyy_uuid = productDetails.getString("company_uuid");
                    String total_commission = productDetails.getString("total_commission");
                    String tips_to_sell = productDetails.getString("tips_to_sell");
                    String customer_data_needed = productDetails.getString("customer_data_needed");
                    String product_details = productDetails.getString("product_details");
                    String payment_type = productDetails.getString("payment_type");
                    String product_uuid = productDetails.getString("product_uuid");
                    String mrp = productDetails.getString("mrp");

                    ProductModel productModel = new ProductModel(broucher,video,to_date,good_time_to_sell,category,title,upfront_commission,from_date,location_of_sell,target_customer,type,price_on_x,companyy_uuid,total_commission,tips_to_sell,customer_data_needed,product_details,payment_type,product_uuid,  mrp);
                    temp.add(productModel);

                }
                company_inCategoryAndCompanyModel = new Company_InCategoryAndCompanyModel(max_commission,company_uuid,image_url,company_name,no_of_products,temp);
                companylist.add(company_inCategoryAndCompanyModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // rv_companyexp1.setMaxHeight(Constants.max_company_height_in_explore);
        compnayListGridAdapter.notifyDataSetChanged();
    }

    private void get_smallcard_industry() {

        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        JSONObject json = new JSONObject();
        try {
            json.put("seller_uuid",mUser.getUid());
            json.put("all", true);
            Log.d("zzz json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,APIURL.url+"industry/list", json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("zzz", APIURL.url + "industry/list" + "\nonResponse: " + response);
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
                                    String name = object.getString("name");

                                    Log.d("zzzarray", object.toString());

                                    IndustryCardModel industryCardModel=new IndustryCardModel(image_url,name,industry_uuid);
                                    industryCardModel.setShould_use_large_image(true);
                                    cardindustrylist.add(industryCardModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            industrySmallCardAdapter2.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                maybeStopRefresh();
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
