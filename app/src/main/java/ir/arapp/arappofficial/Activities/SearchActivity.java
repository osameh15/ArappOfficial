package ir.arapp.arappofficial.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.lang.reflect.Field;
import java.util.Objects;

import ir.arapp.arappofficial.R;

public class SearchActivity extends AppCompatActivity
{
    private MaterialSearchBar searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Hooks
        searchView = findViewById(R.id.search_view);

        //Search View ...
        searchView.openSearch();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

//    private void searchViewTypeFace()
//    {
//        try
//        {
//            final Field placeHolder = searchView.getClass().getDeclaredField("placeHolder");
//            placeHolder.setAccessible(true);
//            final TextView textView = (TextView) placeHolder.get(searchView);
//            Objects.requireNonNull(textView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//
//            //fix trouble number 1 paddingLeft
//            textView.setPadding(0, 0, 0, 0);
//            final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
//            layoutParams.setMargins(0, 0, 0, 0);
//
//            //fix trouble number 2 font-family and number 3 Finally, disable boldness?
//            Typeface typeface = Typeface.createFromAsset(getAssets(), "font/vazir.ttf");
//            textView.setTypeface(typeface);
//
//            textView.setText(getResources().getString(R.string.search));
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}