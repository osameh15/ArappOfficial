package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.AppService.SessionManager;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{

    //Variables
    private static final String TAG = LoginActivity.class.getSimpleName();
    TextInputLayout phoneEditText;
    TextInputLayout passwordEditText;
    CircularProgressButton loginButton;
    CheckBox rememberMe;
    //Session Manager
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        phoneEditText = findViewById(R.id.userLogin);
        passwordEditText = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        rememberMe = findViewById(R.id.rememberMe);

        //Session Manager
        sessionManager = new SessionManager(this);

        //Set background drawable for login button...
        loginButton.setBackgroundResource(R.drawable.button_shape);
    }

    //Validate methods...
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
    //validate phone method ...
    private boolean validatePhone()
    {
        String phone = Objects.requireNonNull(phoneEditText.getEditText()).getText().toString();

        if (phone.isEmpty())
        {
            StyleableToast.makeText(getApplicationContext(), "شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return false;
        }
        else
        {
            return true;
        }
    }
    private boolean validatePassword()
    {
        String phone = Objects.requireNonNull(passwordEditText.getEditText()).getText().toString();

        if (phone.isEmpty())
        {
            StyleableToast.makeText(getApplicationContext(), "رمز عبور خود را وارد نکرده اید!", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    //method for sign up button ...
    public void signUp(View view)
    {
        Intent signUpActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(signUpActivity);
    }
    //method for forget password ...
    public void forgetPassword(View view)
    {
        Intent forgetPasswordActivity = new Intent(LoginActivity.this, ForgetPassword.class);
        startActivity(forgetPasswordActivity);
    }

    //Login methods ...
    //method for login button ...
    public void login(View view)
    {
        if (checkConnection() && validatePhone() & validatePassword())
        {
            String phone = Objects.requireNonNull(phoneEditText.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordEditText.getEditText()).getText().toString();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .loginApp(phone, password);

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
                                final String serviceType = jsonObject.getString("service");
                                sessionManager.setUserPhone(phone);
                                sessionManager.setServiceProvider(serviceType);
                                loginLoading(message, true);
                            }
                            else
                            {
                                loginLoading(message, false);
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
                    StyleableToast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
                }
            });
        }
    }
    //button animation for loading and go to home activity ...
    private void loginLoading(String message, boolean flag)
    {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loginAnimation = new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... strings)
            {
                try
                {
                    Thread.sleep(900);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return "done";
            }

            @Override
            protected void onPostExecute(String s)
            {
                if(s.equals("done"))
                {
                    if (flag)
                    {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        loginButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        (new Handler()).postDelayed(this::homeActivity, 500);
                    }
                    else
                    {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        loginButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        (new Handler()).postDelayed(this::again, 400);
                    }
                }
            }

            private void homeActivity()
            {
                //user successfully logged in
                if (rememberMe.isChecked())
                {
                    sessionManager.setLogin(true);
                }
                Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(homeActivity);
                finish();
            }
            private void again()
            {
                loginButton.revertAnimation();
                loginButton.setBackgroundResource(R.drawable.button_shape);
            }
        };
        loginButton.startAnimation();
        loginAnimation.execute();
    }
}
