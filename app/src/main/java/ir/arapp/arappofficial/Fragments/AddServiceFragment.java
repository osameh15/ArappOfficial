package ir.arapp.arappofficial.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import ir.arapp.arappofficial.R;
import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker;
import me.adawoud.bottomsheettimepicker.OnTimeRangeSelectedListener;

public class AddServiceFragment extends Fragment implements View.OnClickListener, OnTimeRangeSelectedListener
{
    //Variables
    private AppCompatSpinner categorySpinner;
    private AppCompatSpinner buildDateSpinner;
    private Typeface typeface;
    private CircularProgressButton timeActivity;

    //Spinner List
    private List<String> categoryList;
    private List<String> buildDateList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        typeface = ResourcesCompat.getFont(Objects.requireNonNull(getActivity()).getApplicationContext(), R.font.vazir);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_service, container, false);

        //Hooks
        categorySpinner = view.findViewById(R.id.categorySpinner);
        timeActivity = view.findViewById(R.id.buttonSubmitTimeActivity);
        buildDateSpinner = view.findViewById(R.id.buildDateSpinner);

        //Spinner List defining
        categoryList = new ArrayList<>();
        buildDateList = new ArrayList<>();
        //Spinner Adapter
        spinnerAdapter();

        //Bottom sheet dialog (time picker)
        timeActivity.setOnClickListener(this);
        return view;
    }

    //Spinner list and adapter ...
    private void spinnerAdapter()
    {
        categoryList.addAll(Arrays.asList(getResources().getStringArray(R.array.category_list)));
        buildDateList.addAll(Arrays.asList(getResources().getStringArray(R.array.year_list)));

        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, categoryList)
                {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getDropDownView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }
                };
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);

        categorySpinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1 = new
                ArrayAdapter<String>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.custom_spinner_layout, buildDateList)
                {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                    {
                        View view = super.getDropDownView(position, convertView, parent);

                        ((TextView) view).setTypeface(typeface);

                        return  view;
                    }
                };
        arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_layout_dropdown);

        buildDateSpinner.setAdapter(arrayAdapter1);
    }

    //Time Picker
    private void timePicker()
    {
        String tagBottomSheetTimeRangePicker = "TAG_TIME_PICKER";
        BottomSheetTimeRangePicker
                .Companion
                .tabLabels("باز شدن", "بسته شدن")
                .doneButtonLabel("تایید")
                .startTimeInitialHour(8)
                .startTimeInitialMinute(0)
                .endTimeInitialHour(20)
                .endTimeInitialMinute(0)
                .newInstance(this, DateFormat.is24HourFormat(getContext()))
                .show(Objects.requireNonNull(getFragmentManager()), tagBottomSheetTimeRangePicker);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonSubmitTimeActivity:
                timePicker();
                break;
            case R.id.buttonSubmitAddService:
                break;
        }
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMinute, int endHour, int endMinute)
    {
        //save activity time of service ... open and close
    }
}
