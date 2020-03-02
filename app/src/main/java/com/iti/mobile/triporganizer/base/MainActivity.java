package com.iti.mobile.triporganizer.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this,
                        "Draw over other app permission is available",
                        Toast.LENGTH_SHORT).show();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
