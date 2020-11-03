package ir.arapp.arappofficial.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.List;

import ir.arapp.arappofficial.Data.SliderItem;
import ir.arapp.arappofficial.R;

public class BannerAdapter extends BaseBannerAdapter<SliderItem, BannerViewHolder>
{

    @Override
    protected void onBind(BannerViewHolder holder, SliderItem data, int position, int pageSize)
    {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public BannerViewHolder createViewHolder(View itemView, int viewType)
    {
        return new BannerViewHolder(itemView);
    }

    @Override
    public int getLayoutId(int viewType)
    {
        return R.layout.slider_item_container;
    }
}
