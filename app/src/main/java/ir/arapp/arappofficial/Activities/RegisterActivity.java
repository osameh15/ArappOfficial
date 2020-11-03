package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity
{

    //Variables
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile ("^" +
                    "(?=.*[0-9])" +                        //at least 1 digit
                    "(?=.*[a-z])" +                        //at least 1 lower case letter
                    "(?=.*[A-Z])" +                       //at least 1 upper case letter
                    "(?=.*[@#$%&+=)])" +    //at least 1 special character
                    "(?=\\S+$)" +                       //no white space
                    ".{6,}" +                                //at least 6 character
                    "$");

    Toolbar toolbar;
    LinearLayout phoneNumberLinearLayout;
    LinearLayout validateLinearLayout;
    LinearLayout loginLinearLayout;
    LinearLayout registerLinearLayout;
    TextInputLayout nameEditText;
    TextInputLayout emailEditText;
    TextInputLayout passEditText;
    TextInputLayout cnfPassEditText;
    TextInputLayout phoneNumber;
    TextView countDownTimerText;
    TextView resendVerifyCodeText;
    PinView pinView;
    Button buttonValidate;
    CountDownTimer countDownTimer;
    RadioButton setService;
    RadioButton getService;
    CircularProgressButton circularProgressButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hooks
        toolbar = findViewById(R.id.registerToolbar);
        nameEditText = findViewById(R.id.nameRegister);
        emailEditText = findViewById(R.id.emailRegister);
        passEditText  = findViewById(R.id.passwordRegister);
        cnfPassEditText = findViewById(R.id.confirmPasswordRegister);
        setService = findViewById(R.id.setServiceRadioButton);
        getService = findViewById(R.id.getServiceRadioButton);
        circularProgressButton = findViewById(R.id.buttonSubmitRegister);
        phoneNumberLinearLayout = findViewById(R.id.phoneNumberLinearLayout);
        validateLinearLayout = findViewById(R.id.validationLinearLayout);
        loginLinearLayout = findViewById(R.id.loginLinearLayout);
        registerLinearLayout = findViewById(R.id.registerLinearLayout);
        phoneNumber = findViewById(R.id.phoneNumberInputPhoneNo);
        countDownTimerText = findViewById(R.id.countDownTimerRegister);
        resendVerifyCodeText = findViewById(R.id.resendVerifyCodeText);
        pinView = findViewById(R.id.validationCodeRegister);
        buttonValidate = findViewById(R.id.buttonValidate);

        //Toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set background drawable for register button...
        circularProgressButton.setBackgroundResource(R.drawable.button_shape);

    }

    //back Button method ...
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    //Validate functions ...
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
    //validate phone number method ...
    private boolean verifyPhoneNumber()
    {
        String mobile = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();

        if (mobile.length() == 0)
        {
            phoneNumber.setError("لطفا شماره همراه خود را وارد کنید");
        }
        else if (mobile.length() == 11)
        {
            if (mobile.startsWith("09"))
            {
                phoneNumber.setError(null);
                phoneNumber.setErrorEnabled(false);
                return true;
            }
            else
            {
                phoneNumber.setError("شماره همراه واردشده معتبر نیست");
            }
        }
        else
        {
            phoneNumber.setError("شماره همراه واردشده معتبر نیست");
        }

        return false;
    }
    //validate name method ...
    private boolean validateName()
    {
        String name = Objects.requireNonNull(nameEditText.getEditText()).getText().toString();

        if (name.isEmpty())
        {
            nameEditText.setError("نام و نام خانوادگی خود را وارد نمایید");
            nameEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
        else
        {
            nameEditText.setError(null);
            nameEditText.setErrorEnabled(false);
            return true;
        }
    }
    //validate email method ...
    private boolean validateEmail()
    {
        String mail = Objects.requireNonNull(emailEditText.getEditText()).getText().toString();

        if (mail.isEmpty())
        {
            emailEditText.setError("ایمیل خود را وارد نمایید");
            emailEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            emailEditText.setError("آدرس ایمیل را به درستی وارد کنید");
            emailEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else
        {
            emailEditText.setError(null);
            emailEditText.setErrorEnabled(false);
            return true;
        }
        return false;
    }
    //validate password pattern method ...
    private boolean validatePassword()
    {
        String passwordInput = Objects.requireNonNull(passEditText.getEditText()).getText().toString();

        if (passwordInput.isEmpty())
        {
            passEditText.setError("رمز عبور خود را وارد نمایید");
            passEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches())
        {
            passEditText.setError("رمزعبور حداقل باید دارای 6 کاراکتر (ترکیبی از عدد و حروف بزرگ و کوچک و کاراکترهای خاص) باشد");
            passEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else
        {
            passEditText.setError(null);
            passEditText.setErrorEnabled(false);
            return true;
        }
        return false;
    }
    //Method for check the password and confirm password ...
    private boolean checkPassword()
    {
        String passInput = Objects.requireNonNull(passEditText.getEditText()).getText().toString();
        String cnfPassInput = Objects.requireNonNull(cnfPassEditText.getEditText()).getText().toString();

        if (cnfPassInput.equals(passInput))
        {
            cnfPassEditText.setError(null);
            cnfPassEditText.setErrorEnabled(false);
            return true;
        }
        else if (cnfPassInput.length() == 0 && passInput.length() != 0)
        {
            cnfPassEditText.setError("ابتدا رمزعبور خود را وارد نمایید");
            cnfPassEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
        else
        {
            cnfPassEditText.setError("رمزعبور های واردشده یکسان نیست!");
            cnfPassEditText.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
    }
    //Method for show type of service ...
    private String typeOfServices()
    {
        if (setService.isChecked())
        {
            return "سرویس دهنده";
        }
        else if (getService.isChecked())
        {
            return "سرویس گیرنده";
        }
        else
        {
            StyleableToast.makeText(getApplicationContext(), "یکی از سرویس ها را انتخاب کنید", Toast.LENGTH_LONG, R.style.toastTheme).show();
            return "failed";
        }
    }

    //Step one to create account ...
    //method for submit phone button ...
    public void submitPhoneNumber(View view)
    {
        if (verifyPhoneNumber() && checkConnection())
        {
            final String mobile = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .registerPhoneApp(mobile);

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
                                submitPhone();
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                            }
                            else
                            {
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
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
    //Method for show verify code and submit phone number ...
    private void submitPhone()
    {
        Objects.requireNonNull(pinView.getText()).clear();
        pinView.setLineColor(getResources().getColor(R.color.shade6));
        closeShowKeyboard(true);
        phoneNumberLinearLayout.setVisibility(View.GONE);
        loginLinearLayout.setVisibility(View.GONE);
        validateLinearLayout.setVisibility(View.VISIBLE);
        pinView.requestFocus();
        if (pinView.requestFocus())
        {
            closeShowKeyboard(false);
            pinView.setLineColor(getResources().getColor(R.color.shade6));
        }
        buttonValidate.setEnabled(true);
        resendVerifyCodeText.setEnabled(false);
        buttonValidate.setBackground(getDrawable(R.drawable.button_shape));
        resendVerifyCodeText.setTextColor(getResources().getColor(R.color.disable));
        startCountDownTimer();
    }
    //method for edit phone button ...
    public void editPhoneNumber(View view)
    {
        closeShowKeyboard(true);
        validateLinearLayout.setVisibility(View.GONE);
        loginLinearLayout.setVisibility(View.VISIBLE);
        phoneNumberLinearLayout.setVisibility(View.VISIBLE);
        phoneNumber.requestFocus();
        if (phoneNumber.requestFocus())
        {
            closeShowKeyboard(false);
        }
        cancelTimer();
    }
    //method for verify the submitted code button ...
    public void verifyCode(View view)
    {
        final String mobile = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();
        String OTP = Objects.requireNonNull(pinView.getText()).toString();

        if (checkConnection() && OTP.length() != 0)
        {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .verifyPhoneApp(mobile, OTP);

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
                                cancelTimer();
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                                closeShowKeyboard(true);
                                validateLinearLayout.setVisibility(View.GONE);
                                registerLinearLayout.setVisibility(View.VISIBLE);
                                nameEditText.requestFocus();
                                if (nameEditText.requestFocus())
                                {
                                    closeShowKeyboard(false);
                                }
                            }
                            else
                            {
                                pinView.setLineColor(getResources().getColor(R.color.splitComplementary2));
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
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
        else if (OTP.length() == 0)
        {
            StyleableToast.makeText(getApplicationContext(), "ابتدا کد تایید را وارد کنید", Toast.LENGTH_LONG, R.style.toastTheme).show();
        }
    }
    //method for login button ...
    public void login(View view)
    {
        Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginActivity);
    }
    //resend verify code method button ...
    public void sendVerifyCode(View view)
    {
        if (checkConnection())
        {
            final String mobile = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .registerPhoneApp(mobile);

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
                                buttonValidate.setEnabled(true);
                                resendVerifyCodeText.setEnabled(false);
                                buttonValidate.setBackground(getDrawable(R.drawable.button_shape));
                                resendVerifyCodeText.setTextColor(getResources().getColor(R.color.disable));
                                pinView.setLineColor(getResources().getColor(R.color.shade6));
                                Objects.requireNonNull(pinView.getText()).clear();
                                startCountDownTimer();
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                            }
                            else
                            {
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
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

    //Method for close or show input method(keyboard) ...
    private void closeShowKeyboard(Boolean flag)
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (flag)
            {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            else
            {
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    //Timers...
    //timer to change pass...
    private void startCountDownTimer()
    {
        countDownTimer = new CountDownTimer(60000, 1000)
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished)
            {
                if (millisUntilFinished < 10000)
                {
                    countDownTimerText.setTextColor(getResources().getColor(R.color.compound5));
                    countDownTimerText.setText("00:0"+millisUntilFinished / 1000);
                }
                else
                {
                    countDownTimerText.setText("00:"+millisUntilFinished / 1000);
                    countDownTimerText.setTextColor(getResources().getColor(R.color.colorAccentDark));
                }
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish()
            {
                countDownTimerText.setText("00:00");
                countDownTimerText.setTextColor(getResources().getColor(R.color.disable));
                resendVerifyCodeText.setTextColor(getResources().getColor(R.color.shade5));
                resendVerifyCodeText.setEnabled(true);
                buttonValidate.setEnabled(false);
                buttonValidate.setBackground(getDrawable(R.drawable.button_shape_disable));
                StyleableToast.makeText(getApplicationContext(), "زمان شما به پایان رسید. مجددا اقدام کنید",
                        Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        }.start();
    }
    //Function to cancel timer...
    private void cancelTimer()
    {
        countDownTimer.cancel();
    }

    //Register methods ...
    //method for submit register button ...
    public void submitRegister(View view)
    {
        register();
    }
    //Method for sign up and save in database ...
    private void register()
    {
        String mobile = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();
        String name = Objects.requireNonNull(nameEditText.getEditText()).getText().toString();
        String mail = Objects.requireNonNull(emailEditText.getEditText()).getText().toString();
        String passwordInput = Objects.requireNonNull(passEditText.getEditText()).getText().toString();

        if (!checkConnection() | !validateName() | !validateEmail() | !validatePassword() | !checkPassword() | typeOfServices().equals("failed"))
        {
            return;
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .registerUserApp(mobile, name, mail, passwordInput, typeOfServices());

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
                            registerLoading(message, true);
                        }
                        else
                        {
                            registerLoading(message, false);
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
    //button animation for loading and go to home activity ...
    private void registerLoading(String message, boolean flag)
    {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loginAnimation = new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... strings)
            {
                try
                {
                    Thread.sleep(1500);
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
                        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        circularProgressButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::homeActivity, 500);
                    }
                    else
                    {
                        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        circularProgressButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::again, 500);
                    }
                }
            }

            private void homeActivity()
            {
                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
            private void again()
            {
                circularProgressButton.revertAnimation();
                circularProgressButton.setBackgroundResource(R.drawable.button_shape);
            }
        };
        circularProgressButton.startAnimation();
        loginAnimation.execute();
    }
}
