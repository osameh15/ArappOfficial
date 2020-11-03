package ir.arapp.arappofficial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;

import ir.arapp.arappofficial.Activities.DetailActivity;
import ir.arapp.arappofficial.Data.CommentData;
import ir.arapp.arappofficial.Data.HomeServicesData;
import ir.arapp.arappofficial.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>
{
    Context context;
    ArrayList<CommentData> commentData;

    public CommentAdapter(Context context, ArrayList<CommentData> commentData)
    {
        this.context = context;
        this.commentData = commentData;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;

        if (viewType == R.layout.comment_card_services)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_services, parent, false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_services_more, parent, false);
        }

        return new CommentHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position)
    {
        if (position == commentData.size() && commentData.size() >= 4)
        {
            holder.moreCardView.setOnClickListener(view ->
            {
                StyleableToast.makeText(context, "مشاهده تمامی نظرات کاربران", Toast.LENGTH_SHORT, R.style.toastTheme).show();
            });
        }
        else
        {
            CommentData data = commentData.get(position);

            holder.serviceImage.setImageResource(data.getImage());
            holder.mName.setText(data.getName());
            holder.mText.setOnTouchListener((view, motionEvent) ->
            {
                // Disallow the touch request for parent scroll on touch of child view
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            });
            holder.mText.setMovementMethod(new ScrollingMovementMethod());
            holder.mText.setText(data.getText());
            holder.mDate.setText(data.getDate());
            holder.mRate.setText(data.getRate()+ "");
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return (commentData.size() >= 4) ? commentData.size() + 1 : commentData.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return (position == commentData.size() && commentData.size() >= 4)
                ? R.layout.comment_card_services_more : R.layout.comment_card_services;
    }

    public static class CommentHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        CardView moreCardView;
        ImageView serviceImage;
        TextView mName;
        TextView mText;
        TextView mDate;
        TextView mRate;
        ProgressBar progressBar;
        ScrollView scrollView;
        @SuppressLint("ClickableViewAccessibility")
        public CommentHolder(@NonNull View itemView)
        {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewComment);
            moreCardView = itemView.findViewById(R.id.cardViewCommentMore);
            serviceImage =itemView.findViewById(R.id.profileCommentImage);
            mName = itemView.findViewById(R.id.profileNameComment);
            mText = itemView.findViewById(R.id.textComment);
            mDate = itemView.findViewById(R.id.timeComment);
            mRate = itemView.findViewById(R.id.rateUserComment);
            progressBar = itemView.findViewById(R.id.progressBarProfileComment);
            scrollView = itemView.findViewById(R.id.scrollCommentText);
        }
    }
}
