package com.iti.mobile.triporganizer.details;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.databinding.FragmentDetailsBinding;
import com.iti.mobile.triporganizer.utils.AlarmUtils;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.iti.mobile.triporganizer.utils.DetailsUtils.generateDates;
import static com.iti.mobile.triporganizer.utils.DetailsUtils.isValidData;
import static com.iti.mobile.triporganizer.utils.Flags.DATE1;
import static com.iti.mobile.triporganizer.utils.Flags.DATE2;
import static com.iti.mobile.triporganizer.utils.Flags.DETAILSTRIP_FRAGMENTBINDING;
import static com.iti.mobile.triporganizer.utils.Flags.EDIT_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Flags.TIME1;
import static com.iti.mobile.triporganizer.utils.Flags.TIME2;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_ROUND;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_SINGLE;
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Tags.DATE;
import static com.iti.mobile.triporganizer.utils.Tags.DATE_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.END_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.START_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.TIME;
import static com.iti.mobile.triporganizer.utils.Tags.TIME_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.TRIP_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DetailsFragment";

    private AutocompleteSupportFragment startPointAutocompleteFragment;
    private AutocompleteSupportFragment endPointAutocompleteFragment;

    @Inject
    ViewModelProviderFactory providerFactory;
    private DetailsViewModel detailsViewModel;
    private FragmentDetailsBinding binding;

    @Inject
    FirebaseAuth firebaseAuth;

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
    private boolean isRound = false;
    private Trip trip;
    private LocationData locationData;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hour1, minute1, hour2, minute2, year1, year2, month1, month2, day1, day2;


    private PlacesClient placesClient;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        detailsViewModel=new ViewModelProvider(this,providerFactory).get(DetailsViewModel.class);

        tripTypeChoice=TRIP_TYPE_SINGLE;
        tripActionChoice=VIEW_TRIP_FLAG;
        notesList=new ArrayList<>();
        controller = Navigation.findNavController(view);
        trip = new Trip();
        locationData = new LocationData();
        setUpViews(view);
        setUpDummyData();
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
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    private void showNotesList(View root) {
        if (notesList.size() > 0) {
            noteAdapter = new NoteAdapter(getContext(), notesList);
            binding.notesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.notesRecyclerview.setAdapter(noteAdapter);
        }
    }

    public void setUpViews(View root) {
        binding.bkImageView.setOnClickListener(this);
        binding.editBtn.setOnClickListener(this);
        binding.viewBtn.setOnClickListener(this);
        binding.saveTripFab.setOnClickListener(this);
        binding.date1Tv.setOnClickListener(this);
        binding.date2Tv.setOnClickListener(this);
        binding.time1Tv.setOnClickListener(this);
        binding.time2Tv.setOnClickListener(this);
        binding.singleBtn.setOnClickListener(this);
        binding.roundBtn.setOnClickListener(this);
        binding.addNoteImageView.setOnClickListener(this);

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
            case R.id.saveTripFab:
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
                    binding.notesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.notesRecyclerview.setAdapter(noteAdapter);
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
                                binding.time1Tv.setText(hourOfDay + ":" + minute);
                                break;
                            case 4:
                                hour2=hourOfDay;
                                minute2=minute;
                                binding.time2Tv.setText(hourOfDay + ":" + minute);
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
                        binding.date1Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                    case 2:
                        year2=year;
                        month2=month+1;
                        day2=dayOfMonth;
                        binding.date2Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                }
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    private void goToHomeActivity() {

    }
    private void setUpDummyData(){
        binding.tripNameTv.setText("Trip1");
        binding.tripNameEt.setText("Trip1");
        ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setText("cairo");
        binding.startPointTv.setText("cairo");
        ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setText("alex");
        binding.endPointTv.setText("Alex");
        binding.date1ViewTv.setText("1/3/2020");
        binding.date1Tv.setText("1/3/2020");
        binding.time1ViewTv.setText("10:40");
        binding.time1Tv.setText("10:40");
        binding.date2ViewTv.setText("2/3/2020");
        binding.date2Tv.setText("1/3/2020");
        binding.time2ViewTv.setText("11:30");
        binding.time2Tv.setText("10:40");

    }
    private void saveTrip() {
        String tripName=binding.tripNameEt.getText().toString();
        String date1=binding.date1Tv.getText().toString().trim();
        String time1=binding.time1Tv.getText().toString().trim();
        String date2=binding.date2Tv.getText().toString().trim();
        String time2=binding.time2Tv.getText().toString().trim();
        Date formatedDate1 = null;
        Date formatedDate2 = null;
        try {
            if (!date1.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                date1 = date1 + " " + time1;
                formatedDate1 = format.parse(date1);
                Log.d(TAG, formatedDate1.toString());
                Log.d(TAG, "saved formatted" + date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isRound) {
            try {
                if (!date2.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    date2 = date2 + " " + time2;
                    formatedDate2 = format.parse(date2);
                    Log.d(TAG, formatedDate2.toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        setLocationData(formatedDate1,startPonitLat,startPonitLng,endPonitLat,endPonitLng,startAddress,endAddress);
        if(isRound){
            setLocationData(formatedDate1,endPonitLat,endPonitLng,startPonitLat,startPonitLng,endAddress,startAddress);
        }
        setTripData(tripName,isRound,firebaseAuth.getCurrentUser().getUid(),"UpComing",locationData);

        isValidData(trip,isRound,null,binding,startPointAutocompleteFragment,endPointAutocompleteFragment,generateErrorsMessages(),DETAILSTRIP_FRAGMENTBINDING,generateDates(date1,date2,time1,time2));
        if (isValidData(trip,isRound,null,binding,startPointAutocompleteFragment,endPointAutocompleteFragment,generateErrorsMessages(),DETAILSTRIP_FRAGMENTBINDING,generateDates(date1,date2,time1,time2))) {
            detailsViewModel.updateTrip(trip);
        }
    }

    private HashMap<String,String> generateErrorsMessages(){
        HashMap<String, String> hash_map = new HashMap<String, String>();
        hash_map.put(TRIP_NAME,getResources().getString(R.string.plzEnterTripName));
        hash_map.put(START_POINT,getResources().getString(R.string.plzEnterStartPoint));
        hash_map.put(END_POINT,getResources().getString(R.string.plzEnterEndPoint));
        hash_map.put(DATE,getResources().getString(R.string.plzPickDate));
        hash_map.put(TIME,getResources().getString(R.string.plzPickTime));
        hash_map.put(DATE_COMPARE,getString(R.string.plzPickValidStartDate));
        hash_map.put(TIME_COMPARE,getString(R.string.plzPickValidStartTime));
        return hash_map;
    }

    private void setTripData(String tripName, boolean isRound, String uid, String upComing, LocationData locationData) {
        trip.setTripName(tripName);
        trip.setRound(isRound);
        trip.setUserId(uid);
        trip.setStatus(upComing);
        trip.setLocationData(locationData);
    }
    private void setLocationData(Date formatedDate1, double startPonitLat, double startPonitLng, double endPonitLat, double endPonitLng, String startAddress, String endAddress) {
        locationData.setStartDate(formatedDate1);
        locationData.setStartTripStartPointLat(startPonitLat);
        locationData.setStartTripStartPointLng(startPonitLng);
        locationData.setStartTripEndPointLat(endPonitLat);
        locationData.setStartTripEndPointLng(endPonitLng);
        locationData.setStartTripAddressName(startAddress);
        locationData.setStartTripEndAddressName(endAddress);
    }

    private void viewTrip(int tripTypeChoice, int tripActionChoice) {
        tripActionChoice = VIEW_TRIP_FLAG;
        focusViewButton();
        showTextViewHideEditText();
        showTripType(tripTypeChoice,tripActionChoice);

    }

    private void editTrip(int tripTypeChoice, int tripActionChoice) {
       tripActionChoice= EDIT_TRIP_FLAG;
       focusEditButton();
       showEditTextHideTextView();
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
        isRound = false;
        focusSingleButton();
        hideSecondDateTimeViews();
    }

    private void showRoundTrip(int tripActionChoice) {
        isRound = true;
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

    private void showTextViewHideEditText() {
        binding.addNoteImageView.setVisibility(GONE);
        binding.tripNameEt.setVisibility(GONE);
        binding.tripTitleTv.setVisibility(VISIBLE);
        binding.tripNameTv.setVisibility(VISIBLE);
        binding.startPointConstraintlayout.setVisibility(GONE);
        binding.startPointTitleTv.setVisibility(VISIBLE);
        binding.startPointTv.setVisibility(VISIBLE);
        binding.endPointConstraintlayout.setVisibility(GONE);
        binding.endPointTitleTv.setVisibility(VISIBLE);
        binding.endPointTv.setVisibility(VISIBLE);
        binding.date1Tv.setVisibility(GONE);
        binding.date1TitleTv.setVisibility(VISIBLE);
        binding.date1ViewTv.setVisibility(VISIBLE);
        binding.time1Tv.setVisibility(GONE);
        binding.time1TitleTv.setVisibility(VISIBLE);
        binding.time1ViewTv.setVisibility(VISIBLE);
        binding.saveTripFab.setVisibility(GONE);
    }

    private void showEditTextHideTextView() {
        binding.addNoteImageView.setVisibility(VISIBLE);
        binding.tripNameEt.setVisibility(VISIBLE);
        binding.tripTitleTv.setVisibility(GONE);
        binding.tripNameTv.setVisibility(GONE);
        binding.startPointConstraintlayout.setVisibility(VISIBLE);
        binding.startPointTitleTv.setVisibility(GONE);
        binding.startPointTv.setVisibility(GONE);
        binding.endPointConstraintlayout.setVisibility(VISIBLE);
        binding.endPointTitleTv.setVisibility(GONE);
        binding.endPointTv.setVisibility(GONE);
        binding.date1Tv.setVisibility(VISIBLE);
        binding.date1TitleTv.setVisibility(GONE);
        binding.date1ViewTv.setVisibility(GONE);
        binding.time1Tv.setVisibility(VISIBLE);
        binding.time1TitleTv.setVisibility(GONE);
        binding.time1ViewTv.setVisibility(GONE);
        binding.saveTripFab.setVisibility(VISIBLE);
    }

    private void showSecondDateTimeTextView() {
        binding.date2Tv.setVisibility(GONE);
        binding.date2TitleTv.setVisibility(VISIBLE);
        binding.date2ViewTv.setVisibility(VISIBLE);
        binding.time2Tv.setVisibility(GONE);
        binding.time2TitleTv.setVisibility(VISIBLE);
        binding.time2ViewTv.setVisibility(VISIBLE);
    }

    private void showSecondDateTimeEditText() {
        binding.date2Tv.setVisibility(VISIBLE);
        binding.date2TitleTv.setVisibility(GONE);
        binding.date2ViewTv.setVisibility(GONE);
        binding.time2TitleTv.setVisibility(GONE);
        binding.time2Tv.setVisibility(VISIBLE);
        binding.time2ViewTv.setVisibility(GONE);
    }


    private void hideSecondDateTimeViews() {
        binding.date2Tv.setVisibility(GONE);
        binding.date2TitleTv.setVisibility(GONE);
        binding.date2ViewTv.setVisibility(GONE);
        binding.time2TitleTv.setVisibility(GONE);
        binding.time2Tv.setVisibility(GONE);
        binding.time2ViewTv.setVisibility(GONE);
    }
    private void focusSingleButton() {
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }

    private void focusRoundButton() {
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }
    private void focusEditButton() {
        binding.editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.editBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_white_24dp, 0, 0, 0);
        binding.viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        binding.viewBtn.setTextColor(getResources().getColor(R.color.darktxt));
        binding.viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_gray_24dp, 0, 0, 0);

    }

    private void focusViewButton() {
        binding.viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.viewBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_white_24dp, 0, 0, 0);
        binding.editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        binding.editBtn.setTextColor(getResources().getColor(R.color.darktxt));
        binding.editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_gray_24dp, 0, 0, 0);
    }


}
