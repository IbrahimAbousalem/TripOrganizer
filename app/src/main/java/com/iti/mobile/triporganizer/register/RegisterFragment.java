package com.iti.mobile.triporganizer.register;


import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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


import javax.inject.Inject;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private TextInputLayout emailTextInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout userNameInputLayout;
    private TextInputEditText userNameEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputEditText confirmPasswordEditText;
    private Button signUpButton;
    private TextView goToSignInTV;
    private ProgressBar progressBar ;
    private boolean userIsExist = false;
    @Inject
    ViewModelProviderFactory providerFactory;
    private RegisterViewModel registerViewModel ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        setUpViews(view);

        signUpButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString();
            String userName = userNameEditText.getText().toString();

            if (userName.isEmpty() || userName.length()< 3) {
                userNameEditText.setError(getString(R.string.valid_userName));
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (!isValidEmail(email)) {
                userNameEditText.setError(null);
                emailEditText.setError(getString(R.string.valid_email));
                progressBar.setVisibility(View.GONE);
                return;
            }
            String password = passwordEditText.getText().toString();
            if(password.isEmpty()){
                emailEditText.setError(null);
                passwordEditText.setError(getString(R.string.empty_password));
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (password.length() <= 3) {
                passwordEditText.setError(getString(R.string.valid_password));
                progressBar.setVisibility(View.GONE);
                return;
            }
            String confirmPassword = confirmPasswordEditText.getText().toString();
            if(confirmPassword.isEmpty()){
                confirmPasswordEditText.setError(getString(R.string.empty_confirm_password));
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError(getString(R.string.valid_confirm_password));
                progressBar.setVisibility(View.GONE);
                return;
            }
            passwordEditText.setError(null);
            confirmPasswordEditText.setError(null);
            User user = new User(userName, "", email, "", "");
            registerViewModel.registerUser(user, password).observe(getActivity(), s -> {
                progressBar.setVisibility(View.GONE);
                if(s.equals("Register Successfully")){
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                }
            });
        });
                goToSignInTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        return view;
    }



    private void setUpViews(View view) {
        userNameInputLayout = view.findViewById(R.id.userNameInputLayout);
        userNameEditText = view.findViewById(R.id.userNameEditText);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        signUpButton = view.findViewById(R.id.signupBtn);
        goToSignInTV = view.findViewById(R.id.goToSignInTV);
        progressBar = view.findViewById(R.id.progressBar);
        registerViewModel = new ViewModelProvider(this, providerFactory).get(RegisterViewModel.class);
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


}
