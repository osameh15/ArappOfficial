package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

import java.util.Objects;

import ir.arapp.arappofficial.Fragments.AddServiceFragment;
import ir.arapp.arappofficial.Fragments.HomeFragment;
import ir.arapp.arappofficial.Fragments.UserProfileFragment;
import ir.arapp.arappofficial.R;

public class ProfileActivity extends AppCompatActivity
{

    //variables...
    Toolbar toolbar;
    TextView toolbarTitle;
    FrameLayout fragmentContainer;
    private String fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Hooks
        toolbar = findViewById(R.id.profileToolbar);
        toolbarTitle = findViewById(R.id.profile_toolbar);
        fragmentContainer = findViewById(R.id.fragment_profile_container);

        //Toolbar
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bundle extra data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            fragment = bundle.getString("name");
        }

        //show Fragment
        showFragments();
    }

    //OnBackPressed
    @Override
    public void onBackPressed()
    {
        finish();
    }
    //back Button method ...
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    //show fragments function to switch Between fragments
    private void showFragments()
    {
        if (Objects.requireNonNull(fragment).equals("editProfile"))
        {
            toolbarTitle.setText(getResources().getString(R.string.edit_profile));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new UserProfileFragment()).commit();
        }
        else if (Objects.requireNonNull(fragment).equals("addService"))
        {
            toolbarTitle.setText(getResources().getString(R.string.add_service));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile_container, new AddServiceFragment()).commit();
        }
    }
}
