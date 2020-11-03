package ir.arapp.arappofficial.Adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.Objects;

import ir.arapp.arappofficial.Data.SliderItem;
import ir.arapp.arappofficial.R;

public class BannerViewHolder extends BaseViewHolder<SliderItem>
{
    public BannerViewHolder(@NonNull View itemView)
    {
        super(itemView);
        RoundedImageView imageView = itemView.findViewById(R.id.bannerImage);
    }

    @Override
    public void bindData(SliderItem data, int position, int pageSize)
    {
        RoundedImageView imageView = itemView.findViewById(R.id.bannerImage);
        Glide
                .with(imageView)
                .load(data.getUrl())
                .placeholder(R.drawable.arapp_news)
                .error(R.drawable.arapp_news)
                .override(500, 200)
                .into(imageView);
    }
}
