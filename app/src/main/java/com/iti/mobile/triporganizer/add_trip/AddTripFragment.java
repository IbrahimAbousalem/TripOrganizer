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
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;

public class AddTripFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddTripFragment";
    private AutocompleteSupportFragment startPointAutocompleteFragment;
    private AutocompleteSupportFragment endPointAutocompleteFragment;
    private PlacesClient placesClient;

    NavController controller;
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
        String date1 = binding.date1Tv.getText().toString();
        String time1 = binding.time1Tv.getText().toString();
        String date2 = binding.date2Tv.getText().toString();
        String time2 = binding.time2Tv.getText().toString();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            date1 = date1 + " " + time1;
            Date formatedDate = format.parse(date1);
            Log.d(TAG, formatedDate.toString());
            locationData.setStartDate(formatedDate);
            Log.d(TAG, "saved formatted" + date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        locationData.setStartTripStartPointLat(startPonitLat);
        locationData.setStartTripStartPointLng(startPonitLng);
        locationData.setStartTripEndPointLat(endPonitLat);
        locationData.setStartTripEndPointLng(endPonitLng);
        locationData.setStartTripAddressName(startAddress);
        locationData.setStartTripEndAddressName(endAddress);
        if (isRound) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                date2 = date2 + " " + time2;
                Date formatedDate = format.parse(date2);
                Log.d(TAG, formatedDate.toString());
                locationData.setRoundDate(formatedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            locationData.setRoundTripStartPointLat(endPonitLat);
            locationData.setRoundTripStartPointLng(endPonitLng);
            locationData.setRoundTripEndPointLat(startPonitLat);
            locationData.setRoundTripEndPointLng(startPonitLng);
            locationData.setRoundTripStartAddressName(endAddress);
            locationData.setRoundTripEndAddressName(startAddress);
        }
        trip.setTripName(tripName);
        trip.setRound(isRound);
        trip.setUserId(firebaseAuth.getCurrentUser().getUid());
        trip.setStatus("UpComing");
        trip.setLocationData(locationData);
        addTripViewModel.addTripAndNotes(trip, notesList);
        //TODO: Go back after adding a trip.
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
