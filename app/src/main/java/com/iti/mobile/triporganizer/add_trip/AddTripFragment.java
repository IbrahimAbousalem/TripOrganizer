package com.iti.mobile.triporganizer.add_trip;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.iti.mobile.triporganizer.databinding.FragmentAddTripBinding;
import com.iti.mobile.triporganizer.details.NoteAdapter;
import com.iti.mobile.triporganizer.utils.AlarmUtils;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.DateUtils;

import java.text.DateFormat;
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

public class AddTripFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddTripFragment";
    private AutocompleteSupportFragment startPointAutocompleteFragment;
    private AutocompleteSupportFragment endPointAutocompleteFragment;
    private PlacesClient placesClient;

    private NavController controller;
    @Inject
    ViewModelProviderFactory providerFactory;
    AddTripViewModel addTripViewModel;
    private FragmentAddTripBinding binding;

    private double startPonitLat;
    private double startPonitLng;
    private double endPonitLat;
    private double endPonitLng;
    private String startAddress;
    private String endAddress;
    private boolean isRound = false;
    private Trip trip;
    private LocationData locationData;

    NoteAdapter noteAdapter;
    String userId;
    private List<Note> notesList;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int hour1, minute1, hour2, minute2, year1, year2, month1, month2, day1, day2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTripBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        showSingleTrip();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        trip = new Trip();
        locationData = new LocationData();
        notesList = new ArrayList<>();
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        addTripViewModel = new ViewModelProvider(this, providerFactory).get(AddTripViewModel.class);
        userId = addTripViewModel.getUserId();
        initView(view);
    }

    private void initView(View view) {
        binding.taskToolbar.setNavigationOnClickListener((View.OnClickListener) v -> {
            //controller.navigate(R.id.action_addTripFragment_to_homeFragment)
            Objects.requireNonNull(getActivity()).onBackPressed();
        });
        binding.addTripFab.setOnClickListener(this);
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
        ((EditText)startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
        endPointAutocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.endAutoCompleteFragment);
        ((EditText)endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);
        handleStartPointPlacesSelected(startPointAutocompleteFragment);
        handleEndPointPlacesSelected(endPointAutocompleteFragment);
    }

    private void handleStartPointPlacesSelected(AutocompleteSupportFragment startPointAutocompleteFragment) {
        startPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS));
        startPointAutocompleteFragment.setHint(getString(R.string.estart_point));
        startPointAutocompleteFragment.setCountry("EG");
        startPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        startPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " +
                        place.getLatLng().latitude + ", " + place.getLatLng().longitude);

                startPonitLat = place.getLatLng().latitude;
                startPonitLng = place.getLatLng().longitude;
                startAddress = place.getName();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void handleEndPointPlacesSelected(AutocompleteSupportFragment endPointAutocompleteFragment) {
        endPointAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        endPointAutocompleteFragment.setHint(getString(R.string.eend_point));
        endPointAutocompleteFragment.setCountry("EG");
        endPointAutocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        endPointAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " +
                        place.getLatLng().latitude + ", " + place.getLatLng().longitude);
                endPonitLat = place.getLatLng().latitude;
                endPonitLng = place.getLatLng().longitude;
                endAddress = place.getName();
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
            case R.id.addTripFab:
                addTrip();
                break;
            case R.id.singleBtn:
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundTrip();
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
        noteAdapter.notifyItemRangeChanged(position, notesList.size());
    }

    private void showSingleTrip() {
        isRound = false;
        focusSingleButton();
        hideSecondDateTimeViews();
    }

    private void showRoundTrip() {
        isRound = true;
        focusRoundButton();
        showSecondDateTimeEditText();

    }

    private void showSecondDateTimeEditText() {
        binding.date2Tv.setVisibility(VISIBLE);
        binding.time2Tv.setVisibility(VISIBLE);
    }

    private void hideSecondDateTimeViews() {
        binding.date2Tv.setVisibility(GONE);
        binding.time2Tv.setVisibility(GONE);
    }

    private void focusSingleButton() {
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }

    private void focusRoundButton() {
        binding.roundBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_clr));
        binding.roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        binding.singleBtn.setBackground(getResources().getDrawable(R.drawable.trip_btn_rounded_no_clr));
        binding.singleBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }

    private void addTrip() {
        String tripName = binding.tripNameEt.getText().toString();
        String date1 = binding.date1Tv.getText().toString();
        String time1 = binding.time1Tv.getText().toString();
        String date2 = binding.date2Tv.getText().toString();
        String time2 = binding.time2Tv.getText().toString();
        Date dateFormat1=null;
        Date dateFormat2=null;
        try {
            if(!date1.isEmpty()){
                SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy HH:mm");
                date1 = date1 + " " + time1;
                Log.d(TAG,"un formatted 1................."+date1);
                dateFormat1=format.parse(date1);
                locationData.setStartDate(dateFormat1);
                Log.d(TAG,"saved formatted 1................."+dateFormat1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (isRound) {
            try {
                if (!date2.isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    date2 = date2 + " " + time2;
                    dateFormat2= format.parse(date2);
                    Log.d(TAG,"un formatted 2................."+date2);
                    locationData.setRoundDate(dateFormat2);
                    Log.d(TAG, "saved formatted 2................"+dateFormat2);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
            locationData.setStartTripStartPointLat(startPonitLat);
            locationData.setStartTripStartPointLng(startPonitLng);
            locationData.setStartTripEndPointLat(endPonitLat);
            locationData.setStartTripEndPointLng(endPonitLng);
            locationData.setStartTripAddressName(startAddress);
            locationData.setStartTripEndAddressName(endAddress);
            locationData.setRoundTripStartPointLat(endPonitLat);
            locationData.setRoundTripStartPointLng(endPonitLng);
            locationData.setRoundTripEndPointLat(startPonitLat);
            locationData.setRoundTripEndPointLng(startPonitLng);
            locationData.setRoundTripStartAddressName(endAddress);
            locationData.setRoundTripEndAddressName(startAddress);
            locationData.setRound(isRound);
            trip.setTripName(tripName);
            trip.setUserId(userId);
            trip.setStatus(Constants.UPCOMING);
            trip.setLocationData(locationData);

            isValidData(isRound);
            if(isValidData(isRound)){
                addTripViewModel.addTripAndNotes(trip, notesList).observe(getViewLifecycleOwner(), newTrip -> {
                    AlarmUtils.startAlarm(getContext(), newTrip.getLocationData().getStartDate().getTime(),newTrip.getTripName(), String.valueOf(newTrip.getId()), String.valueOf(newTrip.getLocationData().getStartTripEndPointLat()), String.valueOf(newTrip.getLocationData().getStartTripEndPointLng()));
                    if (newTrip.getLocationData().isRound()) {
                        AlarmUtils.startAlarm(getContext(), newTrip.getLocationData().getRoundDate().getTime(), newTrip.getTripName(), String.valueOf(newTrip.getId()), String.valueOf(newTrip.getLocationData().getRoundTripEndPointLat()), String.valueOf(newTrip.getLocationData().getRoundTripEndPointLng()));
                    }
                    Objects.requireNonNull(getActivity()).onBackPressed();
                });
            }
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
    private void showToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    //TODO: Go back after adding a trip.
    private void showTime(int time) {
        Calendar selectedDateTime = Calendar.getInstance();
        mHour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        mMinute = selectedDateTime.get(Calendar.MINUTE);
        Calendar currentDateTime=Calendar.getInstance();
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
                                    showToast(getResources().getString(R.string.plzPickValidStartTime_current));
                                    break;
                                case 4:
                                    showToast(getResources().getString(R.string.plzPickValidStartTime_current));
                                    break;
                            }
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
                        year1 = year;
                        month1 = month + 1;
                        day1 = dayOfMonth;
                        binding.date1Tv.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        break;
                    case 2:
                        year2 = year;
                        month2 = month + 1;
                        day2 = dayOfMonth;
                        binding.date2Tv.setText(dayOfMonth + "-" + (month + 1) + "-" +year);
                        break;
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
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
}