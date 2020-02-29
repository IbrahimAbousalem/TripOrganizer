package com.iti.mobile.triporganizer.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.login.LoginViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private RecyclerView tripsRecyclerView;
    private NavController controller;
    @Inject
    ViewModelProviderFactory providerFactory;
    private TripsViewModel tripsViewModel;
    @Inject
    FirebaseAuth firebaseAuth;

    Trip data;
    Note note;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        tripsViewModel = new ViewModelProvider(this, providerFactory).get(TripsViewModel.class);
        controller = Navigation.findNavController(view);
        TripsAdapter tripsAdapter = new TripsAdapter();
        //createDummyTrip();
       // tripsViewModel.addTrip(data);
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripsRecyclerView.setAdapter(tripsAdapter);
        tripsViewModel.getTripsList(firebaseAuth.getCurrentUser().getUid()).observe(requireActivity(), tripAndLocationList -> {
            List<TripAndLocation> tripAndLocations = tripAndLocationList;

            tripsAdapter.submitList(tripAndLocationList);
            Log.d("data", "we have trips .. ");
        });


    }

//    private void createDummyTrip() {
//        data = new Trip();
//        data.setTripName("Test Trip one");
//        data.setId(9);
//        data.setRound(false);
//        data.setUserId("b3JWEfSAnRf3UjJRZvyb17frnE43");
//        data.setStatus("Past");
//        LocationData locationData = new LocationData();
//        locationData.setId(8);
//        locationData.setTripId(9);
//        locationData.setStartDate(new Date());
//        locationData.setStartTripAddressName("Cairo");
//        locationData.setStartTripEndAddressName("Alexandria");
//        locationData.setStartTripStartPoint(151534);
//        locationData.setStartTripEndPoint(54545787);
//        data.setLocationData(locationData);
//        note = new Note("Note 1", 9, false);
//    }



    private void initViews(View view) {
        tripsRecyclerView = view.findViewById(R.id.tripsRecyclerView);
    }
}
