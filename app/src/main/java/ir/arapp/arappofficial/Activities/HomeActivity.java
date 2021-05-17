package ir.arapp.arappofficial.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import ir.arapp.arappofficial.AppService.DrawerLocker;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.AppService.SessionManager;
import ir.arapp.arappofficial.Data.UserData;
import ir.arapp.arappofficial.Fragments.CategoryFragment;
import ir.arapp.arappofficial.Fragments.HomeFragment;
import ir.arapp.arappofficial.Fragments.NotificationFragment;
import ir.arapp.arappofficial.Fragments.ProfileFragment;
import ir.arapp.arappofficial.Fragments.SpecialFragment;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {
    //Variable
    private static final String TAG = HomeActivity.class.getSimpleName();
    static final float END_SCALE = 0.7f;
    private static final int TIME_INTERVAL = 2000;
    private boolean doubleBackToExitPressed = false;
    ImageView menuImage;
    ImageView searchView;
    FrameLayout contentView;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    private String phone;
    private String versionName;
    //Handler and timer to back pressed
    private Handler handler = new Handler();
    private final Runnable runnable = () ->
    {
        doubleBackToExitPressed = false;
    };
    //Drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //Session Manager
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
        menuImage = findViewById(R.id.nav_openDrawer);
        searchView = findViewById(R.id.nav_SearchView);
        contentView = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.homeToolbar);
        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        //Session Manager
        sessionManager = new SessionManager(this);
        phone = sessionManager.getUserPhone();

        //Toolbar
        setSupportActionBar(toolbar);

        //Navigation drawer function
        navigationDrawer();

        //Get data
        getData();
        setNotifyChecked();

        //Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setItemIconTintList(null);

        //search view
        searchViewMethod();

        //Check recently changed dialog only first time in home screen ...
        versionName = sessionManager.getVersionName(this, HomeActivity.class);
        checkIsFirstRun();
    }

    //check internet connection ...
    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != networkInfo) {
            return true;
        } else {
            StyleableToast.makeText(getApplicationContext(), "عدم اتصال به اینترنت!", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return false;
        }
    }

    //OnDestroy method
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

    //Search menu function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //        getMenuInflater().inflate(R.menu.toolbar_search_menu, menu);
        //        MenuItem menuItem = menu.findItem(R.id.action_search);

        //        SearchView searchView = (SearchView) menuItem.getActionView();
        //
        //        //search view plate
        //        View searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        //        searchPlate.setBackgroundResource(R.drawable.search_bg);
        //
        //        //search view edit text
        //        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        //        searchEditText.setHint(getResources().getString(R.string.search));
        //        Typeface type = ResourcesCompat.getFont(getApplicationContext(), R.font.vazir);
        //        searchEditText.setTypeface(type);
        //        searchEditText.setHintTextColor(getResources().getColor(R.color.disable));
        //        searchEditText.setTextColor(getResources().getColor(R.color.colorAccentDark));
        //        searchEditText.setTextSize(14);
        //
        //        //search view close button
        //        ImageView closeButtonImage = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        //        closeButtonImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccentDark), PorterDuff.Mode.SRC_IN);
        //
        return super.onCreateOptionsMenu(menu);
    }

    //Check recently changed dialog only first time in home screen ...
    private void checkIsFirstRun() {
        String storedVersionName;
        storedVersionName = sessionManager.getStoredVersionName();

        if (storedVersionName.equals("")) {
            sessionManager.setStoredVersionName(versionName);
            return;
        } else if (!storedVersionName.equals(versionName)) {
            showRecentlyChangeDialog();
        }
        sessionManager.setStoredVersionName(versionName);
    }

    //Navigation drawer methods
    @Override
    public void onBackPressed() {
        //check drawer layout open or close
        if (drawerLayout.isDrawerVisible(GravityCompat.END))
        {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else
            {
            //if user clicking twice on back button finish activity ...
            if (doubleBackToExitPressed)
            {
                super.onBackPressed();
                finishAffinity();
            }
            this.doubleBackToExitPressed = true;
            StyleableToast.makeText(getApplicationContext(), "لطفا کلید بازگشت را مجددا فشار دهید!", Toast.LENGTH_LONG, R.style.toastTheme).show();
            handler.postDelayed(runnable, TIME_INTERVAL);
        }
    }

    private void navigationDrawer() {
        //Navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        menuImage.setOnClickListener(v ->
        {
            if (drawerLayout.isDrawerVisible(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        animateNavigateDrawer();
    }

    private void animateNavigateDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffsetDiff - xOffset;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_faves:
                StyleableToast.makeText(getApplicationContext(), "علاقه مندی ها", Toast.LENGTH_LONG, R.style.toastTheme).show();
                break;
            case R.id.nav_newsReceive:
                break;
            case R.id.inviteFriends:
                shareApp();
                break;
            case R.id.nav_rating:
                StyleableToast.makeText(getApplicationContext(), "از اپلیکیشن آراپ حمایت کنید!", Toast.LENGTH_LONG, R.style.toastTheme).show();
                break;
            case R.id.nav_recentUpdates:
                showRecentlyChangeDialog();
                break;
            case R.id.nav_aboutUs:
                showAboutUsDialog();
                break;
            case R.id.nav_contactUS:
                showContactUsDialog();
                break;
            case R.id.nav_question:
                StyleableToast.makeText(getApplicationContext(), "سؤالات متداول", Toast.LENGTH_LONG, R.style.toastTheme).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void setDrawerLocked(boolean shouldLock) {
        if (shouldLock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    private void notificationCheckBox(int notify) {
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_newsReceive);
        CompoundButton compoundButton = (CompoundButton) menuItem.getActionView();
        if (notify == 1) {
            compoundButton.setChecked(true);
        } else {
            compoundButton.setChecked(false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void headerDrawer(String name, String province, String city, int notify, String image) {
        View view = navigationView.getHeaderView(0);
        TextView nameText = view.findViewById(R.id.nav_profileName);
        TextView cityText = view.findViewById(R.id.nav_city);
        ImageView imageView = view.findViewById(R.id.nav_profilePicture);

        nameText.setText(name);
        cityText.setText(phone);
        //Notification receiver_ get from database
        notificationCheckBox(notify);
        //Load Image
        loadProfileImage(image, imageView);
    }

    private void loadProfileImage(String image, ImageView imageView) {
        Glide
                .with(this)
                .load(image)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .override(500, 500)
                .into(imageView);
    }

    //Bottom Navigation functions
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuItem ->
            {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_profile:
                        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(3).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(4).setEnabled(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                        break;
                    case R.id.bottom_category:
                        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(3).setEnabled(false);
                        bottomNavigationView.getMenu().getItem(4).setEnabled(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoryFragment()).commit();
                        break;
                    case R.id.bottom_home:
                        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
                        bottomNavigationView.getMenu().getItem(3).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(4).setEnabled(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;
                    case R.id.bottom_notification:
                        bottomNavigationView.getMenu().getItem(0).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
                        bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(3).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(4).setEnabled(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
                        break;
                    case R.id.bottom_special:
                        bottomNavigationView.getMenu().getItem(0).setEnabled(false);
                        bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(3).setEnabled(true);
                        bottomNavigationView.getMenu().getItem(4).setEnabled(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecialFragment()).commit();
                        break;
                }
                return true;
            };

    //Get Data from server
    private void getData() {
        if (checkConnection()) {
            Call<UserData> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getUserData1(phone);

            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    if (response.isSuccessful()) {
                        UserData ud = response.body();
                        headerDrawer(ud.getName(), ud.getProvince(), ud.getCity(), ud.getNotification_receiver(), ud.getAvatar());
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    StyleableToast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
                }
            });
        }
    }

    //Set notification to 1 (receive) and 0 (don't receive)
    private void setNotifyChecked() {
        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_newsReceive);
        CompoundButton compoundButton = (CompoundButton) menuItem.getActionView();
        compoundButton.setOnClickListener(v ->
        {
            if (compoundButton.isChecked()) {
                setNotification(1);
            } else {
                setNotification(0);
            }
        });
    }

    private void setNotification(int notification) {
        if (checkConnection()) {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .setNotificationReceiver(phone, notification);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = Objects.requireNonNull(response.body()).string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("error_msg");
                            if (!error) {
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                            } else {
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    StyleableToast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
                }
            });
        }
    }

    //search view
    private void searchViewMethod() {
        searchView.setOnClickListener(view ->
        {
            Intent searchActivity = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(searchActivity);
        });
    }

    //show pop up dialogs
    //recently change
    private void showRecentlyChangeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_recently_change);
        ImageView close = dialog.findViewById(R.id.close_popup_recently);
        TextView textView = dialog.findViewById(R.id.recentlyChangesText);
        close.setOnClickListener(v -> dialog.dismiss());
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(getResources().getString(R.string.update));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //about us
    private void showAboutUsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_about_us);
        ImageView close = dialog.findViewById(R.id.close_popup_info);
        close.setOnClickListener(v -> dialog.dismiss());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //Contact us
    private void showContactUsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_popup_contact_us);
        ImageView close = dialog.findViewById(R.id.close_popup_contact);
        Button callButton = dialog.findViewById(R.id.call);
        Button mailButton = dialog.findViewById(R.id.mail);
        close.setOnClickListener(v -> dialog.dismiss());
        callButton.setOnClickListener(v ->
        {
            Uri number = Uri.parse("tel:+989112834604");
            Intent intent = new Intent(Intent.ACTION_DIAL, number);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
        mailButton.setOnClickListener(v ->
        {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"Sajjad.Haghzad@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "گزارش مشکلات/ارتباط با ما");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            try {
                startActivity(Intent.createChooser(emailIntent, "ارسال ایمیل از طریق:"));
            } catch (android.content.ActivityNotFoundException ex) {
                StyleableToast.makeText(getApplicationContext(), "هیچ کلاینتی برای ارسال ایمیل پیدا نشد",
                        Toast.LENGTH_SHORT, R.style.toastTheme).show();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //Share App function
    private void shareApp() {
        String message = "لینک دانلود آراپ از بازار:";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("text/plain");

        Intent chooser = Intent.createChooser(intent, "اشتراک گذاری از طریق:");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}
