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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.databinding.FragmentLoginBinding;
import com.iti.mobile.triporganizer.databinding.FragmentRegisterBinding;


import javax.inject.Inject;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private boolean userIsExist = false;
    @Inject
    ViewModelProviderFactory providerFactory;
    private RegisterViewModel registerViewModel ;
    private FragmentRegisterBinding binding;

    NavController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding.signupBtn.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String email = binding.emailEditText.getText().toString();
            String userName = binding.userNameEditText.getText().toString();
            if (userName.isEmpty() || userName.length()< 3) {
                binding.userNameEditText.setError(getString(R.string.valid_userName));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            if (!isValidEmail(email)) {
                binding.userNameEditText.setError(null);
                binding.emailEditText.setError(getString(R.string.valid_email));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            String password = binding.passwordEditText.getText().toString();
            if(password.isEmpty()){
                binding.emailEditText.setError(null);
                binding.passwordEditText.setError(getString(R.string.empty_password));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            if (password.length() <= 3) {
                binding.passwordEditText.setError(getString(R.string.valid_password));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            String confirmPassword = binding.confirmPasswordEditText.getText().toString();
            if(confirmPassword.isEmpty()){
                binding.confirmPasswordEditText.setError(getString(R.string.empty_confirm_password));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                binding.confirmPasswordEditText.setError(getString(R.string.valid_confirm_password));
                binding.progressBar.setVisibility(View.GONE);
                return;
            }
            binding.passwordEditText.setError(null);
            binding.confirmPasswordEditText.setError(null);
            User user = new User(userName, "", email, "", "");
            registerViewModel.registerUser(user, password).observe(getActivity(), s -> {
                binding.progressBar.setVisibility(View.GONE);
                if(s.equals("Register Successfully")){
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            });
        });
                binding.goToSignInTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        registerViewModel = new ViewModelProvider(this, providerFactory).get(RegisterViewModel.class);
    }
    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
