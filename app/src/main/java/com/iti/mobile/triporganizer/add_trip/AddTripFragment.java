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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import static com.iti.mobile.triporganizer.utils.DetailsUtils.isValidData;
import static com.iti.mobile.triporganizer.utils.Flags.ADDTRIP_FRAGMENTBINDING;
import static com.iti.mobile.triporganizer.utils.Flags.DATE1;
import static com.iti.mobile.triporganizer.utils.Flags.DATE2;
import static com.iti.mobile.triporganizer.utils.Flags.TIME1;
import static com.iti.mobile.triporganizer.utils.Flags.TIME2;
import static com.iti.mobile.triporganizer.utils.Tags.DATE;
import static com.iti.mobile.triporganizer.utils.Tags.DATE_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.ENDDATE;
import static com.iti.mobile.triporganizer.utils.Tags.ENDTIME;
import static com.iti.mobile.triporganizer.utils.Tags.END_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.STARTDATE;
import static com.iti.mobile.triporganizer.utils.Tags.STARTTIME;
import static com.iti.mobile.triporganizer.utils.Tags.START_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.TIME;
import static com.iti.mobile.triporganizer.utils.Tags.TIME_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.TRIP_NAME;

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

    @Inject
    FirebaseAuth firebaseAuth;

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
        initView(view);
    }

    private void initView(View view) {
        binding.addTripFab.setOnClickListener(this);
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

    private void handleStartPointPlacesSelected(AutocompleteSupportFragment startPointAutocompleteFragment) {
        ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setError(null);
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
        ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                .setError(null);
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
            case R.id.tripNameEt:
                binding.tripNameEt.setError(null);
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
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString();
                if (inputText.isEmpty()) return;
                Note note = new Note();
                note.setMessage(inputText);
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

    private void addTrip() {
        String tripName = binding.tripNameEt.getText().toString();
        String date1 = binding.date1Tv.getText().toString().trim();
        String time1 = binding.time1Tv.getText().toString().trim();
        String date2 = binding.date2Tv.getText().toString().trim();
        String time2 = binding.time2Tv.getText().toString().trim();
        Date formatedDate1 = null;
        Date formatedDate2 = null;
        /////////////////////////////////////////////////////////////////////////
        String dateTv1 = binding.date1Tv.getText().toString().trim();
        String timeTv1 = binding.time1Tv.getText().toString().trim();
        String dateTv2 = binding.date2Tv.getText().toString().trim();
        String timeTv2 = binding.time2Tv.getText().toString().trim();

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
        setLocationData(formatedDate1,formatedDate2,startPonitLat,startPonitLng,endPonitLat,endPonitLng,startAddress,endAddress);
        if(isRound){
            setLocationData(formatedDate1,formatedDate2,endPonitLat,endPonitLng,startPonitLat,startPonitLng,endAddress,startAddress);
        }
        setTripData(isRound,tripName,firebaseAuth.getCurrentUser().getUid(),"UpComing",locationData);

        isValidData(trip,isRound,binding,null,startPointAutocompleteFragment,endPointAutocompleteFragment,generateErrorsMessages(),ADDTRIP_FRAGMENTBINDING,generateDates(dateTv1,dateTv2,timeTv1,timeTv2));
        if (isValidData(trip,isRound,binding,null,startPointAutocompleteFragment,endPointAutocompleteFragment,generateErrorsMessages(),ADDTRIP_FRAGMENTBINDING,generateDates(dateTv1,dateTv2,timeTv1,timeTv2))) {
            addTripViewModel.addTripAndNotes(trip, notesList).observe(requireActivity(), newTrip -> {
                        AlarmUtils.startAlarm(getContext(), newTrip.getLocationData().getStartDate().getTime(),newTrip.getTripName(), String.valueOf(newTrip.getId()), String.valueOf(newTrip.getLocationData().getStartTripEndPointLat()), String.valueOf(newTrip.getLocationData().getStartTripEndPointLng()));
                        if (newTrip.getLocationData().isRound()) {
                            AlarmUtils.startAlarm(getContext(), newTrip.getLocationData().getRoundDate().getTime(), newTrip.getTripName(), String.valueOf(newTrip.getId()), String.valueOf(newTrip.getLocationData().getRoundTripEndPointLat()), String.valueOf(newTrip.getLocationData().getRoundTripEndPointLng()));
                }
                getActivity().onBackPressed();
            });
        }
    }
    private HashMap<String,String> generateDates(String date1,String date2,String time1,String time2){
        HashMap<String, String> hash_map = new HashMap<String, String>();
        hash_map.put(STARTDATE,date1);
        hash_map.put(ENDDATE,date2);
        hash_map.put(STARTTIME,time1);
        hash_map.put(ENDTIME,time2);
        return hash_map;
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

    private void setTripData(boolean isRound,String tripName,String uid, String upComing, LocationData locationData) {
        trip.setTripName(tripName);
        trip.setUserId(uid);
        trip.setStatus(upComing);
        trip.setLocationData(locationData);
        trip.getLocationData().setRound(isRound);
    }

    private void setLocationData(Date formatedDate1,Date formatedDate2, double startPonitLat, double startPonitLng, double endPonitLat, double endPonitLng, String startAddress, String endAddress) {
        locationData.setStartDate(formatedDate1);
        locationData.setRoundDate(formatedDate2);
        locationData.setStartTripStartPointLat(startPonitLat);
        locationData.setStartTripStartPointLng(startPonitLng);
        locationData.setStartTripEndPointLat(endPonitLat);
        locationData.setStartTripEndPointLng(endPonitLng);
        locationData.setStartTripAddressName(startAddress);
        locationData.setStartTripEndAddressName(endAddress);

    }

    private void showTime(int time) {
        binding.time1Tv.setError(null);
        binding.time2Tv.setError(null);

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch (time) {
                            case 3:
                                hour1 = hourOfDay;
                                minute1 = minute;
                                binding.time1Tv.setText(hourOfDay + ":" + minute);
                                break;
                            case 4:
                                hour2 = hourOfDay;
                                minute2 = minute;
                                binding.time2Tv.setText(hourOfDay + ":" + minute);
                                break;
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void showDatePicker(int date) {
        binding.date1Tv.setError(null);
        binding.date2Tv.setError(null);
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
                        binding.date1Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                    case 2:
                        year2 = year;
                        month2 = month + 1;
                        day2 = dayOfMonth;
                        binding.date2Tv.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        break;
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
