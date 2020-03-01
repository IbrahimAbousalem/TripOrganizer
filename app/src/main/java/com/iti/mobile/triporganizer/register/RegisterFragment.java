package com.iti.mobile.triporganizer.register;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.databinding.FragmentRegisterBinding;


import java.util.Objects;

import javax.inject.Inject;

import static android.view.View.VISIBLE;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";

    @Inject
    ViewModelProviderFactory providerFactory;
    private RegisterViewModel registerViewModel ;
    private FragmentRegisterBinding binding;

    private NavController controller;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void updateUi(String message) {
        hideProgressBar();
        if (message != null&&!message.isEmpty()&&!message.contains("Error")) {
            controller.navigate(R.id.action_registerFragment_to_homeFragment);
        } else {
            displayError(message);
        }
    }

    private void displayError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void showProgressBar(){
        binding.progressBar.setVisibility(VISIBLE);
    }
    private void hideProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        ((TripOrganizerApp) Objects.requireNonNull(getActivity()).getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        registerViewModel = new ViewModelProvider(this, providerFactory).get(RegisterViewModel.class);

        binding.signupBtn.setOnClickListener(v -> registerUser());
        binding.goToSignInTV.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void registerUser() {
        showProgressBar();
        String email = Objects.requireNonNull(binding.emailEditText.getText()).toString();
        String userName = Objects.requireNonNull(binding.userNameEditText.getText()).toString();
        String password = Objects.requireNonNull(binding.passwordEditText.getText()).toString();
        String confirmPassword = Objects.requireNonNull(binding.confirmPasswordEditText.getText()).toString();
        if(!validateData(email, userName, password, confirmPassword)) return;
        User user = new User(userName, "", email, "", "");
        registerViewModel.registerUser(user, password).observe(getViewLifecycleOwner(), s -> {
            binding.progressBar.setVisibility(View.GONE);
            updateUi(s);
        });
    }

    private boolean validateData(String email, String userName, String password, String confirmPassword) {
        if (userName.isEmpty() || userName.length()< 3) {
            binding.userNameEditText.setError(getString(R.string.valid_userName));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }
        if (!isValidEmail(email)) {
            binding.userNameEditText.setError(null);
            binding.emailEditText.setError(getString(R.string.valid_email));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }
        if(password.isEmpty()){
            binding.emailEditText.setError(null);
            binding.passwordEditText.setError(getString(R.string.empty_password));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }else if (password.length() <= 3) {
            binding.passwordEditText.setError(getString(R.string.valid_password));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }
        if(confirmPassword.isEmpty()){
            binding.confirmPasswordEditText.setError(getString(R.string.empty_confirm_password));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }else if (!password.equals(confirmPassword)) {
            binding.confirmPasswordEditText.setError(getString(R.string.valid_confirm_password));
            binding.progressBar.setVisibility(View.GONE);
            return false;
        }
        binding.passwordEditText.setError(null);
        binding.confirmPasswordEditText.setError(null);
        return true;
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
