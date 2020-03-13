package com.iti.mobile.triporganizer.details;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.MapperClass;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.databinding.FragmentDetailsBinding;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.iti.mobile.triporganizer.utils.Flags.DATE1;
import static com.iti.mobile.triporganizer.utils.Flags.DATE2;
import static com.iti.mobile.triporganizer.utils.Flags.TIME1;
import static com.iti.mobile.triporganizer.utils.Flags.TIME2;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DetailsFragment";

    private AutocompleteSupportFragment startPointAutocompleteFragment;
    private AutocompleteSupportFragment endPointAutocompleteFragment;
    private TripAndLocation receivedTripAndLocation;

    @Inject
    ViewModelProviderFactory providerFactory;
    private DetailsViewModel detailsViewModel;
    private FragmentDetailsBinding binding;


    NavController controller;
    NoteAdapter noteAdapter;
    private List<Note> notesList;
    private boolean isRound =false;

    private double startPonitLat;
    private double startPonitLng;
    private double endPonitLat;
    private double endPonitLng;
    private String startAddress;
    private String endAddress;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hour1, minute1, hour2, minute2, year1, year2, month1, month2, day1, day2;
    Date dateFormat1;
    Date dateFormat2;

    private PlacesClient placesClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        detailsViewModel = new ViewModelProvider(this, providerFactory).get(DetailsViewModel.class);
        controller = Navigation.findNavController(view);
        receivedTripAndLocation = DetailsFragmentArgs.fromBundle(getArguments()).getTripAndLocation();
        Calendar startDateCalendar=Calendar.getInstance();
        Date recievedStartDate=receivedTripAndLocation.getLocationDataList().getStartDate();
        dateFormat1=recievedStartDate;
        receivedTripAndLocation.getLocationDataList().setStartDate(dateFormat1);
        startDateCalendar.setTime(recievedStartDate);
        hour1=startDateCalendar.get(Calendar.HOUR_OF_DAY);
        minute1=startDateCalendar.get(Calendar.MINUTE);
        year1=startDateCalendar.get(Calendar.YEAR);
        month1=startDateCalendar.get(Calendar.MONTH);
        day1=startDateCalendar.get(Calendar.DAY_OF_MONTH);
        Calendar endDateCalendar=Calendar.getInstance();
        Date recievedEndDate=receivedTripAndLocation.getLocationDataList().getRoundDate();
        dateFormat2=recievedEndDate;
        receivedTripAndLocation.getLocationDataList().setStartDate(dateFormat2);
        endDateCalendar.setTime(recievedEndDate);
        hour2=endDateCalendar.get(Calendar.HOUR_OF_DAY);
        minute2=endDateCalendar.get(Calendar.MINUTE);
        year2=endDateCalendar.get(Calendar.YEAR);
        month2=endDateCalendar.get(Calendar.MONTH);
        day2=endDateCalendar.get(Calendar.DAY_OF_MONTH);
        startPonitLat=receivedTripAndLocation.getLocationDataList().getStartTripStartPointLat();
        startPonitLng=receivedTripAndLocation.getLocationDataList().getStartTripStartPointLng();
        startAddress=receivedTripAndLocation.getLocationDataList().getStartTripAddressName();
        endPonitLat=receivedTripAndLocation.getLocationDataList().getStartTripEndPointLat();
        endPonitLng=receivedTripAndLocation.getLocationDataList().getStartTripEndPointLng();
        endAddress=receivedTripAndLocation.getLocationDataList().getStartTripEndAddressName();
        notesList=new ArrayList<>();
        setUpViews();
        showViewLayout(receivedTripAndLocation.getLocationDataList().isRound());
        initRecycler();
        detailsViewModel.getAllNotes(receivedTripAndLocation.getTrip().getId()).observe(requireActivity(), notes -> {
            noteAdapter = new NoteAdapter(getContext(), notes);
            binding.notesRecyclerview.setAdapter(noteAdapter);
        });
    }

    private void initRecycler() {
        binding.notesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showViewLayout(boolean round) {
        focusViewButton();
        showViewData();
        showTextViewHideEditText(round);
    }

    private void showViewData() {
        binding.tripNameTv.setText(receivedTripAndLocation.getTrip().getTripName());
        binding.startPointTv.setText(receivedTripAndLocation.getLocationDataList().getStartTripAddressName());
        binding.endPointTv.setText(receivedTripAndLocation.getLocationDataList().getStartTripEndAddressName());
    }

    private void showEditLayout(boolean round){
        focusEditButton();
        showEditData();
        showEditTextHideTextView(round);
    }

    private void showEditData() {
        binding.tripNameEt.setText(receivedTripAndLocation.getTrip().getTripName());
        ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setText(receivedTripAndLocation.getLocationDataList().getStartTripAddressName());
        ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setText(receivedTripAndLocation.getLocationDataList().getStartTripEndAddressName());
        ((EditText)endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
        ((EditText)startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
        binding.date1Tv.setTextColor(getResources().getColor(R.color.text_black));
        binding.date2Tv.setTextColor(getResources().getColor(R.color.text_black));
        binding.time1Tv.setTextColor(getResources().getColor(R.color.text_black));
        binding.time2Tv.setTextColor(getResources().getColor(R.color.text_black));
    }

    private void focusEditButton() {
        binding.editBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.editBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_white_24dp, 0, 0, 0);
        binding.viewBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.viewBtn.setTextColor(getResources().getColor(R.color.darktxt));
        binding.viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_gray_24dp, 0, 0, 0);
    }

    private void focusViewButton() {
        binding.viewBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.viewBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_white_24dp, 0, 0, 0);
        binding.editBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.editBtn.setTextColor(getResources().getColor(R.color.darktxt));
        binding.editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_gray_24dp, 0, 0, 0);
    }

////////////////////////////////////////////////////////////////////
    private void showTextViewHideEditText(boolean round) {
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
        binding.roundBtn.setEnabled(false);
        binding.singleBtn.setEnabled(false);
        if(round){
            showRoundTrip();
            binding.date2Tv.setVisibility(GONE);
            binding.date2TitleTv.setVisibility(VISIBLE);
            binding.date2ViewTv.setVisibility(VISIBLE);
            binding.time2Tv.setVisibility(GONE);
            binding.time2TitleTv.setVisibility(VISIBLE);
            binding.time2ViewTv.setVisibility(VISIBLE);
        }else{
            showSingleTrip();
            binding.date2Tv.setVisibility(GONE);
            binding.date2TitleTv.setVisibility(GONE);
            binding.date2ViewTv.setVisibility(GONE);
            binding.time2Tv.setVisibility(GONE);
            binding.time2TitleTv.setVisibility(GONE);
            binding.time2ViewTv.setVisibility(GONE);
        }
    }

    private void showEditTextHideTextView(boolean round) {
        binding.addNoteImageView.setVisibility(VISIBLE);
        binding.tripNameEt.setVisibility(VISIBLE);
        binding.startPointConstraintlayout.setVisibility(VISIBLE);
        binding.endPointConstraintlayout.setVisibility(VISIBLE);
        binding.date1Tv.setVisibility(VISIBLE);
        binding.time1Tv.setVisibility(VISIBLE);
        binding.saveTripFab.setVisibility(VISIBLE);
        //binding.edit_group_general.setVisibility(VISIBLE);
        binding.tripTitleTv.setVisibility(GONE);
        binding.tripNameTv.setVisibility(GONE);
        binding.startPointTitleTv.setVisibility(GONE);
        binding.startPointTv.setVisibility(GONE);
        binding.endPointTitleTv.setVisibility(GONE);
        binding.endPointTv.setVisibility(GONE);
        binding.date1TitleTv.setVisibility(GONE);
        binding.date1ViewTv.setVisibility(GONE);
        binding.time1TitleTv.setVisibility(GONE);
        binding.time1ViewTv.setVisibility(GONE);

        binding.roundBtn.setEnabled(false);
        binding.singleBtn.setEnabled(false);
        if(round){
            showRoundTrip();
            binding.date2Tv.setVisibility(VISIBLE);
            binding.time2Tv.setVisibility(VISIBLE);

            binding.date2TitleTv.setVisibility(GONE);
            binding.date2ViewTv.setVisibility(GONE);
            binding.time2TitleTv.setVisibility(GONE);
            binding.time2ViewTv.setVisibility(GONE);
        }else{
            showSingleTrip();
            binding.date2Tv.setVisibility(GONE);
            binding.date2TitleTv.setVisibility(GONE);
            binding.date2ViewTv.setVisibility(GONE);
            binding.time2Tv.setVisibility(GONE);
            binding.time2TitleTv.setVisibility(GONE);
            binding.time2ViewTv.setVisibility(GONE);
        }
    }

    private void showRoundData() {
        showSingleData();
        binding.date2ViewTv.setText(DateUtils.simpleDateFormatForYears_Months.format(receivedTripAndLocation.getLocationDataList().getRoundDate()));
        binding.date2Tv.setText(DateUtils.simpleDateFormatForYears_Months.format(receivedTripAndLocation.getLocationDataList().getRoundDate()));
        binding.time2ViewTv.setText(DateUtils.simpleDateFormatForHours_Minutes.format(receivedTripAndLocation.getLocationDataList().getRoundDate()));
        binding.time2Tv.setText(DateUtils.simpleDateFormatForHours_Minutes.format(receivedTripAndLocation.getLocationDataList().getRoundDate()));
    }

    private void showSingleData(){
        binding.date1ViewTv.setText(DateUtils.simpleDateFormatForYears_Months.format(receivedTripAndLocation.getLocationDataList().getStartDate()));
        binding.date1Tv.setText(DateUtils.simpleDateFormatForYears_Months.format(receivedTripAndLocation.getLocationDataList().getStartDate()));
        binding.time1ViewTv.setText(DateUtils.simpleDateFormatForHours_Minutes.format(receivedTripAndLocation.getLocationDataList().getStartDate()));
        binding.time1Tv.setText(DateUtils.simpleDateFormatForHours_Minutes.format(receivedTripAndLocation.getLocationDataList().getStartDate()));
    }

    private void showSingleTrip() {
        isRound =false;
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
        showSingleData();
    }

    private void showRoundTrip() {
        isRound = true;
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.darktxt));
        showRoundData();
    }

///////////////////////////////////////////////////////////////////////////

    public void setUpViews() {
        binding.taskToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
        binding.editBtn.setOnClickListener(this);
        binding.viewBtn.setOnClickListener(this);
        binding.saveTripFab.setOnClickListener(this);
        binding.tripNameEt.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editBtn:
                showEditLayout(receivedTripAndLocation.getLocationDataList().isRound());
                break;
            case R.id.viewBtn:
                showViewLayout(receivedTripAndLocation.getLocationDataList().isRound());
                break;
            case R.id.date1Tv:
                showDatePicker(DATE1);
                break;
            case R.id.time1Tv:
                checkDate1();
                break;
            case R.id.date2Tv:
                showDatePicker(DATE2);
                break;
            case R.id.time2Tv:
                checkDate2();
                break;
            case R.id.singleBtn:
                showSingleView();
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundView();
                showRoundTrip();
                break;
            case R.id.addNoteImageView:
                addNote();
                break;
            case R.id.saveTripFab:
                updateTrip();
                break;
        }
    }

    private void checkDate1() {
        if(binding.date1Tv.getText().toString().isEmpty()){
            showToast(getResources().getString(R.string.plzPickDateFirst));
        }else{
            showTime(TIME1);
        }
    }
    private void checkDate2() {
        if(binding.date2Tv.getText().toString().isEmpty()){
            showToast(getResources().getString(R.string.plzPickDateFirst));
        }else{
            showTime(TIME2);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void showRoundView() {
        binding.date2Tv.setVisibility(VISIBLE);
        binding.date2TitleTv.setVisibility(GONE);
        binding.date2ViewTv.setVisibility(GONE);
        binding.time2Tv.setVisibility(VISIBLE);
        binding.time2TitleTv.setVisibility(GONE);
        binding.time2ViewTv.setVisibility(GONE);
    }

    private void showSingleView() {
        binding.date2Tv.setVisibility(GONE);
        binding.date2TitleTv.setVisibility(GONE);
        binding.date2ViewTv.setVisibility(GONE);
        binding.time2Tv.setVisibility(GONE);
        binding.time2TitleTv.setVisibility(GONE);
        binding.time2ViewTv.setVisibility(GONE);
    }

    private void showTime(int time) {
        Calendar selectedDateTime = Calendar.getInstance();
        mHour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        mMinute = selectedDateTime.get(Calendar.MINUTE);
        Calendar currentDateTime=Calendar.getInstance();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
           if(selectedDateTime.getTimeInMillis()>=currentDateTime.getTimeInMillis()){
                switch(time){
                    case 3:
                        hour1 = hourOfDay;
                        minute1 = minute;
                        binding.time1Tv.setText(hourOfDay + ":" + minute);
                        binding.time1Tv.setTextColor(getResources().getColor(R.color.text_black));
                        break;
                    case 4:
                        hour2=hourOfDay;
                        minute2=minute;
                        checkFirstTime(hour1,minute1,hour2,minute2);
                        break;
                }
            }else{
                switch(time){
                    case 3:
                        showToast(getResources().getString(R.string.plzPickValidStartTime_current));
                        break;
                    case 4:
                        showToast(getResources().getString(R.string.plzPickValidEndTime));
                        break;
                }
            }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private boolean checkFirstTime(int hour1, int minute1, int hour2, int minute2){
        if(binding.time1Tv.getText().toString().isEmpty()){
            showToast(getResources().getString(R.string.plzPickStartTimeFirst));
        }else{
            Calendar startTime=Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour1);
            startTime.set(Calendar.MINUTE, minute1);
            Calendar endTime=Calendar.getInstance();
            endTime.set(Calendar.HOUR_OF_DAY, hour2);
            endTime.set(Calendar.MINUTE, minute2);
            checkFirstSecondTime(startTime,endTime,hour2,minute2);
        }
        return true;
    }

    private boolean checkFirstSecondTime(Calendar startTime, Calendar endTime, int hour2, int minute2) {
        if(endTime.getTimeInMillis()<startTime.getTimeInMillis()){
            showToast(getResources().getString(R.string.plzPickValidEndTime));
            return false;
        }else{
            binding.time2Tv.setText(hour2 + ":" + minute2);
            binding.time2Tv.setTextColor(getResources().getColor(R.color.text_black));
        }
        return true;
    }
    private void showDatePicker(int date) {
        final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTS"));
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        c.getTimeInMillis();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            switch (date) {
                case 1:
                    c.set(year, month, dayOfMonth);
                    year1=year;
                    month1=month;
                    day1=dayOfMonth;
                    binding.date1Tv.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    binding.date1Tv.setTextColor(getResources().getColor(R.color.text_black));
                    break;
                case 2:
                    year2=year;
                    month2=month;
                    day2=dayOfMonth;
                    checkFirstDate(year1,month1,day1,year2,month2,day2);
                    break;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private boolean checkFirstDate(int year1, int month1, int day1, int year2, int month2, int day2) {
        if(binding.date1Tv.getText().toString().isEmpty()){
            showToast(getResources().getString(R.string.plzPickStartDateFirst));
        }else{
            checkFirstSecondDate(year1,month1,day1,year2,month2,day2);
        }
        return true;
    }

    private boolean checkFirstSecondDate(int year1, int month1, int day1, int year2, int month2, int day2) {
        Calendar startDate=Calendar.getInstance();
        startDate.set(Calendar.YEAR,year1);
        startDate.set(Calendar.MONTH,month1);
        startDate.set(Calendar.DAY_OF_MONTH,day1);
        Calendar endDate=Calendar.getInstance();
        endDate.set(Calendar.YEAR,year2);
        endDate.set(Calendar.MONTH,month2);
        endDate.set(Calendar.DAY_OF_MONTH,day2);
        if(endDate.getTimeInMillis()<startDate.getTimeInMillis()){
            showToast(getResources().getString(R.string.plzPickValidEndDate));
            return false;
        }else{
            binding.date2Tv.setText(day2 + "-" + (month2 + 1) + "-" +year2);
            binding.date2Tv.setTextColor(getResources().getColor(R.color.text_black));
        }
        return true;
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
    private boolean isValidData(boolean isRound, Date dateFormat1, Date dateFormat2){
        if(binding.tripNameEt.getText().toString().trim().isEmpty()){
            showToast(getResources().getString(R.string.plzEnterTripName));
            return false;
        }
        if (((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.plzEnterStartPoint));
            return false;
        }
        if (((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.plzEnterEndPoint));
            return false;
        }

        if(binding.date1Tv.getText().toString().trim().isEmpty()){
            showToast(getResources().getString(R.string.plzPickStartDate));
            return false;
        }
        if(binding.time1Tv.getText().toString().trim().isEmpty()){
            showToast(getResources().getString(R.string.plzPickStartTime));
            return false;
        }
        if(isRound){
            if (binding.time2Tv.getText().toString().trim().isEmpty()) {
                showToast(getResources().getString(R.string.plzPickEndTime));
                return false;
            }
            if (binding.date2Tv.getText().toString().trim().isEmpty()) {
                showToast(getResources().getString(R.string.plzPickEndDate));
                return false;
            }
            if(!binding.time2Tv.getText().toString().trim().isEmpty()){
                Calendar startTime=Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour1);
                startTime.set(Calendar.MINUTE, minute1);
                Calendar endTime=Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, hour2);
                endTime.set(Calendar.MINUTE, minute2);
                if(endTime.getTimeInMillis()<startTime.getTimeInMillis()){
                    showToast(getResources().getString(R.string.plzPickValidEndTime));
                    return false;
                }
            }
            if(!binding.date2Tv.getText().toString().trim().isEmpty()){
                if(dateFormat1.compareTo(dateFormat2)>0){
                    showToast(getResources().getString(R.string.plzPickValidEndDate));
                    return false;
                }
            }
        }
        return true;
    }
    private void updateTrip() {
        String tripName=binding.tripNameEt.getText().toString();
        String date1=binding.date1Tv.getText().toString();
        String time1=binding.time1Tv.getText().toString();
        String date2=binding.date2Tv.getText().toString();
        String time2=binding.time2Tv.getText().toString();
        try {
            if(!date1.isEmpty()){
                date1 = date1 + " " + time1;
                Log.d(TAG,"un formatted 1................."+date1);
                dateFormat1=DateUtils.simpleDateFormatFullDate.parse(date1);
                receivedTripAndLocation.getLocationDataList().setStartDate(dateFormat1);
                Log.d(TAG,"saved formatted 1................."+dateFormat1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isRound) {
            try {
                if (!date2.isEmpty()) {
                    date2 = date2 + " " + time2;
                    dateFormat2= DateUtils.simpleDateFormatFullDate.parse(date2);
                    Log.d(TAG,"un formatted 2................."+date2);
                    receivedTripAndLocation.getLocationDataList().setRoundDate(dateFormat2);
                    Log.d(TAG, "saved formatted 2................"+dateFormat2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "before updateTrip: start date"+receivedTripAndLocation.getLocationDataList().getStartDate());
        Log.d(TAG, "before updateTrip: end date"+receivedTripAndLocation.getLocationDataList().getRoundDate());
        Log.d(TAG, "before updateTrip: start point lat(start)"+receivedTripAndLocation.getLocationDataList().getStartTripStartPointLat());
        Log.d(TAG, "before updateTrip: start point lng(start)"+receivedTripAndLocation.getLocationDataList().getStartTripStartPointLng());
        Log.d(TAG, "before updateTrip: start address(start)"+receivedTripAndLocation.getLocationDataList().getStartTripAddressName());
        Log.d(TAG, "before updateTrip: start point lat(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndPointLat());
        Log.d(TAG, "before updateTrip: start point lat(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndPointLng());
        Log.d(TAG, "before updateTrip: start address(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndAddressName());

        Log.d(TAG, "before updateTrip: round address(start)"+receivedTripAndLocation.getLocationDataList().getRoundTripStartAddressName());
        Log.d(TAG, "before updateTrip: round address(end)"+receivedTripAndLocation.getLocationDataList().getRoundTripEndAddressName());

        receivedTripAndLocation.getLocationDataList().setStartTripStartPointLat(startPonitLat);
        receivedTripAndLocation.getLocationDataList().setStartTripStartPointLng(startPonitLng);
        receivedTripAndLocation.getLocationDataList().setStartTripEndPointLat(endPonitLat);
        receivedTripAndLocation.getLocationDataList().setStartTripEndPointLng(endPonitLng);
        receivedTripAndLocation.getLocationDataList().setStartTripAddressName(startAddress);
        receivedTripAndLocation.getLocationDataList().setStartTripEndAddressName(endAddress);

        receivedTripAndLocation.getLocationDataList().setRoundTripStartPointLat(endPonitLat);
        receivedTripAndLocation.getLocationDataList().setRoundTripStartPointLng(endPonitLng);
        receivedTripAndLocation.getLocationDataList().setRoundTripEndPointLat(startPonitLat);
        receivedTripAndLocation.getLocationDataList().setRoundTripEndPointLng(startPonitLng);
        receivedTripAndLocation.getLocationDataList().setRoundTripStartAddressName(endAddress);
        receivedTripAndLocation.getLocationDataList().setRoundTripEndAddressName(startAddress);

        receivedTripAndLocation.getTrip().setTripName(tripName);
        receivedTripAndLocation.getLocationDataList().setRound(isRound);

        isValidData(isRound,dateFormat1,dateFormat2);
        Log.d(TAG, "after updateTrip: start date"+receivedTripAndLocation.getLocationDataList().getStartDate());
        Log.d(TAG, "after updateTrip: end date"+receivedTripAndLocation.getLocationDataList().getRoundDate());

        Log.d(TAG, "after updateTrip: start point lat(start)"+receivedTripAndLocation.getLocationDataList().getStartTripStartPointLat());
        Log.d(TAG, "after updateTrip: start point lng(start)"+receivedTripAndLocation.getLocationDataList().getStartTripStartPointLng());
        Log.d(TAG, "after updateTrip: start address(start)"+receivedTripAndLocation.getLocationDataList().getStartTripAddressName());
        Log.d(TAG, "after updateTrip: start point lat(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndPointLat());
        Log.d(TAG, "after updateTrip: start point lat(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndPointLng());
        Log.d(TAG, "after updateTrip: start address(end)"+receivedTripAndLocation.getLocationDataList().getStartTripEndAddressName());

        Log.d(TAG, "after updateTrip: round address(start)"+receivedTripAndLocation.getLocationDataList().getRoundTripStartAddressName());
        Log.d(TAG, "after updateTrip: round address(end)"+receivedTripAndLocation.getLocationDataList().getRoundTripEndAddressName());
        if(isValidData(isRound,dateFormat1,dateFormat2)){
            detailsViewModel.updateTripAndNotes(receivedTripAndLocation, notesList).observe(getViewLifecycleOwner(), s -> {
                if(s.equals("Updated Successfully!")){
                    Log.i(TAG,"start alarm start date ...."+receivedTripAndLocation.getLocationDataList().getStartDate());
                    AlarmUtils.startAlarm(getContext(), receivedTripAndLocation.getLocationDataList().getStartDate().getTime(), MapperClass.mapTripAndLocationObject(receivedTripAndLocation));
                    Objects.requireNonNull(getActivity()).onBackPressed();
                }else{
                    showToast("Updated Failed!");
                }
            });
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
        builder.setPositiveButton("OK", (dialog, which) -> {
            String inputText = input.getText().toString();
            if (inputText.isEmpty()) return;
            Note note = new Note();
            note.setMessage(inputText);
            note.setStatus(false);
            notesList.add(note);
            if (notesList.size() > 0) {
                noteAdapter = new NoteAdapter(getContext(), notesList);
                noteAdapter.setOnRecyclerViewItemClickListener((v, position) -> deleteNote(position));
                binding.notesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.notesRecyclerview.setAdapter(noteAdapter);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void deleteNote(int position) {
        notesList.remove(position);
        noteAdapter.notifyItemRemoved(position);
        noteAdapter.notifyItemRangeChanged(position,notesList.size());
    }
}