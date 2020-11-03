package ir.arapp.arappofficial.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ir.arapp.arappofficial.Adapter.CategoryItemAdapter;
import ir.arapp.arappofficial.Adapter.NotificationAdapter;
import ir.arapp.arappofficial.AppService.DrawerLocker;
import ir.arapp.arappofficial.AppService.RetrofitClient;
import ir.arapp.arappofficial.Data.CategoryItem;
import ir.arapp.arappofficial.Data.NotificationItem;
import ir.arapp.arappofficial.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment
{
    //Variables
    private LottieAnimationView lottieAnimationView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    ArrayList<NotificationItem> notificationItem;
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        //Hooks
        lottieAnimationView = view.findViewById(R.id.loadingNotification);
        recyclerView = view.findViewById(R.id.notificationRecyclerView);

        //getData
        if (checkConnection())
        {
            notificationItem = new ArrayList<>();
            getData();
        }

        //Drawer layout
        ((DrawerLocker) Objects.requireNonNull(getActivity())).setDrawerLocked(true);

        return view;
    }

    //To hide toolbar
    @Override
    public void onResume()
    {
        super.onResume();
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

    //get notification Data
    private void getData()
    {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getNotification();

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
                        JSONArray jsonArray = new JSONArray(responseBody);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String title = jsonObject.getString("title");
                            String subtitle = jsonObject.getString("subtitle");
                            String text = jsonObject.getString("text");
                            String image = jsonObject.getString("image");
                            int visible = jsonObject.getInt("visible");
                            String created_at = jsonObject.getString("created_at");
                            notificationItem.add(new NotificationItem(id, title, subtitle, text, image, visible, created_at));
                        }
                        setAdapter();
                    }
                    catch (IOException | JSONException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                StyleableToast.makeText(Objects.requireNonNull(getContext()), t.getMessage(), Toast.LENGTH_LONG, R.style.toastTheme).show();
            }
        });
    }
    //set Recycler view adapter
    private void setAdapter()
    {
        lottieAnimationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new NotificationAdapter(getContext(), notificationItem);
        recyclerView.setAdapter(adapter);
    }
}
