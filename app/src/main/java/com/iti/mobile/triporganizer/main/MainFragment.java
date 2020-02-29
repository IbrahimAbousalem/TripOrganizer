package com.iti.mobile.triporganizer.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

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

    private FloatingActionButton add_button;
    BottomNavigationView bottomNavigation;
    private final HomeFragment homeFragment = new HomeFragment();
    private final HistoryFragment historyFragment = new HistoryFragment();
    Fragment activeFragment= homeFragment;
    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        add_button = binding.addButton;
        bottomNavigation = binding.bottomNavigation;
      //  bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // bottomNavigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);
        handleActions();
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

       // bottomNavigation.setSelectedItemId(R.id.botton_navigation_my_trips);
    }

    @Override
    public void onResume() {

       // bottomNavigation.setSelectedItemId(R.id.botton_navigation_my_trips);
        super.onResume();
    }

    private void handleActions() {
        add_button.setOnClickListener((view)->{
            navController.navigate(R.id.action_mainFragment2_to_addTripFragment);
        });
    }
    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.homeFragment:
                    FragmentUtils.hideFragment((AppCompatActivity) getContext(),activeFragment);
                    showHomeFragment();
                    activeFragment = homeFragment;
                    break;

                case R.id.historyFragment:
                    FragmentUtils.hideFragment((AppCompatActivity) getContext(),activeFragment);
                    showHistoryFragment();
                    activeFragment = historyFragment;
                    break;

            }

        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.homeFragment:
                    showHomeFragment();
                    return  true;

                case R.id.historyFragment:
                    showHistoryFragment();
                    return true;

            }
            return false;
        }

    };

    private void showHistoryFragment() {

        FragmentUtils.showFragment((AppCompatActivity) getActivity(), R.id.frame_container, historyFragment, HistoryFragment.TAG, false);
    }

    private void showHomeFragment() {

        FragmentUtils.showFragment((AppCompatActivity) getActivity(), R.id.frame_container, homeFragment, HomeFragment.TAG, false);
    }
}
