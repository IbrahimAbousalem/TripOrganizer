package com.iti.mobile.triporganizer.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.home.TripsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private RecyclerView tripsRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setDummyData();
        return view;
    }

    private void setDummyData() {
        List<Trip> trips = new ArrayList<>();
        for (int i =0; i <10; i++){
            Trip trip = new Trip();
            trip.setStatus("status"+i);
            trip.setUserId("1234"+i);
            trip.setDate(new Date());
            trip.setId("11"+i);
            trip.setType("mtip");
            trips.add(trip);
        }
        TripsAdapter tripsAdapter = new TripsAdapter();
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripsRecyclerView.setAdapter(tripsAdapter);
        tripsAdapter.submitList(trips);
    }

    private void initViews(View view) {
        tripsRecyclerView = view.findViewById(R.id.tripsRecyclerView);
    }
}
