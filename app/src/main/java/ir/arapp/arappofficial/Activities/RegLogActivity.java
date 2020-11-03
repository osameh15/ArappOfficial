package ir.arapp.arappofficial.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;
import java.util.Objects;

import ir.arapp.arappofficial.AppService.SessionManager;
import ir.arapp.arappofficial.R;

public class RegLogActivity extends AppCompatActivity
{
    //Variables ...
    private static final String TAG = RegLogActivity.class.getSimpleName();
    private static final int PERMISSION_CODE = 1001;
    //Session Manager
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_log);

        //Set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Session Manager
        sessionManager = new SessionManager(this);

        //Check permissions
        checkPermission();

        //check user log in
        getCheckUserLogIn();
    }

    //check user log in or log out
    private void getCheckUserLogIn()
    {
        if (sessionManager.isLoggedIn())
        {
            Intent homeActivity = new Intent(RegLogActivity.this, HomeActivity.class);
            startActivity(homeActivity);
            finish();
        }
    }

    //Function to permission for access to read external storage
    private void checkPermission()
    {
        //check runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            {
                //permission not generated
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
                //show popup for runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
        }
    }
    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_CODE)
        {
            if (grantResults.length > 0)
            {
                boolean storagePermission = grantResults[0] != PackageManager.PERMISSION_GRANTED;
                boolean locationPermission = grantResults[1] != PackageManager.PERMISSION_GRANTED;
                if (storagePermission && locationPermission)
                {
                    //permission was denied
                    StyleableToast.makeText(getApplicationContext(), "اجازه دسترسی به هیچ یک داده نشد! برای استفاده از تمامی امکانات برنامه اجازه دسترسی به موارد فوق را صادر کنید!"
                            , Toast.LENGTH_LONG, R.style.toastTheme).show();
                }
                if (storagePermission)
                {
                    //permission was denied
                    StyleableToast.makeText(getApplicationContext(), "اجازه دسترسی به کارت حافظه داده نشد!"
                            , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                }
                if (locationPermission)
                {
                    //permission was denied
                    StyleableToast.makeText(getApplicationContext(), "اجازه دسترسی به موقعیت مکانی داده نشد!"
                            , Toast.LENGTH_SHORT, R.style.toastTheme).show();
                }
            }
        }
    }

    //Register activity ...
    public void registerButton(View view)
    {
        Intent phoneNumber = new Intent(RegLogActivity.this, RegisterActivity.class);
        startActivity(phoneNumber);
    }

    //Login activity ...
    public void loginButton(View view)
    {
        Intent loginActivity = new Intent(RegLogActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
}
