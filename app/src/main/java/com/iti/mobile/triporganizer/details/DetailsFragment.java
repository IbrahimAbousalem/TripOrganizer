package com.iti.mobile.triporganizer.details;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

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


    @Inject
    ViewModelProviderFactory providerFactory;
    private DetailsViewModel detailsViewModel;

    NavController controller;
    NoteAdapter noteAdapter;
    private List<Note> notesList;
    private int tripTypeChoice;
    private int tripActionChoice;



    private double startPonitLat;
    private double startPonitLng;
    private double endPonitLat;
    private double endPonitLng;
    private String startAddress;
    private String endAddress;
    private String name;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hour1, minute1, hour2, minute2, year1, year2, month1, month2, day1, day2;

    private Trip trip;
    private LocationData locationData;
    private PlacesClient placesClient;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        detailsViewModel = new ViewModelProvider(this, providerFactory).get(DetailsViewModel.class);
        tripTypeChoice=TRIP_TYPE_SINGLE;
        tripActionChoice=VIEW_TRIP_FLAG;
        notesList=new ArrayList<>();
        controller = Navigation.findNavController(view);
        trip = new Trip();
        locationData = new LocationData();
        switch (tripActionChoice) {
            case VIEW_TRIP_FLAG:
                viewTrip(tripTypeChoice, tripActionChoice);
                break;
            case EDIT_TRIP_FLAG:
                editTrip(tripTypeChoice, tripActionChoice);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_details, container, false);
        setUpViews(root);
        //showNotesList(root);
        return root;
    }

    // edit ->> 3nde trip bs m3ndesh notes or 3nde  bltaly lma ad8t ok y3ml save ll notes + update ll trips
    // (Missing) new trip -> ana m3ndesh trip wla notes f deh momkn n3mlha function lw7dha mn sf7a lw7da 3lshan el save y3ml add l trip nad add l notes ... + el alamrm
    private void showNotesList(View root) {
        if (notesList.size() > 0) {
            notes_recyclerview = root.findViewById(R.id.notes_recyclerview);
            noteAdapter = new NoteAdapter(getContext(), notesList);
            notes_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            notes_recyclerview.setAdapter(noteAdapter);
        }
    }

    public void setUpViews(View root) {
        notes_recyclerview = root.findViewById(R.id.notes_recyclerview);
        bkImageView = root.findViewById(R.id.bkImageView);
        bkImageView.setOnClickListener(this);
        editBtn = root.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
        viewBtn = root.findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(this);
        saveFabBtn = root.findViewById(R.id.addTripFab);
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
        startPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
               Place.Field.LAT_LNG,Place.Field.ADDRESS));
        startPointAutocompleteFragment.setHint(getString(R.string.estart_point));
        startPointAutocompleteFragment.setCountry("EG");
        startPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        startPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+", "+
                place.getLatLng().latitude+", "+place.getLatLng().longitude);

                startPonitLat=place.getLatLng().latitude;
                startPonitLng=place.getLatLng().longitude;
                startAddress=place.getName();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void handleEndPointPlacesSelected(AutocompleteSupportFragment endPointAutocompleteFragment) {
        endPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));
        endPointAutocompleteFragment.setHint(getString(R.string.eend_point));
        endPointAutocompleteFragment.setCountry("EG");
        endPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        endPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId()+", "+
                        place.getLatLng().latitude+", "+place.getLatLng().longitude);

                endPonitLat=place.getLatLng().latitude;
                endPonitLng=place.getLatLng().longitude;
                endAddress=place.getName();

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
            case R.id.addTripFab:
                saveTrip();
                break;
            case R.id.singleBtn:
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundTrip(tripActionChoice);
                break;
            case R.id.addNoteImageView:
                addNote();

        }
    }

    private void addNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView titleTv = new TextView(getContext());
        titleTv.setText(getResources().getString(R.string.enternote));
        titleTv.setPadding(20, 30, 20, 30);
        titleTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        builder.setCustomTitle(titleTv);
        final EditText input = new EditText(getContext());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();
                if(inputText.isEmpty())return;
                Note note = new Note();
                note.setMessage(input.getText().toString());
                note.setStatus(false);
                notesList.add(note);
                if (notesList.size() > 0) {
                    noteAdapter = new NoteAdapter(getContext(), notesList);
                    noteAdapter.setOnRecyclerViewItemClickListener(new NoteAdapter.onRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClickListener(View v, int position) {
                            deleteNote(position);
                        }
                    });
                    notes_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    notes_recyclerview.setAdapter(noteAdapter);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteNote(int position) {
        notesList.remove(position);
        noteAdapter.notifyItemRemoved(position);
        noteAdapter.notifyItemRangeChanged(position,notesList.size());
    }

    private void showTime(int time) {
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
                                hour1 = hourOfDay;
                                minute1 = minute;
                                time1TV.setText(hourOfDay + ":" + minute);
                                break;
                            case 4:
                                hour2=hourOfDay;
                                minute2=minute;
                                time2Tv.setText(hourOfDay + ":" + minute);
                                break;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void showDatePicker(int date) {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTS"));
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        c.getTimeInMillis();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                switch (date) {
                    case 1:
                        c.set(year, month, dayOfMonth);
                        year1=year;
                        month1=month+1;
                        day1=dayOfMonth;
                        date1Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                    case 2:
                        year2=year;
                        month2=month+1;
                        day2=dayOfMonth;
                        date2Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                }
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void goToHomeActivity() {

    }

    private void saveTrip() {
        //TODO : check the roundTrip after the startTrip
        String tripName=tripNameEt.getText().toString();
        String date1=date1Tv.getText().toString();
        String time1=time1TV.getText().toString();
        String date2=date2Tv.getText().toString();
        String time2=time2Tv.getText().toString();
        try {
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy HH:mm");
            date1 = date1 + " " + time1;
            Date formatedDate=format.parse(date1);
            Log.d(TAG, formatedDate.toString());
            locationData.setStartDate(formatedDate);
            Log.d(TAG,"saved formatted"+date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        locationData.setStartTripStartPointLat(startPonitLat);
        locationData.setStartTripStartPointLng(startPonitLng);
        locationData.setStartTripEndPointLat(endPonitLat);
        locationData.setStartTripEndPointLng(endPonitLng);
        locationData.setStartTripAddressName(startAddress);
        locationData.setStartTripEndAddressName(endAddress);
        trip.setTripName(tripName);
        trip.setRound(false);
        trip.setUserId("hZDY3CF3aWU5WjC6fNHmck2dBz02");
        trip.setStatus("UpComing");
        trip.setLocationData(locationData);
        detailsViewModel.addTripAndNotes(trip, notesList);
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
