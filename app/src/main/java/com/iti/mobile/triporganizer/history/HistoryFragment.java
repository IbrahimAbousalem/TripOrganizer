package com.iti.mobile.triporganizer.history;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    public static final String TAG = "HistoryFragment";
    private RecyclerView historyRecycleView;
    private HistoryAdapter adapter;
    private NavController controller;
    List<Trip> data;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //Dagger
        //ViewModel
        controller = Navigation.findNavController(view);
    }

    private void initView(View view) {
        historyRecycleView = view.findViewById(R.id.historyRecyclerRV);
        historyRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter();
        historyRecycleView.setAdapter(adapter);
    }
}
