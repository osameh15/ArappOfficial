package ir.arapp.arappofficial.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.muddzdev.styleabletoast.StyleableToast;
import com.pushpole.sdk.PushPole;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import br.com.simplepass.loadingbutton.customViews.CircularProgressImageButton;
import ir.arapp.arappofficial.Activities.HomeActivity;
import ir.arapp.arappofficial.Activities.LoginActivity;
import ir.arapp.arappofficial.Activities.RegLogActivity;
import ir.arapp.arappofficial.R;

public class MainActivity extends AppCompatActivity
{
    //Variables
    private LottieAnimationView logoSplash;
    private LottieAnimationView wifiLogo;
    private TextView tryAgain;
    private CircularProgressButton circularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Push Notification initialize
        PushPole.initialize(this, true);

        //Hooks
        logoSplash = findViewById(R.id.splashLogo);
        wifiLogo = findViewById(R.id.wifiLogo);
        tryAgain = findViewById(R.id.tryAgainText);
        circularProgressButton = findViewById(R.id.tryAgain);

        //Set background drawable for try button ...
        circularProgressButton.setBackgroundResource(R.drawable.button_shape);

        //set timer to go to next activity
        int SPLASH_SCREEN = 2400;
        new Handler().postDelayed(this::checkActivity, SPLASH_SCREEN);
    }

    //check internet connection ...
    private boolean checkConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return null != networkInfo;
    }

    //Go to activity if device connected to internet...
    private void checkActivity()
    {
        if (checkConnection())
        {
            Intent regLogActivity = new Intent(MainActivity.this, RegLogActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(logoSplash, "logo");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
            startActivity(regLogActivity, activityOptions.toBundle());
            finish();
        }
        else
        {
            wifiLogo.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.VISIBLE);
            circularProgressButton.setVisibility(View.VISIBLE);
        }
    }

    public void tryConnection(View view)
    {
        loading();
    }

    private void loading()
    {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loginAnimation = new AsyncTask<String, String, String>()
        {
            @Override
            protected String doInBackground(String... strings)
            {
                try
                {
                    Thread.sleep(1000);
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
                    if (checkConnection())
                    {
                        //Push Notification initialize
                        PushPole.initialize(getApplicationContext(), true);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                        circularProgressButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::homeActivity, 500);
                    }
                    else
                    {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close);
                        circularProgressButton.doneLoadingAnimation(Color.parseColor("#089C9C"), bitmap);
                        (new Handler()).postDelayed(this::again, 500);
                    }
                }
            }

            private void homeActivity()
            {
                //user successfully logged in
                checkActivity();
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
