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
import ir.arapp.arappofficial.Activities.ServiceActivity;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class CategoryServiceAdapter extends RecyclerView.Adapter<CategoryServiceAdapter.CategoryServiceHolder>
{
    Context context;
    ArrayList<HomeServicesData> servicesData;

    public CategoryServiceAdapter(Context context, ArrayList<HomeServicesData> servicesData)
    {
        this.context = context;
        this.servicesData = servicesData;
    }

    @NonNull
    @Override
    public CategoryServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        if (viewType == R.layout.category_card_services)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_services, parent, false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_services_more, parent, false);
        }

        return new CategoryServiceHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryServiceHolder holder, int position)
    {
        if (position == servicesData.size() && servicesData.size() >= 2)
        {
            holder.moreCardView.setOnClickListener(view ->
            {
                Intent categoryIntent = new Intent(context, ServiceActivity.class);
                categoryIntent.putExtra("name", servicesData.get(0).getCategory());
                context.startActivity(categoryIntent);
            });
        }
        else
        {
            HomeServicesData homeServicesData = servicesData.get(position);
            holder.serviceImage.setImageResource(homeServicesData.getImage());
            holder.mTime.setText(homeServicesData.getDate());
            holder.mTitle.setText(homeServicesData.getTitle());
            holder.mCity.setText(homeServicesData.getCity());
            holder.mComment.setText(homeServicesData.getAllComment());

            if (homeServicesData.getRate() == 0)
            {
                holder.mRate.setText("-");
                holder.frameLayout.setBackgroundResource(R.drawable.rate_zero_shape);
            }
            else if (homeServicesData.getRate() < 2)
            {
                holder.mRate.setText(homeServicesData.getRate() + "");
                holder.frameLayout.setBackgroundResource(R.drawable.rate_min_shape);
            }
            else if (homeServicesData.getRate() < 4)
            {
                holder.mRate.setText(homeServicesData.getRate() + "");
                holder.frameLayout.setBackgroundResource(R.drawable.rate_normal_shape);
            }
            else if (homeServicesData.getRate() <= 5)
            {
                holder.mRate.setText(homeServicesData.getRate() + "");
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
    }

    @Override
    public int getItemCount()
    {
        return (servicesData.size() >= 2) ? servicesData.size() + 1 : servicesData.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == servicesData.size() && servicesData.size() >= 2)
                ? R.layout.category_card_services_more : R.layout.category_card_services;
    }

    public static class CategoryServiceHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        CardView moreCardView;
        ImageView serviceImage;
        TextView mTitle;
        TextView mTime;
        TextView mComment;
        TextView mCity;
        TextView mRate;
        FrameLayout frameLayout;
        ProgressBar progressBar;
        public CategoryServiceHolder(@NonNull View itemView)
        {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewServiceItem);
            moreCardView = itemView.findViewById(R.id.cardViewServiceMore);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            mTitle = itemView.findViewById(R.id.titleService);
            mTime = itemView.findViewById(R.id.timeService);
            mComment = itemView.findViewById(R.id.commentAllService);
            mCity = itemView.findViewById(R.id.cityService);
            mRate = itemView.findViewById(R.id.rate_service);
            frameLayout = itemView.findViewById(R.id.rateCategoryFrameLayout);
            progressBar = itemView.findViewById(R.id.progressBarImageService);
        }
    }
}
