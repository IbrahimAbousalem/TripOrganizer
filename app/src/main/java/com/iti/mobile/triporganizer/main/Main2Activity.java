package com.iti.mobile.triporganizer.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.databinding.ActivityMain2Binding;

public class Main2Activity extends AppCompatActivity {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private ActivityMain2Binding activityMain2Binding;
    private FloatingActionButton addButton;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         activityMain2Binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(activityMain2Binding.getRoot());
        bottomNavigationView = activityMain2Binding.bottomNavigation;
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
        navController = navHostFragment.getNavController();


        addButton = activityMain2Binding.addButton;
        addButton.setOnClickListener((view)->{
            navController.navigate(R.id.action_homeFragment_to_addTripFragment3);
        });



    }
}
