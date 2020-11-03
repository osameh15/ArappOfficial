package ir.arapp.arappofficial.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.arapp.arappofficial.Activities.LoginActivity;
import ir.arapp.arappofficial.Activities.RegisterActivity;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.AppService.SessionManager;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment implements View.OnClickListener
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
    private AppCompatSpinner eduSpinner;
    private AppCompatSpinner provinceSpinner;
    private AppCompatSpinner citySpinner;
    private Typeface typeface;
    private TextInputLayout name;
    private TextInputLayout email;
    private TextInputLayout phoneEditText;
    private TextInputLayout bio;
    private TextInputLayout passEditText;
    private TextInputLayout cnfPassEditText;
    private TextView updatedAt;
    private TextView serviceLocation;
    private CircularProgressButton submitButton;
    private CircularProgressButton buttonChangePassword;
    //Session Manager
    private SessionManager sessionManager;
    private String phone;
    private String educationServer = "none";
    private int provinceServer = 0;
    private String cityServer = "none";

    //Spinner List
    private List<String> eduList;
    private List<String> provinceList;
    private List<String> cityList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        typeface = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()).getApplicationContext(), R.font.vazir);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //Hooks
        eduSpinner = view.findViewById(R.id.educationSpinner);
        provinceSpinner = view.findViewById(R.id.provinceSpinner);
        citySpinner = view.findViewById(R.id.citySpinner);
        name = view.findViewById(R.id.nameProfile);
        email = view.findViewById(R.id.emailProfile);
        phoneEditText = view.findViewById(R.id.phoneProfile);
        passEditText = view.findViewById(R.id.passwordProfile);
        cnfPassEditText = view.findViewById(R.id.confirmPasswordProfile);
        bio = view.findViewById(R.id.BioProfile);
        updatedAt = view.findViewById(R.id.updatedProfile);
        serviceLocation = view.findViewById(R.id.serviceLocation);
        submitButton = view.findViewById(R.id.buttonEditProfile);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        //Session Manager
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()).getApplicationContext());
        phone = sessionManager.getUserPhone();

        //Spinner List defining
        eduList = new ArrayList<>();
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();

        //Get data
        getData();

        //Set background drawable for register button...
        submitButton.setBackgroundResource(R.drawable.button_shape);
        buttonChangePassword.setBackgroundResource(R.drawable.button_shape_red);

        //On click ...
        submitButton.setOnClickListener(this);
        buttonChangePassword.setOnClickListener(this);

        return view;
    }

    //Validate functions ...
    //validate phone number method ...
    //validate name method ...
    private boolean validateName()
    {
        String nameString = Objects.requireNonNull(name.getEditText()).getText().toString();

        if (nameString.isEmpty())
        {
            name.setError("نام و نام خانوادگی خود را وارد نمایید");
            name.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
        else
        {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateBio()
    {
        String bioString = Objects.requireNonNull(bio.getEditText()).getText().toString();

        if (bioString.isEmpty())
        {
            bio.setError("هنوز هیچ توضیحی ننوشته اید!");
            bio.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
            return false;
        }
        else
        {
            bio.setError(null);
            bio.setErrorEnabled(false);
            return true;
        }
    }
    //validate email method ...
    private boolean validateEmail()
    {
        String mail = Objects.requireNonNull(email.getEditText()).getText().toString();

        if (mail.isEmpty())
        {
            email.setError("ایمیل خود را وارد نمایید");
            email.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("آدرس ایمیل را به درستی وارد کنید");
            email.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.splitComplementary2)));
        }
        else
        {
            email.setError(null);
            email.setErrorEnabled(false);
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
    //Spinner list and adapter ...
    private void spinnerAdapter(String edu, String province, String city)
    {
        eduList.addAll(Arrays.asList(getResources().getStringArray(R.array.education_list)));
        provinceList.addAll(Arrays.asList(getResources().getStringArray(R.array.province_list)));

        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, eduList)
                {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getDropDownView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }
                };
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);

        ArrayAdapter<String> arrayAdapter1 = new
                ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.custom_spinner_layout, provinceList)
                {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getDropDownView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }
                };
        arrayAdapter1.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);

        eduSpinner.setAdapter(arrayAdapter);
        provinceSpinner.setAdapter(arrayAdapter1);

        if (edu != null)
        {
            int eduPosition = arrayAdapter.getPosition(edu);
            eduSpinner.setSelection(eduPosition);
        }
        if (province != null)
        {
            int provincePosition = arrayAdapter1.getPosition(province);
            provinceSpinner.setSelection(provincePosition);
        }

        eduSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position != 0)
                {
                    educationServer = parent.getItemAtPosition(position).toString();
                }
                else
                {
                    educationServer = "none";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 1)
                {
                    provinceServer = 8;
                    cityList.clear();
                    cityList.addAll(Arrays.asList(getResources().getStringArray(R.array.city_list_tehran)));
                    ArrayAdapter<String> arrayAdapter2 = new
                            ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, cityList)
                            {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getDropDownView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }
                            };
                    arrayAdapter2.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);
                    citySpinner.setAdapter(arrayAdapter2);
                    if (city != null)
                    {
                        int cityPosition = arrayAdapter2.getPosition(city);
                        citySpinner.setSelection(cityPosition);
                    }
                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (position != 0)
                            {
                                cityServer = parent.getItemAtPosition(position).toString();
                            }
                            else
                            {
                                cityServer = "none";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }
                else if (position == 2)
                {
                    provinceServer = 25;
                    cityList.clear();
                    cityList.addAll(Arrays.asList(getResources().getStringArray(R.array.city_list_gilan)));
                    ArrayAdapter<String> arrayAdapter2 = new
                            ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, cityList)
                            {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getDropDownView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }
                            };
                    arrayAdapter2.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);
                    citySpinner.setAdapter(arrayAdapter2);
                    if (city != null)
                    {
                        int cityPosition = arrayAdapter2.getPosition(city);
                        citySpinner.setSelection(cityPosition);
                    }
                    citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (position != 0)
                            {
                                cityServer = parent.getItemAtPosition(position).toString();
                            }
                            else
                            {
                                cityServer = "none";
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }
                else
                {
                    provinceServer = 0;
                    cityList.clear();
                    cityList.add("ابتدا استان خود را انتخاب نمایید");
                    ArrayAdapter<String> arrayAdapter2 = new
                            ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, cityList)
                            {
                                @NonNull
                                @Override
                                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                {
                                    View view = super.getDropDownView(position, convertView, parent);

                                    ((TextView) view).setTypeface(typeface);

                                    return  view;
                                }
                            };
                    arrayAdapter2.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);
                    citySpinner.setAdapter(arrayAdapter2);
                    cityServer = "none";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
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
                                String email = jsonObject.getString("email");
                                String bio = jsonObject.getString("bio");
                                String education = jsonObject.getString("education");
                                String province = jsonObject.getString("province");
                                String city = jsonObject.getString("city");
                                String type = jsonObject.getString("service");
                                String update = jsonObject.getString("updated_at");
                                setData(name, email, bio, education, province, city, type, update);
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
    private void setData(String nameText, String emailText, String bioText, String edu, String provinceText, String cityText, String type, String up)
    {
        Objects.requireNonNull(name.getEditText()).setText(nameText);
        Objects.requireNonNull(email.getEditText()).setText(emailText);
        Objects.requireNonNull(phoneEditText.getEditText()).setText(phone);
        if (bioText.equals("null"))
        {
            Objects.requireNonNull(bio.getEditText()).setText("توضیحات خود را اینجا بنویسید");
        }
        else
        {
            Objects.requireNonNull(bio.getEditText()).setText(bioText);
        }
        updatedAt.setText("آخرین بروزرسانی:  " + up);
        serviceLocation.setText(type);
        spinnerAdapter(edu, provinceText, cityText);
    }

    //On click method ...
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonEditProfile:
                submitChangeProfile();
                break;
            case R.id.buttonChangePassword:
                changePassword();
                break;
            default:
                break;
        }
    }

    //change profile information method
    private void submitChangeProfile()
    {
        String nameString = Objects.requireNonNull(name.getEditText()).getText().toString();
        String bioString = Objects.requireNonNull(bio.getEditText()).getText().toString();
        String mail = Objects.requireNonNull(email.getEditText()).getText().toString();

        if (!checkConnection() | !validateName() | !validateEmail() | !validateBio())
        {
            return;
        }
        if (bioString.equals("توضیحات خود را اینجا بنویسید"))
        {
            bioString = "none";
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .editUserProfile(phone, nameString, mail, bioString, educationServer, provinceServer, cityServer);

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
                            submitLoading(message, true);
                        }
                        else
                        {
                            submitLoading(message, false);
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
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        });
    }
    private void submitLoading(String message, boolean flag)
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
                        StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message,
                                Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        submitButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::back, 500);
                    }
                    else
                    {
                        StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message,
                                Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        submitButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::again, 500);
                    }
                }
            }

            private void back()
            {
                Objects.requireNonNull(getActivity()).finish();
            }
            private void again()
            {
                submitButton.revertAnimation();
                submitButton.setBackgroundResource(R.drawable.button_shape);
            }
        };
        submitButton.startAnimation();
        loginAnimation.execute();
    }

    //change password and save to database
    private void changePassword()
    {
        String pass = Objects.requireNonNull(passEditText.getEditText()).getText().toString();

        if (!checkConnection() | !validatePassword() | !checkPassword())
        {
            return;
        }
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .changePassword(phone, pass);
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
                StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        });
    }
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
                        StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message,
                                Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        buttonChangePassword.doneLoadingAnimation(Color.parseColor("#E83223"), bitmap);
                        (new Handler()).postDelayed(this::back, 500);
                    }
                    else
                    {
                        StyleableToast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message,
                                Toast.LENGTH_LONG, R.style.toastTheme).show();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        buttonChangePassword.doneLoadingAnimation(Color.parseColor("#E83223"), bitmap);
                        (new Handler()).postDelayed(this::again, 500);
                    }
                }
            }

            private void back()
            {
                Objects.requireNonNull(getActivity()).finish();
            }
            private void again()
            {
                buttonChangePassword.revertAnimation();
                buttonChangePassword.setBackgroundResource(R.drawable.button_shape_red);
            }
        };
        buttonChangePassword.startAnimation();
        loginAnimation.execute();
    }
}
