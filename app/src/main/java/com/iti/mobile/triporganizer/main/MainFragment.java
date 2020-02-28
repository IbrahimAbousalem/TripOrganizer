package com.iti.mobile.triporganizer.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.databinding.FragmentMainBinding;
import com.iti.mobile.triporganizer.history.HistoryFragment;
import com.iti.mobile.triporganizer.utils.FragmentUtils;


public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private NavController navController;
    private HistoryFragment historyFragment;
    private HomeFragment homeFragment;
    private FloatingActionButton add_button;
    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        add_button = binding.addButton;
        BottomNavigationView bottomNavigation = binding.bottomNavigation;
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.botton_navigation_my_trips);
        handleActions();
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void handleActions() {
        add_button.setOnClickListener((view)->{
            navController.navigate(R.id.action_mainFragment2_to_detailsFragment);
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.botton_navigation_my_trips:
                    showHomeFragment();
                    return  true;

                case R.id.botton_navigation_history:
                    showHistoryFragment();
                    return true;

            }
            return false;
        }

    };

    private void showHistoryFragment() {
        if (historyFragment == null){
            historyFragment = new HistoryFragment();
        }
        FragmentUtils.showFragment((AppCompatActivity) getActivity(), R.id.frame_container, historyFragment, HistoryFragment.TAG, true);
    }

    private void showHomeFragment() {
        if (homeFragment == null){
            homeFragment = new HomeFragment();
        }
        FragmentUtils.showFragment((AppCompatActivity) getActivity(), R.id.frame_container, homeFragment, HomeFragment.TAG, true);
    }
}
