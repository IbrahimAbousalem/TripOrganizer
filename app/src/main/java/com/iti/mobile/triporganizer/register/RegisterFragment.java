package com.iti.mobile.triporganizer.register;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;

import java.util.HashMap;
import java.util.Map;

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
            String email = emailEditText.getText().toString();
            String userName = userNameEditText.getText().toString();

            if (!isValidEmail(email)) {
                emailTextInputLayout.setError(getString(R.string.valid_email));
                return;
            }
            String password = passwordEditText.getText().toString();
            if (password.length() <= 3) {
                passwordTextInputLayout.setError(getString(R.string.valid_password));
                return;
            }
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (!password.equals(confirmPassword)) {
                confirmPasswordTextInputLayout.setError(getString(R.string.valid_confirm_password));
                return;
            }
            User user = new User(userName, "", email, "", "");
            registerViewModel.registerUser(user, password).observe(getActivity(), s -> {
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
        registerViewModel = new ViewModelProvider(this, providerFactory).get(RegisterViewModel.class);
    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


}
