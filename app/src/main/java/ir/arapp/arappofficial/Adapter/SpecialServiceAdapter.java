package ir.arapp.arappofficial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import ir.arapp.arappofficial.Activities.DetailActivity;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class SpecialServiceAdapter extends PagerAdapter
{
    private Context context;
    private ArrayList<HomeServicesData> servicesData;
    private LayoutInflater layoutInflater;

    public SpecialServiceAdapter(Context context, ArrayList<HomeServicesData> servicesData)
    {
        this.context = context;
        this.servicesData = servicesData;
    }

    @Override
    public int getCount()
    {
        return servicesData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view.equals(object);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.special_layout_slider, container, false);

        CardView cardView;
        RoundedImageView roundedImageView;
        FrameLayout frameLayout;
        TextView title;
        TextView name;
        TextView date;
        TextView comment;
        TextView rate;

        cardView = view.findViewById(R.id.specialCardView);
        roundedImageView = view.findViewById(R.id.specialImage);
        frameLayout = view.findViewById(R.id.rateSpecialFrameLayout);
        title = view.findViewById(R.id.specialServiceTitle);
        name = view.findViewById(R.id.specialServiceName);
        date = view.findViewById(R.id.specialServiceTime);
        comment = view.findViewById(R.id.specialServiceComment);
        rate = view.findViewById(R.id.rateSpecial);

        roundedImageView.setImageResource(servicesData.get(position).getImage());
        title.setText(servicesData.get(position).getTitle());
        name.setText(servicesData.get(position).getName());
        date.setText(servicesData.get(position).getDate());
        comment.setText(servicesData.get(position).getAllComment());

        if (servicesData.get(position).getRate() == 0)
        {
            rate.setText("-");
            frameLayout.setBackgroundResource(R.drawable.rate_zero_shape);
        }
        else if (servicesData.get(position).getRate() < 2)
        {
            rate.setText(servicesData.get(position).getRate() + "");
            frameLayout.setBackgroundResource(R.drawable.rate_min_shape);
        }
        else if (servicesData.get(position).getRate() < 4)
        {
            rate.setText(servicesData.get(position).getRate() + "");
            frameLayout.setBackgroundResource(R.drawable.rate_normal_shape);
        }
        else if (servicesData.get(position).getRate() <= 5)
        {
            rate.setText(servicesData.get(position).getRate() + "");
            frameLayout.setBackgroundResource(R.drawable.rate_max_shape);
        }

        cardView.setOnClickListener(view1 ->
        {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("id", servicesData.get(position).getId());
            intent.putExtra("userId", servicesData.get(position).getUserID());
            intent.putExtra("currentUserId", servicesData.get(position).getCurrentUserID());
            intent.putExtra("email", servicesData.get(position).getEmail());
            intent.putExtra("phone", servicesData.get(position).getPhone());
            intent.putExtra("title", servicesData.get(position).getTitle());
            intent.putExtra("intro", servicesData.get(position).getIntro());
            intent.putExtra("name", servicesData.get(position).getName());
            intent.putExtra("text", servicesData.get(position).getText());
            intent.putExtra("image", servicesData.get(position).getImage());
            intent.putExtra("date", servicesData.get(position).getDate());
            intent.putExtra("rate", servicesData.get(position).getRate());
            intent.putExtra("category", servicesData.get(position).getCategory());
            intent.putExtra("city", servicesData.get(position).getCity());
            intent.putExtra("province", servicesData.get(position).getProvince());
            intent.putExtra("rateCount", servicesData.get(position).getRate_count());
            intent.putExtra("special", servicesData.get(position).getSpecial());
            intent.putExtra("allComment", servicesData.get(position).getAllComment());
            intent.putExtra("allLikes", servicesData.get(position).getAllLikes());
            intent.putExtra("address", servicesData.get(position).getAddress());
            intent.putExtra("specialText", servicesData.get(position).getSpecialText());
            context.startActivity(intent);
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((View) object);
    }
}
