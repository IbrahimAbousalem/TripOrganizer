package com.iti.mobile.triporganizer.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;

public class HomeFragment extends Fragment {

    private NavController controller;
    TextView dummyText;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        dummyText = view.findViewById(R.id.text);
        dummyText.setOnClickListener((v)->{
            controller.navigate(R.id.action_homeFragment_to_historyFragment);
        });

    }
}
