package ir.arapp.arappofficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.madapps.liquid.LiquidRefreshLayout;
import com.muddzdev.styleabletoast.StyleableToast;
import com.zhpan.bannerview.BannerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ir.arapp.arappofficial.Adapter.BannerAdapter;
import ir.arapp.arappofficial.Adapter.BannerViewHolder;
import ir.arapp.arappofficial.Adapter.CategoryServiceAdapter;
import ir.arapp.arappofficial.Adapter.HomeServiceAdapter;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.Data.SliderItem;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment
{
    //Variables
    private LiquidRefreshLayout refreshLayout;
    LottieAnimationView newsLoading;
    private BannerViewPager<SliderItem, BannerViewHolder> bannerViewPager;
    private List<SliderItem> sliderItemList;
    private LinearLayout popularLinearLayout;
    private LinearLayout newServicesLinearLayout;
    private LinearLayout restaurantLinearLayout;
    private LinearLayout fastFoodLinearLayout;
    private LinearLayout cafeLinearLayout;
    private LinearLayout hotelLinearLayout;
    private LinearLayout restRoomLinearLayout;
    private LinearLayout shopCenterLinearLayout;
    private RecyclerView popularRecyclerView;
    private RecyclerView newServicesRecyclerView;
    private RecyclerView restaurantRecyclerView;
    private RecyclerView fastFoodRecyclerView;
    private RecyclerView cafeRecyclerView;
    private RecyclerView hotelRecyclerView;
    private RecyclerView restRoomRecyclerView;
    private RecyclerView shopCenterRecyclerView;
    private RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Hooks
        refreshLayout = view.findViewById(R.id.refreshLayout);
        newsLoading = view.findViewById(R.id.newsLoading);
        bannerViewPager = view.findViewById(R.id.bannerSlider);
        popularRecyclerView = view.findViewById(R.id.recyclerViewLikes);
        newServicesRecyclerView = view.findViewById(R.id.recyclerViewNewServices);
        restaurantRecyclerView = view.findViewById(R.id.recyclerViewRestaurant);
        fastFoodRecyclerView = view.findViewById(R.id.recyclerViewFastFood);
        cafeRecyclerView = view.findViewById(R.id.recyclerViewCafe);
        hotelRecyclerView = view.findViewById(R.id.recyclerViewHotel);
        restRoomRecyclerView = view.findViewById(R.id.recyclerViewRestRoom);
        shopCenterRecyclerView = view.findViewById(R.id.recyclerViewShopCenter);
        popularLinearLayout = view.findViewById(R.id.popularLinearLayout);
        newServicesLinearLayout = view.findViewById(R.id.newServicesLinearLayout);
        restaurantLinearLayout = view.findViewById(R.id.restaurantLinearLayout);
        fastFoodLinearLayout = view.findViewById(R.id.fastFoodLinearLayout);
        cafeLinearLayout = view.findViewById(R.id.cafeLinearLayout);
        hotelLinearLayout = view.findViewById(R.id.hotelLinearLayout);
        restRoomLinearLayout = view.findViewById(R.id.restRoomLinearLayout);
        shopCenterLinearLayout = view.findViewById(R.id.shopCenterLinearLayout);

        //Slider
        if (checkConnection())
        {
            sliderItemList = new ArrayList<>();
            getNewsData();
        }

        //RecyclerView
        if (checkConnection())
        {
            ViewCompat.setNestedScrollingEnabled(popularRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(newServicesRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(restaurantRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(fastFoodRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(cafeRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(hotelRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(restRoomRecyclerView, false);
            ViewCompat.setNestedScrollingEnabled(shopCenterRecyclerView, false);
            popularRecyclerViewShow();
            newServicesRecyclerViewShow();
            restaurantRecyclerViewShow();
            fastFoodRecyclerViewShow();
            cafeRecyclerViewShow();
            hotelRecyclerViewShow();
            restRoomRecyclerViewShow();
            shopCenterRecyclerViewShow();
        }

        //refresh layout
        liquidRefreshLayout();

        return view;
    }

    //check internet connection ...
    private boolean checkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != networkInfo)
        {
            return true;
        }
        else
        {
            StyleableToast.makeText(getActivity().getApplicationContext(), "عدم اتصال به اینترنت!", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return false;
        }
    }

    //Slider news
    private void getNewsData()
    {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getNews();

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        String responseBody = Objects.requireNonNull(response.body()).string();
                        JSONArray jsonArray = new JSONArray(responseBody);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String image = jsonObject.getString("image");
                            String text = jsonObject.getString("text");
                            int show = jsonObject.getInt("show");
                            String created_at = jsonObject.getString("created_at");
                            String updated_at = jsonObject.getString("updated_at");
                            sliderItemList.add(new SliderItem(id, show, text, image, created_at, updated_at));
                        }
                        bannerAdapter();
                    }
                    catch (IOException | JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                StyleableToast.makeText(Objects.requireNonNull(getContext()), t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        });
    }
    private void bannerAdapter()
    {
        newsLoading.setVisibility(View.GONE);
        bannerViewPager.setVisibility(View.VISIBLE);
        BannerAdapter bannerAdapter = new BannerAdapter();
        bannerViewPager
                .setOffScreenPageLimit(2)
                .setLifecycleRegistry(getLifecycle())
                .setAdapter(bannerAdapter)
                .registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
                {
                    @Override
                    public void onPageSelected(int position)
                    {
                        //SliderItem sliderItem = bannerViewPager.getData().get(position);
                        super.onPageSelected(position);
                    }
                })
                .setOnPageClickListener(position ->
                {
                    StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                            "اطلاعیه "+position, Toast.LENGTH_LONG, R.style.toastTheme).show();
                })
                .create(sliderItemList);
        bannerAdapter.notifyDataSetChanged();
    }

    //RecyclerView method
    private void popularRecyclerViewShow()
    {
        popularRecyclerView.setHasFixedSize(true);
        popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        ArrayList<HomeServicesData> servicesData = new ArrayList<>();

        //test
        servicesData.add(new HomeServicesData(4, 4, 1, "alirezafateminia@gmail.com", "09386513123", "مرکز خرید ولیعصر",
                "ولیعصر، برای تو", "علیرضا فاطمی نیا", "فروظگاه خرید ولیعصر لحظات خوبی را برای شما در نظر گرفته است. مفتخریم اعلام کنیم که فروشگاه های همواره تخفیف ولیعصر در تمامی حوزه ها متخصصین بالقوه ای را آماده خدمت رسانی به مشتریان عزیز نموده است.",
                R.drawable.shop_center, "چند دقیقه پیش", 0, "مراکز خرید", "رشت", "گیلان", 0, 23, 4,
                1, "رشت، کنار مجتمع بگیلان", "close"));

        servicesData.add(new HomeServicesData(1, 1, 1, "osirandoust@gmail.com", "09369642754", "رستوران عقاب سبز",
                "همچون عقاب سیر کنید", "اسامه ایراندوست", "در خیابان های کردستان تنها قدم نزنید. رستوران عقاب شما را به دنیایی دیگر می برد. با عقاب سفر کنید",
                R.drawable.restaurant, "2 روز پیش", 4, "رستوران", "سنندج", "کردستان", 320, 850, 20,
                13, "سنندج، میدان اقبال", "open"));

        servicesData.add(new HomeServicesData(2, 2, 1, "sogand.rje@gmail.com", "09118333033", "کافه لاماسیا",
                "با لاماسیا تا آسیا", "سوگند رجایی", "اگر بدنبال بهترین کافه گیلان هستید لاماسیا را از دست ندهید. همراه با بازی های جذاب و جایزه های فراوان و اینترنت رایگان",
                R.drawable.cafe, "5 روز پیش", 3.2, "کافه", "رشت", "گیلان", 256, 120, 2,
                18, "رشت، سبزه میدان، جنب کافه کتاب", "close_soon"));

        servicesData.add(new HomeServicesData(3, 3, 1, "mmd.badi@gmail.com", "09116948828", "فروشگاه گیزمیز",
                "گیز میز ... خریدی لیز", "محمد بادزهره", "فروشگاه های خرید پوشاک همواره تخفیف گیزمیز که با بهترین کیفیت در استان گیلان مشغول به خدمت رسانی است",
                R.drawable.rest_room, "یک هفته پیش", 1.8, "مراکز خرید", "فومن", "گیلان", 565, 10, 6,
                77, "فومن، خیابان امام", "open_soon"));

        if (servicesData.size() == 0)
        {
            popularLinearLayout.setVisibility(View.GONE);
        }

        adapter = new HomeServiceAdapter(getActivity(), servicesData);
        popularRecyclerView.setAdapter(adapter);
    }
    private void newServicesRecyclerViewShow()
    {
        newServicesRecyclerView.setHasFixedSize(true);
        newServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        ArrayList<HomeServicesData> servicesData = new ArrayList<>();

        //test
        servicesData.add(new HomeServicesData(4, 4, 1, "alirezafateminia@gmail.com", "09386513123", "مرکز خرید ولیعصر",
                "ولیعصر، برای تو", "علیرضا فاطمی نیا", "فروظگاه خرید ولیعصر لحظات خوبی را برای شما در نظر گرفته است. مفتخریم اعلام کنیم که فروشگاه های همواره تخفیف ولیعصر در تمامی حوزه ها متخصصین بالقوه ای را آماده خدمت رسانی به مشتریان عزیز نموده است.",
                R.drawable.shop_center, "چند دقیقه پیش", 0, "مراکز خرید", "رشت", "گیلان", 0, 23, 4,
                1, "رشت، کنار مجتمع بگیلان", "close"));

        servicesData.add(new HomeServicesData(1, 1, 1, "osirandoust@gmail.com", "09369642754", "رستوران عقاب سبز",
                "همچون عقاب سیر کنید", "اسامه ایراندوست", "در خیابان های کردستان تنها قدم نزنید. رستوران عقاب شما را به دنیایی دیگر می برد. با عقاب سفر کنید",
                R.drawable.restaurant, "2 روز پیش", 4, "رستوران", "سنندج", "کردستان", 320, 850, 20,
                13, "سنندج، میدان اقبال", "open"));

        servicesData.add(new HomeServicesData(2, 2, 1, "sogand.rje@gmail.com", "09118333033", "کافه لاماسیا",
                "با لاماسیا تا آسیا", "سوگند رجایی", "اگر بدنبال بهترین کافه گیلان هستید لاماسیا را از دست ندهید. همراه با بازی های جذاب و جایزه های فراوان و اینترنت رایگان",
                R.drawable.cafe, "5 روز پیش", 3.2, "کافه", "رشت", "گیلان", 256, 120, 2,
                18, "رشت، سبزه میدان، جنب کافه کتاب", "close_soon"));

        adapter = new HomeServiceAdapter(getActivity(), servicesData);
        newServicesRecyclerView.setAdapter(adapter);
    }
    private void restaurantRecyclerViewShow()
    {
        restaurantRecyclerView.setHasFixedSize(true);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        ArrayList<HomeServicesData> servicesData = new ArrayList<>();

        //test
        servicesData.add(new HomeServicesData(1, 1, 1, "osirandoust@gmail.com", "09369642754", "رستوران عقاب سبز",
                "همچون عقاب سیر کنید", "اسامه ایراندوست", "در خیابان های کردستان تنها قدم نزنید. رستوران عقاب شما را به دنیایی دیگر می برد. با عقاب سفر کنید",
                R.drawable.restaurant, "2 روز پیش", 4, "رستوران", "سنندج", "کردستان", 320, 850, 20,
                13, "سنندج، میدان اقبال", "open"));

        adapter = new CategoryServiceAdapter(getActivity(), servicesData);
        restaurantRecyclerView.setAdapter(adapter);
    }
    private void fastFoodRecyclerViewShow()
    {
        fastFoodLinearLayout.setVisibility(View.GONE);
    }
    private void cafeRecyclerViewShow()
    {
        cafeRecyclerView.setHasFixedSize(true);
        cafeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        ArrayList<HomeServicesData> servicesData = new ArrayList<>();

        //test
        servicesData.add(new HomeServicesData(2, 2, 1, "sogand.rje@gmail.com", "09118333033", "کافه لاماسیا",
                "با لاماسیا تا آسیا", "سوگند رجایی", "اگر بدنبال بهترین کافه گیلان هستید لاماسیا را از دست ندهید. همراه با بازی های جذاب و جایزه های فراوان و اینترنت رایگان",
                R.drawable.cafe, "5 روز پیش", 3.2, "کافه", "رشت", "گیلان", 256, 120, 2,
                18, "رشت، سبزه میدان، جنب کافه کتاب", "close_soon"));

        adapter = new CategoryServiceAdapter(getActivity(), servicesData);
        cafeRecyclerView.setAdapter(adapter);
    }
    private void hotelRecyclerViewShow()
    {
        hotelLinearLayout.setVisibility(View.GONE);
    }
    private void restRoomRecyclerViewShow()
    {
        restRoomLinearLayout.setVisibility(View.GONE);
    }
    private void shopCenterRecyclerViewShow()
    {
        shopCenterRecyclerView.setHasFixedSize(true);
        shopCenterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        ArrayList<HomeServicesData> servicesData = new ArrayList<>();

        //test
        servicesData.add(new HomeServicesData(4, 4, 1, "alirezafateminia@gmail.com", "09386513123", "مرکز خرید ولیعصر",
                "ولیعصر، برای تو", "علیرضا فاطمی نیا", "فروظگاه خرید ولیعصر لحظات خوبی را برای شما در نظر گرفته است. مفتخریم اعلام کنیم که فروشگاه های همواره تخفیف ولیعصر در تمامی حوزه ها متخصصین بالقوه ای را آماده خدمت رسانی به مشتریان عزیز نموده است.",
                R.drawable.shop_center, "چند دقیقه پیش", 0, "مراکز خرید", "رشت", "گیلان", 0, 23, 4,
                1, "رشت، کنار مجتمع بگیلان", "close"));

        servicesData.add(new HomeServicesData(3, 3, 1, "mmd.badi@gmail.com", "09116948828", "فروشگاه گیزمیز",
                "گیز میز ... خریدی لیز", "محمد بادزهره", "فروشگاه های خرید پوشاک همواره تخفیف گیزمیز که با بهترین کیفیت در استان گیلان مشغول به خدمت رسانی است",
                R.drawable.rest_room, "یک هفته پیش", 1.8, "مراکز خرید", "فومن", "گیلان", 565, 10, 6,
                77, "فومن، خیابان امام", "open_soon"));

        adapter = new CategoryServiceAdapter(getActivity(), servicesData);
        shopCenterRecyclerView.setAdapter(adapter);
    }

    //Liquid refresh layout
    private void liquidRefreshLayout()
    {
        int TIME_REFRESH = 3500;
        refreshLayout.setOnRefreshListener(new LiquidRefreshLayout.OnRefreshListener()
        {
            @Override
            public void completeRefresh()
            {
            }

            @Override
            public void refreshing()
            {
                new Handler().postDelayed(() -> refreshLayout.finishRefreshing(), TIME_REFRESH);
            }
        });
    }

}
