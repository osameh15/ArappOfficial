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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class ForgetPassword extends AppCompatActivity
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
    LinearLayout phoneLinearLayout;
    LinearLayout validateLinearLayout;
    LinearLayout changePassLinearLayout;
    TextInputLayout phoneTextInputLayout;
    TextInputLayout passTextInputLayout;
    TextInputLayout cnfPassTextInputLayout;
    TextView countDownTimerText;
    TextView resendVerifyCodeText;
    PinView pinView;
    Button buttonValidate;
    CircularProgressButton buttonChangePass;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //Hooks
        toolbar = findViewById(R.id.forgetPasswordToolbar);
        phoneLinearLayout = findViewById(R.id.forgetPasswordPhoneLinearLayout);
        validateLinearLayout = findViewById(R.id.forgetPassValidateLinearLayout);
        changePassLinearLayout = findViewById(R.id.changePasswordLinearLayout);
        phoneTextInputLayout = findViewById(R.id.forgetPassInputPhoneNo);
        passTextInputLayout = findViewById(R.id.passwordForgetPass);
        countDownTimerText = findViewById(R.id.countDownTimerRegisterForgetPass);
        resendVerifyCodeText = findViewById(R.id.resendVerifyCodeTextForgetPass);
        cnfPassTextInputLayout = findViewById(R.id.confirmPasswordForgetPass);
        buttonValidate = findViewById(R.id.buttonValidateForgetPass);
        buttonChangePass = findViewById(R.id.buttonChangePass);
        pinView = findViewById(R.id.validationCodeForgetPass);

        //Toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set background drawable for register button...
        buttonChangePass.setBackgroundResource(R.drawable.button_shape);
    }

    //back Button method ...
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    //Validate methods ...
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
        String mobile = Objects.requireNonNull(phoneTextInputLayout.getEditText()).getText().toString();

        if (mobile.length() == 0)
        {
            phoneTextInputLayout.setError("لطفا شماره همراه خود را وارد کنید");
            phoneTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else if (mobile.length() == 11)
        {
            if (mobile.startsWith("09"))
            {
                phoneTextInputLayout.setError(null);
                phoneTextInputLayout.setErrorEnabled(false);
                return true;
            }
            else
            {
                phoneTextInputLayout.setError("شماره همراه واردشده معتبر نیست");
                phoneTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            }
        }
        else
        {
            phoneTextInputLayout.setError("شماره همراه واردشده معتبر نیست");
            phoneTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }

        return false;
    }
    //validate password pattern method ...
    private boolean validatePassword()
    {
        String passwordInput = Objects.requireNonNull(passTextInputLayout.getEditText()).getText().toString();

        if (passwordInput.isEmpty())
        {
            passTextInputLayout.setError("رمز عبور خود را وارد نمایید");
            passTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches())
        {
            passTextInputLayout.setError("رمزعبور حداقل باید دارای 6 کاراکتر (ترکیبی از عدد و حروف بزرگ و کوچک و کاراکترهای خاص) باشد");
            passTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else
        {
            passTextInputLayout.setError(null);
            passTextInputLayout.setErrorEnabled(false);
            return true;
        }
        return false;
    }
    //Method for check the password and confirm password ...
    private boolean checkPassword()
    {
        String passInput = Objects.requireNonNull(passTextInputLayout.getEditText()).getText().toString();
        String cnfPassInput = Objects.requireNonNull(cnfPassTextInputLayout.getEditText()).getText().toString();

        if (cnfPassInput.equals(passInput))
        {
            cnfPassTextInputLayout.setError(null);
            cnfPassTextInputLayout.setErrorEnabled(false);
            return true;
        }
        else if (cnfPassInput.length() == 0 && passInput.length() != 0)
        {
            cnfPassTextInputLayout.setError("ابتدا رمزعبور خود را وارد نمایید");
            cnfPassTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
        else
        {
            cnfPassTextInputLayout.setError("رمزعبور های واردشده یکسان نیست!");
            cnfPassTextInputLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
    }

    //forget pass methods and change password ...
    //method for submit phone button ...
    public void submitPhoneNumber(View view)
    {
        if (verifyPhoneNumber() && checkConnection())
        {
            String phone = Objects.requireNonNull(phoneTextInputLayout.getEditText()).getText().toString();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .resetPasswordPhoneApp(phone);

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
    //submit phone function to check phone number and show pin code ...
    private void submitPhone()
    {
        Objects.requireNonNull(pinView.getText()).clear();
        pinView.setLineColor(getResources().getColor(R.color.shade6));
        closeShowKeyboard(true);
        phoneLinearLayout.setVisibility(View.GONE);
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
        phoneLinearLayout.setVisibility(View.VISIBLE);
        phoneTextInputLayout.requestFocus();
        if (phoneTextInputLayout.requestFocus())
        {
            closeShowKeyboard(false);
        }
        cancelTimer();
    }
    //resend verify code method button ...
    public void sendVerifyCode(View view)
    {
        if (checkConnection())
        {
            String phone = Objects.requireNonNull(phoneTextInputLayout.getEditText()).getText().toString();

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .resetPasswordPhoneApp(phone);

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
    //method for verify the submitted code button ...
    public void verifyCode(View view)
    {
        String phone = Objects.requireNonNull(phoneTextInputLayout.getEditText()).getText().toString();
        String OTP = Objects.requireNonNull(pinView.getText()).toString();

        if (checkConnection() && OTP.length() != 0)
        {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .resetPasswordCodeApp(phone, OTP);

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
                                closeShowKeyboard(true);
                                validateLinearLayout.setVisibility(View.GONE);
                                changePassLinearLayout.setVisibility(View.VISIBLE);
                                passTextInputLayout.requestFocus();
                                if (passTextInputLayout.requestFocus())
                                {
                                    closeShowKeyboard(false);
                                }
                                StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
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
    //Method for submit new password button ...
    public void submitNewPass(View view)
    {
        newPassChanger();
    }
    //Method for change the password ...
    private void newPassChanger()
    {
        if (!validatePassword() | !checkPassword() | !checkConnection())
        {
            return;
        }

        String phone = Objects.requireNonNull(phoneTextInputLayout.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passTextInputLayout.getEditText()).getText().toString();
        String cnfPassword = Objects.requireNonNull(cnfPassTextInputLayout.getEditText()).getText().toString();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .resetPasswordApp(phone, password, cnfPassword);

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
                            changePassLoading(message, true);
                        }
                        else
                        {
                            changePassLoading(message, false);
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
    private void changePassLoading(String message, boolean flag)
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
                        buttonChangePass.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::loginActivity, 500);
                    }
                    else
                    {
                        StyleableToast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        buttonChangePass.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::again, 500);
                    }
                }
            }
            private void loginActivity()
            {
                Intent loginActivity = new Intent(ForgetPassword.this, LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
            private void again()
            {
                buttonChangePass.revertAnimation();
                buttonChangePass.setBackgroundResource(R.drawable.button_shape);
            }
        };
        buttonChangePass.startAnimation();
        loginAnimation.execute();
    }

    //Method for close or show input method(keyboard) ...
    private void closeShowKeyboard(boolean flag)
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

    //Timers ...
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
                buttonValidate.setEnabled(false);
                resendVerifyCodeText.setEnabled(true);
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
}
