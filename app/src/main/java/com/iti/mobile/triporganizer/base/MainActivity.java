package com.iti.mobile.triporganizer.base;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding activityMainBinding;
    private FloatingActionButton addButton;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        bottomNavigationView = activityMainBinding.bottomNavigation;
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());
        navController = navHostFragment.getNavController();
        addButton = activityMainBinding.addButton;
        navHostFragment.getNavController().addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                //TODO bottom navigation is circular fix it
                if (destination.getId() == R.id.homeFragment || destination.getId() == R.id.historyFragment){
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    if (destination.getId() == R.id.homeFragment) {
                        addButton.setOnClickListener((view) -> {
                            navController.navigate(R.id.action_homeFragment_to_addTripFragment);
                        });
                    }else {addButton.setOnClickListener((view) -> {
                        navController.navigate(R.id.action_historyFragment_to_addTripFragment);
                    });}
                }else {
                    bottomNavigationView.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                }
            }
        });


    }
}
