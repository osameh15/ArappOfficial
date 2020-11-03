package ir.arapp.arappofficial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import ir.arapp.arappofficial.Activities.DetailActivity;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ServiceItemHolder>
{
    ArrayList<HomeServicesData> servicesData;
    private Context context;

    public ServiceItemAdapter(ArrayList<HomeServicesData> servicesData, Context context)
    {
        this.servicesData = servicesData;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);

        return new ServiceItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServiceItemHolder holder, int position)
    {
        HomeServicesData homeServicesData = servicesData.get(position);

        holder.imageService.setImageResource(homeServicesData.getImage());
        holder.timeService.setText(homeServicesData.getDate());
        holder.titleService.setText(homeServicesData.getTitle());
        holder.locationService.setText(homeServicesData.getAddress());
        holder.distanceService.setText(homeServicesData.getSpecial() + " km");

        if (homeServicesData.getRate() == 0)
        {
            holder.rateService.setText("-");
            holder.frameLayout.setBackgroundResource(R.drawable.rate_zero_shape);
        }
        else if (homeServicesData.getRate() < 2)
        {
            holder.rateService.setText(homeServicesData.getRate() + "");
            holder.frameLayout.setBackgroundResource(R.drawable.rate_min_shape);
        }
        else if (homeServicesData.getRate() < 4)
        {
            holder.rateService.setText(homeServicesData.getRate() + "");
            holder.frameLayout.setBackgroundResource(R.drawable.rate_normal_shape);
        }
        else if (homeServicesData.getRate() <= 5)
        {
            holder.rateService.setText(homeServicesData.getRate() + "");
            holder.frameLayout.setBackgroundResource(R.drawable.rate_max_shape);
        }

        holder.progressBar.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", homeServicesData.getId());
            intent.putExtra("userId", homeServicesData.getUserID());
            intent.putExtra("currentUserId", homeServicesData.getCurrentUserID());
            intent.putExtra("email", homeServicesData.getEmail());
            intent.putExtra("phone", homeServicesData.getPhone());
            intent.putExtra("title", homeServicesData.getTitle());
            intent.putExtra("intro", homeServicesData.getIntro());
            intent.putExtra("name", homeServicesData.getName());
            intent.putExtra("text", homeServicesData.getText());
            intent.putExtra("image", homeServicesData.getImage());
            intent.putExtra("date", homeServicesData.getDate());
            intent.putExtra("rate", homeServicesData.getRate());
            intent.putExtra("category", homeServicesData.getCategory());
            intent.putExtra("city", homeServicesData.getCity());
            intent.putExtra("province", homeServicesData.getProvince());
            intent.putExtra("rateCount", homeServicesData.getRate_count());
            intent.putExtra("special", homeServicesData.getSpecial());
            intent.putExtra("allComment", homeServicesData.getAllComment());
            intent.putExtra("allLikes", homeServicesData.getAllLikes());
            intent.putExtra("address", homeServicesData.getAddress());
            intent.putExtra("specialText", homeServicesData.getSpecialText());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        return servicesData.size();
    }

    public static class ServiceItemHolder extends RecyclerView.ViewHolder
    {
        ImageView imageService;
        TextView timeService;
        TextView titleService;
        TextView locationService;
        TextView distanceService;
        TextView rateService;
        FrameLayout frameLayout;
        ProgressBar progressBar;
        CardView cardView;
        public ServiceItemHolder(@NonNull View itemView)
        {
            super(itemView);
            imageService = itemView.findViewById(R.id.serviceItemImage);
            timeService = itemView.findViewById(R.id.timeServiceItem);
            titleService = itemView.findViewById(R.id.titleServiceItem);
            locationService = itemView.findViewById(R.id.locateServiceItem);
            distanceService = itemView.findViewById(R.id.distanceServiceItem);
            rateService = itemView.findViewById(R.id.rateServiceItem);
            frameLayout = itemView.findViewById(R.id.rateServiceItemFrameLayout);
            progressBar = itemView.findViewById(R.id.progressBarImageItemService);
            cardView = itemView.findViewById(R.id.cardViewServiceItem);
        }
    }
}
