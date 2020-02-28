package com.iti.mobile.triporganizer.details;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.iti.mobile.triporganizer.utils.Flags.DATE1;
import static com.iti.mobile.triporganizer.utils.Flags.DATE2;
import static com.iti.mobile.triporganizer.utils.Flags.EDIT_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Flags.TIME1;
import static com.iti.mobile.triporganizer.utils.Flags.TIME2;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_ROUND;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_SINGLE;
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DetailsFragment";
    private Button editBtn;
    private Button viewBtn;
    private ImageView bkImageView;
    private FloatingActionButton saveFabBtn;

    private AutocompleteSupportFragment startPointAutocompleteFragment;
    private AutocompleteSupportFragment endPointAutocompleteFragment;
    private ConstraintLayout startPointConstraintlayout;
    private ConstraintLayout endPointConstraintlayout;

    private TextView tripNameTv;
    private TextView startPointTv;
    private TextView endPointTv;
    private TextView date1ViewTv;
    private TextView time1ViewTv;
    private TextView date2ViewTv;
    private TextView time2ViewTv;

    private EditText tripNameEt;
    private TextView date1Tv;
    private TextView time1TV;
    private TextView date2Tv;
    private TextView time2Tv;

    private TextView date1TitleTv;
    private TextView time1TitleTv;
    private TextView date2TitleTv;
    private TextView time2TitleTv;

    private Button singleBtn;
    private Button roundBtn;
    private ImageView addNoteImageView;
    private RecyclerView notes_recyclerview;
    private List<String> notesList;

    NavController controller;
    NoteAdapter noteAdapter;
    private int tripTypeChoice;
    private int tripActionChoice;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private PlacesClient placesClient;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tripTypeChoice=TRIP_TYPE_SINGLE;
        tripActionChoice=VIEW_TRIP_FLAG;
        controller = Navigation.findNavController(view);
        switch (tripActionChoice) {
            case VIEW_TRIP_FLAG:
                viewTrip(tripTypeChoice, tripActionChoice);
                break;
            case EDIT_TRIP_FLAG:
                editTrip(tripTypeChoice, tripActionChoice);
                break;
        }
    }

    private void showTripType(int tripType,int tripAction) {
        tripActionChoice=tripAction;
        switch (tripType) {
            case TRIP_TYPE_SINGLE:
                showSingleTrip();
                break;
            case TRIP_TYPE_ROUND:
                showRoundTrip(tripActionChoice);
                break;
        }
    }
    private void showSingleTrip() {
        focusSingleButton();
        hideSecondDateTimeViews();
    }

    private void showRoundTrip(int tripActionChoice) {
        focusRoundButton();
        switch (tripActionChoice) {
            case VIEW_TRIP_FLAG:
                showSecondDateTimeTextView();
                break;
            case EDIT_TRIP_FLAG:
                showSecondDateTimeEditText();
                break;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_details, container, false);
        setUpViews(root);
        showNotesList(root);
        return root;
    }

    private void showNotesList(View root) {
        notesList=new ArrayList<>();
        notesList.add("one");
        notesList.add("two");
        if (notesList.size() > 0) {
            notes_recyclerview = root.findViewById(R.id.notes_recyclerview);
            noteAdapter = new NoteAdapter(getContext(), notesList);
            notes_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            notes_recyclerview.setAdapter(noteAdapter);
        }
    }

    public void setUpViews(View root) {
        bkImageView = root.findViewById(R.id.bkImageView);
        bkImageView.setOnClickListener(this);
        editBtn = root.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
        viewBtn = root.findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(this);
        saveFabBtn = root.findViewById(R.id.saveFab);
        saveFabBtn.setOnClickListener(this);

        tripNameEt = root.findViewById(R.id.tripNameEt);
        startPointConstraintlayout=root.findViewById(R.id.startPointConstraintlayout);
        endPointConstraintlayout=root.findViewById(R.id.endPointConstraintlayout);
        date1Tv = root.findViewById(R.id.date1Tv);
        date1Tv.setOnClickListener(this);
        date2Tv = root.findViewById(R.id.date2Tv);
        date2Tv.setOnClickListener(this);
        time1TV = root.findViewById(R.id.time1Tv);
        time1TV.setOnClickListener(this);
        time2Tv = root.findViewById(R.id.time2Tv);
        time2Tv.setOnClickListener(this);

        tripNameTv=root.findViewById(R.id.tripNameTv);
        startPointTv=root.findViewById(R.id.startPointTv);
        endPointTv=root.findViewById(R.id.endPointTv);
        date1ViewTv=root.findViewById(R.id.date1ViewTv);
        time1ViewTv=root.findViewById(R.id.time1ViewTv);
        date2ViewTv=root.findViewById(R.id.date2ViewTv);
        time2ViewTv=root.findViewById(R.id.time2ViewTv);

        date1TitleTv = root.findViewById(R.id.date1TitleTv);
        time1TitleTv = root.findViewById(R.id.time1TitleTv);
        date2TitleTv = root.findViewById(R.id.date2TitleTv);
        time2TitleTv = root.findViewById(R.id.time2TitleTv);


        singleBtn = root.findViewById(R.id.singleBtn);
        singleBtn.setOnClickListener(this);
        roundBtn = root.findViewById(R.id.roundBtn);
        roundBtn.setOnClickListener(this);
        addNoteImageView = root.findViewById(R.id.addNoteImageView);
        addNoteImageView.setOnClickListener(this);

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.google_places_api_key));
        }
        placesClient = Places.createClient(getContext());
        startPointAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.startAutoCompleteFragment);
        endPointAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.endAutoCompleteFragment);
        handleStartPointPlacesSelected(startPointAutocompleteFragment);
        handleEndPointPlacesSelected(endPointAutocompleteFragment);
    }

    private void handleStartPointPlacesSelected(AutocompleteSupportFragment startPointAutocompleteFragment) {
        startPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        startPointAutocompleteFragment.setHint(getString(R.string.estart_point));
        startPointAutocompleteFragment.setCountry("EG");
        startPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        startPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void handleEndPointPlacesSelected(AutocompleteSupportFragment endPointAutocompleteFragment) {
        endPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        endPointAutocompleteFragment.setHint(getString(R.string.eend_point));
        endPointAutocompleteFragment.setCountry("EG");
        endPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        endPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bkImageView:
                goToHomeActivity();
                break;
            case R.id.editBtn:
                editTrip(tripTypeChoice, tripActionChoice);
                break;
            case R.id.viewBtn:
                viewTrip(tripTypeChoice, tripActionChoice);
                break;
            case R.id.date1Tv:
                showDatePicker(DATE1);
                break;
            case R.id.time1Tv:
                showTime(TIME1);
                break;
            case R.id.date2Tv:
                showDatePicker(DATE2);
                break;
            case R.id.time2Tv:
                showTime(TIME2);
                break;
            case R.id.saveFab:
                saveTrip();
                break;
            case R.id.singleBtn:
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundTrip(tripActionChoice);
                break;

        }
    }

    private void showTime(int time) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch(time){
                            case 3:
                                time1TV.setText(hourOfDay + ":" + minute);
                                break;
                            case 4:
                                time2Tv.setText(hourOfDay + ":" + minute);
                                break;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void showDatePicker(int date) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
              switch(date){
                  case 1:
                      date1Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                      break;
                  case 2:
                      date2Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                      break;
              }
            }
        },mYear,mMonth,mDay);

        datePickerDialog.show();
    }

    private void goToHomeActivity() {

    }

    private void saveTrip() {

    }

    private void viewTrip(int tripTypeChoice, int tripActionChoice) {
        tripActionChoice = VIEW_TRIP_FLAG;
        focusViewButton();
        disableEditText();
        showTripType(tripTypeChoice,tripActionChoice);

    }

    private void editTrip(int tripTypeChoice, int tripActionChoice) {
       tripActionChoice= EDIT_TRIP_FLAG;
       focusEditButton();
       enableEditText();
       showTripType(tripTypeChoice,tripActionChoice);

    }

    private void disableEditText() {
        addNoteImageView.setVisibility(GONE);
        tripNameEt.setVisibility(GONE);
        tripNameTv.setVisibility(VISIBLE);
        startPointConstraintlayout.setVisibility(GONE);
        startPointTv.setVisibility(VISIBLE);
        endPointConstraintlayout.setVisibility(GONE);
        endPointTv.setVisibility(VISIBLE);
        date1Tv.setVisibility(GONE);
        date1ViewTv.setVisibility(VISIBLE);
        time1TV.setVisibility(GONE);
        time1ViewTv.setVisibility(VISIBLE);
        saveFabBtn.setVisibility(GONE);
    }

    private void enableEditText() {
        addNoteImageView.setVisibility(VISIBLE);
        tripNameEt.setVisibility(VISIBLE);
        tripNameTv.setVisibility(GONE);
        startPointConstraintlayout.setVisibility(VISIBLE);
        startPointTv.setVisibility(GONE);
        endPointConstraintlayout.setVisibility(VISIBLE);
        endPointTv.setVisibility(GONE);
        date1Tv.setVisibility(VISIBLE);
        date1ViewTv.setVisibility(GONE);
        time1TV.setVisibility(VISIBLE);
        time1ViewTv.setVisibility(GONE);
        saveFabBtn.setVisibility(VISIBLE);
    }

    private void showSecondDateTimeTextView() {
        date2TitleTv.setVisibility(VISIBLE);
        date2Tv.setVisibility(GONE);
        date2ViewTv.setVisibility(VISIBLE);
        time2TitleTv.setVisibility(VISIBLE);
        time2Tv.setVisibility(GONE);
        time2ViewTv.setVisibility(VISIBLE);
    }

    private void showSecondDateTimeEditText() {
        date2TitleTv.setVisibility(VISIBLE);
        date2Tv.setVisibility(VISIBLE);
        date2ViewTv.setVisibility(GONE);
        time2TitleTv.setVisibility(VISIBLE);
        time2Tv.setVisibility(VISIBLE);
        time2ViewTv.setVisibility(GONE);
    }


    private void hideSecondDateTimeViews() {
        date2TitleTv.setVisibility(GONE);
        date2Tv.setVisibility(GONE);
        date2ViewTv.setVisibility(GONE);
        time2TitleTv.setVisibility(GONE);
        time2Tv.setVisibility(GONE);
        time2ViewTv.setVisibility(GONE);
    }
    private void focusSingleButton() {
        singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }

    private void focusRoundButton() {
        roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        singleBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }
    private void focusEditButton() {
        editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        editBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_white_24dp, 0, 0, 0);
        viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        viewBtn.setTextColor(getResources().getColor(R.color.darktxt));
        viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_gray_24dp, 0, 0, 0);

    }

    private void focusViewButton() {
        viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        viewBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_white_24dp, 0, 0, 0);
        editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        editBtn.setTextColor(getResources().getColor(R.color.darktxt));
        editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_gray_24dp, 0, 0, 0);
    }


}
