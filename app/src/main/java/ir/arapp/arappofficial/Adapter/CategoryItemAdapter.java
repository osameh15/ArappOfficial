package ir.arapp.arappofficial.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import ir.arapp.arappofficial.Activities.ServiceActivity;
import ir.arapp.arappofficial.Data.CategoryItem;
import ir.arapp.arappofficial.R;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemHolder>
{

    //Variables
    Context context;
    ArrayList<CategoryItem> categoryItems;

    public CategoryItemAdapter(Context context, ArrayList<CategoryItem> categoryItems)
    {
        this.context = context;
        this.categoryItems = categoryItems;
    }

    @NonNull
    @Override
    public CategoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_container, parent, false);

        return new CategoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemHolder holder, int position)
    {
        CategoryItem categoryItem = categoryItems.get(position);

        Glide
                .with(context)
                .load(categoryItem.getUrl())
                .placeholder(R.drawable.arapp_news)
                .error(R.drawable.arapp_news)
                .override(500, 200)
                .into(holder.roundedImageView);

        holder.cardView.setOnClickListener(view ->
        {
            Intent categoryIntent = new Intent(context, ServiceActivity.class);
            categoryIntent.putExtra("name", categoryItem.getText());
            context.startActivity(categoryIntent);
        });
    }

    @Override
    public int getItemCount()
    {
        return categoryItems.size();
    }

    public static class CategoryItemHolder extends RecyclerView.ViewHolder
    {
        //Variables
        CardView cardView;
        RoundedImageView roundedImageView;

        public CategoryItemHolder(@NonNull View itemView)
        {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewCategory);
            roundedImageView = itemView.findViewById(R.id.categoryPicture);
        }
    }
}
