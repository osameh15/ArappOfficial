package ir.arapp.arappofficial.Fragments;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Objects;

import ir.arapp.arappofficial.Adapter.SpecialServiceAdapter;
import ir.arapp.arappofficial.AppService.DrawerLocker;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class SpecialFragment extends Fragment
{

    //Variable
    private TextView offText;
    private TextView titleSpecial;
    private SpecialServiceAdapter adapter;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;
    Integer[] colors = null;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    ArrayList<HomeServicesData> specialData;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_special, container, false);

        //Hooks
        viewPager = view.findViewById(R.id.viewPagerSpecialService);
        offText = view.findViewById(R.id.text_reduction);
        relativeLayout = view.findViewById(R.id.offRelativeLayout);
        titleSpecial = view.findViewById(R.id.titleSpecialService);

        //Adapter and set special services ...
        specialData = new ArrayList<>();
        specialDataList();
        setAdapter();

        //Drawer layout
        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(true);

        return view;
    }

    //To hide toolbar
    @Override
    public void onResume()
    {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
    }

    //Destroy fragment
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(false);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();
    }

    //Adapter of recycler View and show data
    private void specialDataList()
    {
        //test
        specialData.add(new HomeServicesData(4, 4, 1, "alirezafateminia@gmail.com", "09386513123", "مرکز خرید ولیعصر",
                "ولیعصر، برای تو", "علیرضا فاطمی نیا", "فروظگاه خرید ولیعصر لحظات خوبی را برای شما در نظر گرفته است. مفتخریم اعلام کنیم که فروشگاه های همواره تخفیف ولیعصر در تمامی حوزه ها متخصصین بالقوه ای را آماده خدمت رسانی به مشتریان عزیز نموده است.",
                R.drawable.shop_center, "چند دقیقه پیش", 0, "مراکز خرید", "رشت", "گیلان", 0, 23, 4,
                10, "رشت، کنار مجتمع بگیلان", "close"));

        specialData.add(new HomeServicesData(1, 1, 1, "osirandoust@gmail.com", "09369642754", "رستوران عقاب سبز",
                "همچون عقاب سیر کنید", "اسامه ایراندوست", "در خیابان های کردستان تنها قدم نزنید. رستوران عقاب شما را به دنیایی دیگر می برد. با عقاب سفر کنید",
                R.drawable.restaurant, "2 روز پیش", 4, "رستوران", "سنندج", "کردستان", 320, 850, 20,
                18, "سنندج، میدان اقبال", "open"));

        specialData.add(new HomeServicesData(2, 2, 1, "sogand.rje@gmail.com", "09118333033", "کافه لاماسیا",
                "با لاماسیا تا آسیا", "سوگند رجایی", "اگر بدنبال بهترین کافه گیلان هستید لاماسیا را از دست ندهید. همراه با بازی های جذاب و جایزه های فراوان و اینترنت رایگان",
                R.drawable.cafe, "5 روز پیش", 3.2, "کافه", "رشت", "گیلان", 256, 120, 2,
                23, "رشت، سبزه میدان، جنب کافه کتاب", "close_soon"));

        specialData.add(new HomeServicesData(3, 3, 1, "mmd.badi@gmail.com", "09116948828", "فروشگاه گیزمیز",
                "گیز میز ... خریدی لیز", "محمد بادزهره", "فروشگاه های خرید پوشاک همواره تخفیف گیزمیز که با بهترین کیفیت در استان گیلان مشغول به خدمت رسانی است",
                R.drawable.rest_room, "یک هفته پیش", 1.8, "مراکز خرید", "فومن", "گیلان", 565, 10, 6,
                28, "فومن، خیابان امام", "open_soon"));
    }
    private void setAdapter()
    {
        adapter = new SpecialServiceAdapter(getContext(), specialData);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(110, 0, 110, 0);
        viewPager.setPageMargin(10);
        setMaterialDesign();
    }
    private void setMaterialDesign()
    {
        Integer[] colors_tmp =
                {
                        getResources().getColor(R.color.shade4),
                        getResources().getColor(R.color.compound3),
                        getResources().getColor(R.color.text5),
                        getResources().getColor(R.color.compound2)
                };
        colors = colors_tmp;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                if (position < (adapter.getCount()-1) && position < (colors.length -1))
                {
                    relativeLayout.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position ], colors[position+1]));
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position ], colors[position+1]));
                    offText.setText(specialData.get(position).getAllLikes() + "%");
                    titleSpecial.setText(specialData.get(position).getTitle());
                    offText.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left));
                    titleSpecial.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right));
                }
                else
                {
                    viewPager.setBackgroundColor(colors[colors.length-1]);
                    relativeLayout.setBackgroundColor(colors[colors.length-1]);
                    offText.setText(specialData.get(specialData.size()-1).getAllLikes() + "%");
                    titleSpecial.setText(specialData.get(specialData.size()-1).getTitle());
                }
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }
}
