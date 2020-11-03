package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.Objects;

import ir.arapp.arappofficial.Adapter.ServiceItemAdapter;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class ServiceActivity extends AppCompatActivity
{

    //Variables
    Toolbar toolbar;
    private Bundle bundle;
    private RecyclerView serviceRecyclerView;
    private RecyclerView.Adapter adapter;
    private TextView toolbarName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //Hooks
        toolbar = findViewById(R.id.serviceToolbar);
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView);
        toolbarName = findViewById(R.id.serviceToolbarName);

        //Toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bundle (get Extra and data from anther activity)
        bundle = getIntent().getExtras();
        if (bundle != null)
        {
            toolbarName.setText(bundle.getString("name"));
        }

        //RecyclerView
        if (checkConnection())
        {
            serviceItemShow();
        }
    }

    //check internet connection ...
    private boolean checkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != networkInfo)
        {
            return true;
        }
        else
        {
            StyleableToast.makeText(getApplicationContext(), "عدم اتصال به اینترنت!", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return false;
        }
    }

    //Back presses
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    //RecyclerView
    private void serviceItemShow()
    {
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

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

        adapter = new ServiceItemAdapter(servicesData, this);
        serviceRecyclerView.setAdapter(adapter);
    }
}