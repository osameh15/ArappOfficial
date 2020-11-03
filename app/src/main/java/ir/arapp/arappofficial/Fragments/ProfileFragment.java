package ir.arapp.arappofficial.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.muddzdev.styleabletoast.StyleableToast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import ir.arapp.arappofficial.Activities.LoginActivity;
import ir.arapp.arappofficial.Activities.ProfileActivity;
import ir.arapp.arappofficial.AppService.DrawerLocker;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.AppService.SessionManager;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements View.OnClickListener
{
    //Variables
    private static final int PICK_IMAGE_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private CardView editProfile;
    private CardView addService;
    private CardView myService;
    private CardView bookingService;
    private CardView showComments;
    private CardView showRating;
    private CardView clearSearch;
    private CardView logout;
    private CardView changePhoto;
    private LinearLayout cardViewLayout;
    private RoundedImageView profilePicture;
    private TextView name;
    private TextView city;
    private TextView created_at;
    private TextView serviceType;
    private Bitmap bitmap;
    private LottieAnimationView loading;
    //Session Manager
    private SessionManager sessionManager;
    private String phone;
    private String serviceProvider;

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        //Hooks
        editProfile = view.findViewById(R.id.editProfile);
        addService = view.findViewById(R.id.addService);
        myService = view.findViewById(R.id.myService);
        bookingService = view.findViewById(R.id.booking);
        showComments = view.findViewById(R.id.userComment);
        showRating = view.findViewById(R.id.serviceRating);
        clearSearch = view.findViewById(R.id.searchHistory);
        logout = view.findViewById(R.id.logOut);
        changePhoto = view.findViewById(R.id.changePhotoProfile);
        profilePicture = view.findViewById(R.id.profilePicture);
        name = view.findViewById(R.id.profileName);
        city = view.findViewById(R.id.cityProfile);
        created_at = view.findViewById(R.id.created_at);
        serviceType = view.findViewById(R.id.service_type);
        cardViewLayout = view.findViewById(R.id.cardViewLayoutProfile);
        loading = view.findViewById(R.id.loadingPictureProfile);
        //Session Manager
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()).getApplicationContext());
        phone = sessionManager.getUserPhone();
        serviceProvider = sessionManager.getServiceProvider();

        //Service application divider ...
        serviceDivider();

        //OnClick()
        editProfile.setOnClickListener(this);
        addService.setOnClickListener(this);
        myService.setOnClickListener(this);
        bookingService.setOnClickListener(this);
        showComments.setOnClickListener(this);
        showRating.setOnClickListener(this);
        clearSearch.setOnClickListener(this);
        changePhoto.setOnClickListener(this);
        logout.setOnClickListener(this);

        //Drawer layout
        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(true);

        //Get Data...
        getData();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //To hide toolbar
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

    //Service application divider ...
    private void serviceDivider()
    {
        if (serviceProvider.equals("سرویس گیرنده"))
        {
            addService.setVisibility(View.GONE);
            myService.setVisibility(View.GONE);
            showComments.setVisibility(View.GONE);
            showRating.setVisibility(View.GONE);
        }
    }

    //Get and Set Data from server
    private void getData()
    {
        if (checkConnection())
        {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getUserData(phone);

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
                            JSONObject jsonObject = new JSONObject(responseBody);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error)
                            {
                                String name = jsonObject.getString("name");
                                String province = jsonObject.getString("province");
                                String city = jsonObject.getString("city");
                                String date = jsonObject.getString("created_at");
                                String image = jsonObject.getString("avatar");
                                setData(name, province, city, date);
                                loadImage(image);
                            }
                            else
                            {
                                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                        "خطا در برقراری ارتباط!", Toast.LENGTH_LONG, R.style.toastTheme).show();
                            }
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
                    StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                            t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
                }
            });
        }
    }
    @SuppressLint("SetTextI18n")
    private void setData(String nameText, String province, String cityText, String date)
    {
        name.setText(nameText);
        if (cityText.equals("null"))
        {
            if (province.equals("null"))
            {
                city.setText("اطلاعات کاربری خود را ویرایش کنید");
                city.setTextColor(getResources().getColor(R.color.compound5));
                city.setTextSize(12);
            }
            else
            {
                city.setText(province);
            }
        }
        else
        {
            city.setText(province + "/" + cityText);
        }
        created_at.setText("تاریخ ایجاد حساب کاربری:  " + date);
        serviceType.setText(serviceProvider);
    }

    @Override
    //onClick method
    public void onClick(View view)
    {
        Intent profileActivity = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), ProfileActivity.class);
        switch (view.getId())
        {
            case R.id.editProfile:
                profileActivity.putExtra("name", "editProfile");
                startActivity(profileActivity);
                break;
            case R.id.addService:
                profileActivity.putExtra("name", "addService");
                startActivity(profileActivity);
                break;
            case R.id.myService:
                profileActivity.putExtra("name", "myService");
                startActivity(profileActivity);
                break;
            case R.id.booking:
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "رزرو سرویس"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                break;
            case R.id.userComment:
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "مشاهده نظرات"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                break;
            case R.id.serviceRating:
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "امتیازات کاربران"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                break;
            case R.id.searchHistory:
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "پاک کردن تاریخچه جستجو"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                break;
            case R.id.logOut:
                sessionManager.setLogin(false);
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "با موفقیت از حساب خود خارج شدید"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                Intent login = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), LoginActivity.class);
                startActivity(login);
                getActivity().finish();
                break;
            case R.id.changePhotoProfile:
                checkPermission();
                break;
            default:
                break;
        }
    }

    //Upload Image... Load Data and check Permission
    //Function to permission for access to read external storage
    private void checkPermission()
    {
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                //permission not generated
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else
            {
                //permission already generated
                selectImage();
            }
        }
        else
        {
            //System os is less than marshmallow
            selectImage();
        }
    }
    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //permission was generated
                selectImage();
            }
            else
            {
                //permission was denied
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "اجازه دسترسی به کارت حافظه داده نشد!"
                        , Toast.LENGTH_SHORT, R.style.toastTheme).show();
            }
        }
    }
    //Function to select an image from gallery
    private void selectImage()
    {
        //intent to pick image
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "تصویر کاربری خود را انتخاب کنید"), PICK_IMAGE_CODE);
    }
    //handle result of access to gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null)
        {
            //set image to imageView
            Uri imageUri = Objects.requireNonNull(data).getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    //Function to convert image to string
    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
    private void uploadImage()
    {
        loading.setVisibility(View.VISIBLE);
        cardViewLayout.setVisibility(View.GONE);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .uploadProfilePicture(phone, imageToString(bitmap));

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
                        JSONObject jsonObject = new JSONObject(responseBody);
                        boolean error = jsonObject.getBoolean("error");
                        String message = jsonObject.getString("error_msg");
                        if (!error)
                        {
                            cardViewLayout.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                    message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        }
                        else
                        {
                            cardViewLayout.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                                    message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        }
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
                cardViewLayout.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        "خطا در اتصال به سرور", Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        });
    }
    private void loadImage(String image)
    {
        Glide
                .with(Objects.requireNonNull(getContext()))
                .load(image)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .override(500, 500)
                .into(profilePicture);
    }
}
