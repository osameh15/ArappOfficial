package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.arapp.arappofficial.Adapter.CommentAdapter;
import ir.arapp.arappofficial.Data.CommentData;
import ir.arapp.arappofficial.R;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener
{

    //Variables
    //Toolbar
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private FrameLayout rateFrameLayout;
    private TextView titleTextView;
    private boolean isHideToolbarView = false;
    //TimeActivity
    private ImageView helpActivity;
    private Dialog dialog;
    private TextView timeActivity;
    //Detail
    private RoundedImageView roundedImageView;
    private TextView categoryTextView;
    private TextView subtitleTextView;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView provinceTextView;
    private TextView addressTextView;
    private TextView timeTextView;
    private TextView bioTextView;
    private Bundle bundle;
    //Rate
    private TextView rateService;
    private TextView rateAll;
    //Map

    //Comment
    private ArrayList<CommentData> commentData;
    private RecyclerView commentRecyclerView;
    private RecyclerView.Adapter commentAdapter;
    private TextView commentNumber ;
    private TextView commentEmpty ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Hooks
        toolbar = findViewById(R.id.detailToolbar);
        appBarLayout = findViewById(R.id.appbarDetail);
        titleTextView = findViewById(R.id.serviceToolbarName);
        rateFrameLayout = findViewById(R.id.rateDetail);
        helpActivity = findViewById(R.id.helpServiceDetail);
        roundedImageView = findViewById(R.id.detailImage);
        timeActivity = findViewById(R.id.activityServiceDetail);
        categoryTextView = findViewById(R.id.categoryServiceDetail);
        subtitleTextView = findViewById(R.id.subtitleServiceDetail);
        nameTextView = findViewById(R.id.nameServiceDetail);
        phoneTextView = findViewById(R.id.phoneServiceDetail);
        provinceTextView = findViewById(R.id.provinceServiceDetail);
        addressTextView = findViewById(R.id.addressServiceDetail);
        timeTextView = findViewById(R.id.timeServiceDetail);
        bioTextView = findViewById(R.id.textServiceDetail);
        rateService = findViewById(R.id.rateServiceItem);
        rateAll = findViewById(R.id.rateAllServiceItem);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        commentNumber = findViewById(R.id.commentTotalDetail);
        commentEmpty = findViewById(R.id.emptyCommentDetail);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarDetail);

        //Toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle("");
        appBarLayout.addOnOffsetChangedListener(this);

        //Bundle and get Data and detail of services
        bundle = getIntent().getExtras();

        setData();

        //Dialog
        dialog = new Dialog(this);

        //Comment Recycler View
        ViewCompat.setNestedScrollingEnabled(commentRecyclerView, false);
        setCommentAdapter();
    }

    //back Button method ...
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i)
    {
        int maxScroll =appBarLayout.getTotalScrollRange();
        float percentage =(float) Math.abs(i) / (float)maxScroll;

        if (percentage == 1f && isHideToolbarView)
        {
            rateFrameLayout.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
        else if (percentage < 1f && !isHideToolbarView)
        {
            rateFrameLayout.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    //Set data in text view and other objects
    @SuppressLint("SetTextI18n")
    private void setData()
    {
        if (bundle != null)
        {
            roundedImageView.setImageResource(bundle.getInt("image"));
            titleTextView.setText(bundle.getString("title"));
            categoryTextView.setText( bundle.getString("category"));
            subtitleTextView.setText(bundle.getString("intro"));
            nameTextView.setText(bundle.getString("name"));
            phoneTextView.setText(bundle.getString("phone"));
            provinceTextView.setText(bundle.getString("province") + " / " + bundle.getString("city"));
            addressTextView.setText(bundle.getString("address"));
            timeTextView.setText(bundle.getString("date"));
            bioTextView.setText(bundle.getString("text"));

            //Rate
            if (bundle.getDouble("rate") == 0 || bundle.getInt("rateCount") == 0)
            {
                rateService.setText("-");
                rateAll.setText("(بدون رأی)");
                rateFrameLayout.setBackgroundResource(R.drawable.rate_zero_shape);
            }
            else if (bundle.getDouble("rate") < 2)
            {
                rateService.setText(bundle.getDouble("rate") + "");
                rateAll.setText("(از " + bundle.getInt("rateCount") + " رأی)");
                rateFrameLayout.setBackgroundResource(R.drawable.rate_min_shape);
            }
            else if (bundle.getDouble("rate") < 4)
            {
                rateService.setText(bundle.getDouble("rate") + "");
                rateAll.setText("(از " + bundle.getInt("rateCount") + " رأی)");
                rateFrameLayout.setBackgroundResource(R.drawable.rate_normal_shape);
            }
            else if (bundle.getDouble("rate") <= 5)
            {
                rateService.setText(bundle.getDouble("rate") + "");
                rateAll.setText("(از " + bundle.getInt("rateCount") + " رأی)");
                rateFrameLayout.setBackgroundResource(R.drawable.rate_max_shape);
            }

            //Time activity
            if (Objects.equals(bundle.getString("specialText"), "open"))
            {
                timeActivity.setText(getResources().getString(R.string.open_service));
                timeActivity.setTextColor(getResources().getColor(R.color.open_service));
            }
            else if (Objects.equals(bundle.getString("specialText"), "close"))
            {
                timeActivity.setText(getResources().getString(R.string.close_service));
                timeActivity.setTextColor(getResources().getColor(R.color.close_service));
            }
            else if (Objects.equals(bundle.getString("specialText"), "open_soon"))
            {
                timeActivity.setText(getResources().getString(R.string.open_service_soon));
                timeActivity.setTextColor(getResources().getColor(R.color.soon_service));
            }
            else if (Objects.equals(bundle.getString("specialText"), "close_soon"))
            {
                timeActivity.setText(getResources().getString(R.string.close_service_soon));
                timeActivity.setTextColor(getResources().getColor(R.color.soon_service));
            }
        }
    }

    //Show dialog time activity
    public void helpActivity(View view)
    {
        dialog.setContentView(R.layout.custom_popup_activity);
        CircularProgressButton circularProgressButton = dialog.findViewById(R.id.close_popup_activity);
        circularProgressButton.setOnClickListener(view1 ->  dialog.dismiss());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //Rate Service
    @SuppressLint("SetTextI18n")
    public void rateService(View view)
    {
        int TIME_LOADING = 2200;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialogTheme);
        @SuppressLint("InflateParams") View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_rate_layout, null);
        //View Hooks
        CircularProgressButton submitButton = bottomSheetView.findViewById(R.id.close_bottom_sheet_rate);
        RatingBar ratingBar = bottomSheetView.findViewById(R.id.ratingBar);
        TextView textView = bottomSheetView.findViewById(R.id.rateText);
        SpinKitView spinKitView = bottomSheetView.findViewById(R.id.loadingRatingBottomSheet);

        submitButton.setOnClickListener(view1 ->
        {
            submitButton.setVisibility(View.GONE);
            spinKitView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(bottomSheetDialog::dismiss,TIME_LOADING);
        });
        ratingBar.setOnRatingBarChangeListener((ratingBar1, v, b) ->
        {
            textView.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);
            textView.setText("امتیاز شما: "+ v);
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    //Comment service and submit it!
    public void commentService(View view)
    {
        int TIME_LOADING = 2200;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottomSheetDialogTheme);
        @SuppressLint("InflateParams") View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_comment_layout, null);
        //View hooks
        CircularProgressButton circularProgressButton = bottomSheetView.findViewById(R.id.close_bottom_sheet_comment);
        TextInputLayout textInputLayout = bottomSheetView.findViewById(R.id.textCommentBottomSheet);
        SpinKitView spinKitView = bottomSheetView.findViewById(R.id.loadingCommentBottomSheet);

        if (Objects.requireNonNull(textInputLayout.getEditText()).length() == 0)
        {
            circularProgressButton.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
        }

        Objects.requireNonNull(textInputLayout.getEditText()).addTextChangedListener
                (
                new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.length() >= 3)
                {
                    circularProgressButton.setText(R.string.confirm);
                    circularProgressButton.setBackgroundResource(R.drawable.rate_max_shape);
                    circularProgressButton.setOnClickListener(view12 ->
                    {
                        circularProgressButton.setVisibility(View.GONE);
                        spinKitView.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(bottomSheetDialog::dismiss, TIME_LOADING);
                    });
                }
                else
                {
                    circularProgressButton.setText(R.string.close);
                    circularProgressButton.setBackgroundResource(R.drawable.rate_min_shape);
                    circularProgressButton.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
                }
            }
            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        }
            );

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    //Phone number of service provider and call to it...
    public void callService(View view)
    {
        Uri uri = Uri.parse("tel:" + bundle.getString("phone"));
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    //Fetch comment data and set adapter ...
    private void getCommentData()
    {
        commentData = new ArrayList<>();

        //test
        commentData.add(new CommentData(1, 1, 1, 1, "osirandoust@gmail.com", "09369642754",
                "اسامه ایراندوست", "عالی بود دم شما گرم", R.drawable.shop_center, "سه ساعت پیش", 4.5));
        commentData.add(new CommentData(2, 2, 1, 2, "sogand.rje@gmail.com", "09118333033",
                "سوگند رجایی", "واقعا افتضاح!", R.drawable.cafe, "دو روز پیش", 2.1));
        commentData.add(new CommentData(3, 3, 1, 3, "osirandoust@gmail.com", "09116948828",
                "محمد بادزهره", "من که کامل راضی بودم. امیدوارم همیشه موفق باشید و سرویس های بهتری ارائه نمایید. در حال حاضر بنظرم یکی از بهترین سرویس های ارائه شده شما هستید... به امید موفقیت بیشتر و روز افزون برای شما", R.drawable.restaurant, "یک هفته پیش", 5));
        commentData.add(new CommentData(4, 4, 1, 3, "osirandoust@gmail.com", "09386513123",
                "علیرضا فاطمی نیا", "بدون شرح، متوسط رو به بالا", R.drawable.rest_room, "دو ماه پیش", 3.6));

    }
    @SuppressLint("SetTextI18n")
    private void setCommentAdapter()
    {
        getCommentData();

        if (commentData.size() != 0)
        {
            commentEmpty.setVisibility(View.GONE);
            commentRecyclerView.setVisibility(View.VISIBLE);
            commentNumber.setText(commentData.size() + " نظر");
        }

        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
        commentAdapter = new CommentAdapter(this, commentData);
        commentRecyclerView.setAdapter(commentAdapter);
    }
}