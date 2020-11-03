package ir.arapp.arappofficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ir.arapp.arappofficial.Adapter.CategoryItemAdapter;
import ir.arapp.arappofficial.Adapter.HomeServiceAdapter;
import ir.arapp.arappofficial.Adapter.MyServiceAdapter;
import ir.arapp.arappofficial.AppService.DrawerLocker;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.Data.CategoryItem;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServiceFragment extends Fragment
{
    //Variables
    private LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    private ArrayList<HomeServicesData> myServiceItems;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_service, container, false);

        //Hooks
        lottieAnimationView = view.findViewById(R.id.loadingMyService);
        recyclerView = view.findViewById(R.id.myServiceRecyclerView);

        //Get Category
        if (checkConnection())
        {
            myServiceItems = new ArrayList<>();
            setAdapter();
        }

        return view;
    }

    //check internet connection ...
    private boolean checkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity())
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    //Get category from database and set Adapter
    private void getItem()
    {
        //test
        myServiceItems.add(new HomeServicesData(4, 4, 1, "alirezafateminia@gmail.com", "09386513123", "مرکز خرید ولیعصر",
                "ولیعصر، برای تو", "علیرضا فاطمی نیا", "فروظگاه خرید ولیعصر لحظات خوبی را برای شما در نظر گرفته است. مفتخریم اعلام کنیم که فروشگاه های همواره تخفیف ولیعصر در تمامی حوزه ها متخصصین بالقوه ای را آماده خدمت رسانی به مشتریان عزیز نموده است.",
                R.drawable.shop_center, "چند دقیقه پیش", 0, "مراکز خرید", "رشت", "گیلان", 0, 23, 4,
                1, "رشت، کنار مجتمع بگیلان", "close"));

        myServiceItems.add(new HomeServicesData(1, 1, 1, "osirandoust@gmail.com", "09369642754", "رستوران عقاب سبز",
                "همچون عقاب سیر کنید", "اسامه ایراندوست", "در خیابان های کردستان تنها قدم نزنید. رستوران عقاب شما را به دنیایی دیگر می برد. با عقاب سفر کنید",
                R.drawable.restaurant, "2 روز پیش", 4, "رستوران", "سنندج", "کردستان", 320, 850, 20,
                13, "سنندج، میدان اقبال", "open"));

        myServiceItems.add(new HomeServicesData(2, 2, 1, "sogand.rje@gmail.com", "09118333033", "کافه لاماسیا",
                "با لاماسیا تا آسیا", "سوگند رجایی", "اگر بدنبال بهترین کافه گیلان هستید لاماسیا را از دست ندهید. همراه با بازی های جذاب و جایزه های فراوان و اینترنت رایگان",
                R.drawable.cafe, "5 روز پیش", 3.2, "کافه", "رشت", "گیلان", 256, 120, 2,
                18, "رشت، سبزه میدان، جنب کافه کتاب", "close_soon"));

        myServiceItems.add(new HomeServicesData(3, 3, 1, "mmd.badi@gmail.com", "09116948828", "فروشگاه گیزمیز",
                "گیز میز ... خریدی لیز", "محمد بادزهره", "فروشگاه های خرید پوشاک همواره تخفیف گیزمیز که با بهترین کیفیت در استان گیلان مشغول به خدمت رسانی است",
                R.drawable.rest_room, "یک هفته پیش", 1.8, "مراکز خرید", "فومن", "گیلان", 565, 10, 6,
                77, "فومن، خیابان امام", "open_soon"));

        myServiceItems.add(new HomeServicesData(3, 3, 1, "mmd.badi@gmail.com", "09116948828", "فروشگاه گیزمیز",
                "گیز میز ... خریدی لیز", "محمد بادزهره", "فروشگاه های خرید پوشاک همواره تخفیف گیزمیز که با بهترین کیفیت در استان گیلان مشغول به خدمت رسانی است",
                R.drawable.rest_room, "یک هفته پیش", 1.8, "مراکز خرید", "فومن", "گیلان", 565, 10, 6,
                77, "فومن، خیابان امام", "open_soon"));
    }
    private void setAdapter()
    {
        lottieAnimationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getItem();
        if (myServiceItems.size() == 0)
        {
            recyclerView.setVisibility(View.GONE);
        }
        adapter = new MyServiceAdapter(getContext(), myServiceItems);
        recyclerView.setAdapter(adapter);
    }

}

