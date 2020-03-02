package com.iti.mobile.triporganizer.history;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.databinding.FragmentHistoryBinding;
import com.iti.mobile.triporganizer.databinding.FragmentLoginBinding;
import com.iti.mobile.triporganizer.main.TripsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    public static final String TAG = "HistoryFragment";
    private RecyclerView historyRecycleView;
    private HistoryAdapter adapter;
    @Inject
    ViewModelProviderFactory providerFactory;
    private HistoryViewModel historyViewModel;
    private NavController controller;

    private FragmentHistoryBinding binding;


    public HistoryFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        ((TripOrganizerApp) Objects.requireNonNull(getActivity()).getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        historyViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(HistoryViewModel.class);
        controller = Navigation.findNavController(view);
        String userId = historyViewModel.getCurrentUserId();
        historyViewModel.getAllHistoryTrips(userId).observe(getActivity(), tripAndLocationList -> {
            adapter.submitList(tripAndLocationList);
            //get it from firebase.
        });

    }

    private void initView() {
        historyRecycleView = binding.historyRecyclerRV;
        historyRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter();
        historyRecycleView.setAdapter(adapter);
    }
}
