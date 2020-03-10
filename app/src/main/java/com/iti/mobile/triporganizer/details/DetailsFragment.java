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
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.databinding.FragmentDetailsBinding;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
        binding.roundBtn.setEnabled(false);
        binding.singleBtn.setEnabled(false);
        if(round){
            showRoundTrip();
            binding.date2Tv.setVisibility(VISIBLE);
            binding.date2TitleTv.setVisibility(GONE);
            binding.date2ViewTv.setVisibility(GONE);
            binding.time2Tv.setVisibility(VISIBLE);
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
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
        showSingleData();
    }

    private void showRoundTrip() {
        isRound = true;
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
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
            if(selectedDateTime.getTimeInMillis()>=currentDateTime.getTimeInMillis()){
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
            }else{
                switch(time){
                    case 3:
                        showToast(getResources().getString(R.string.plzPickValidStartTime));
                        break;
                    case 4:
                        showToast(getResources().getString(R.string.plzPickValidStartTime));
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            switch (date) {
                case 1:
                    c.set(year, month, dayOfMonth);
                    year1=year;
                    month1=month+1;
                    day1=dayOfMonth;
                    binding.date1Tv.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    break;
                case 2:
                    year2=year;
                    month2=month+1;
                    day2=dayOfMonth;
                    binding.date2Tv.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    break;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
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
    private boolean isValidData(boolean isRound){
        if(binding.tripNameEt.getText().toString().trim().isEmpty()){
            showToast(getResources().getString(R.string.plzEnterTripName));
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
        if (((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.plzEnterStartPoint));
            return false;
        }
        if (((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.plzEnterEndPoint));
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
            /*if (receivedTripAndLocation.getLocationDataList().getStartDate().compareTo(receivedTripAndLocation.getLocationDataList().getRoundDate()) > 0) {
                binding.date1Tv.setError(getResources().getString(R.string.plzPickValidStartDate));
                return false;
            }
            if (receivedTripAndLocation.getLocationDataList().getStartDate().getTime() > receivedTripAndLocation.getLocationDataList().getRoundDate().getTime()) {
                binding.time1Tv.setError(getResources().getString(R.string.plzPickTime));
                return false;
            }*/
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
                SimpleDateFormat format=new SimpleDateFormat("YYYY-MM-dd HH:mm",new Locale("ar_EG"));
                date1 = date1 + " " + time1;
                Date formatedDate=format.parse(date1);
                Log.d(TAG, formatedDate.toString());
                receivedTripAndLocation.getLocationDataList().setStartDate(formatedDate);
                Log.d(TAG,"saved formatted///////////////"+formatedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isRound) {
            try {
                if (!date2.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm",new Locale("ar_EG"));
                    date2 = date2 + " " + time2;
                    Date formatedDate= format.parse(date2);
                    receivedTripAndLocation.getLocationDataList().setRoundDate(formatedDate);
                    Log.d(TAG,"saved formatted///////////////"+formatedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        receivedTripAndLocation.getLocationDataList().setStartTripStartPointLat(startPonitLat);
        receivedTripAndLocation.getLocationDataList().setStartTripStartPointLng(startPonitLng);
        receivedTripAndLocation.getLocationDataList().setStartTripEndPointLat(endPonitLat);
        receivedTripAndLocation.getLocationDataList().setStartTripEndPointLng(endPonitLng);
        receivedTripAndLocation.getLocationDataList().setStartTripAddressName(startAddress);
        receivedTripAndLocation.getLocationDataList().setStartTripEndAddressName(endAddress);
        receivedTripAndLocation.getTrip().setTripName(tripName);
        receivedTripAndLocation.getLocationDataList().setRound(isRound);

        isValidData(isRound);
        if(isValidData(isRound)){
            detailsViewModel.updateTripAndNotes(receivedTripAndLocation, notesList).observe(getViewLifecycleOwner(), s -> {
                if(s.equals("Updated Successfully!")){
                    AlarmUtils.startAlarm(getContext(), receivedTripAndLocation.getLocationDataList().getStartDate().getTime(),receivedTripAndLocation.getTrip().getTripName(), String.valueOf(receivedTripAndLocation.getTrip().getId()), String.valueOf(receivedTripAndLocation.getLocationDataList().getStartTripEndPointLat()), String.valueOf(receivedTripAndLocation.getLocationDataList().getStartTripEndPointLng()));
                    if (receivedTripAndLocation.getLocationDataList().isRound()) {
                        AlarmUtils.startAlarm(getContext(), receivedTripAndLocation.getLocationDataList().getRoundDate().getTime(), receivedTripAndLocation.getTrip().getTripName(), String.valueOf(receivedTripAndLocation.getTrip().getId()), String.valueOf(receivedTripAndLocation.getLocationDataList().getRoundTripEndPointLat()), String.valueOf(receivedTripAndLocation.getLocationDataList().getRoundTripEndPointLng()));
                    }
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
            if(inputText.isEmpty())return;
            Note note = new Note();
            note.setMessage(input.getText().toString());
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