package com.iti.mobile.triporganizer.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.databinding.FragmentLoginBinding;
import com.iti.mobile.triporganizer.utils.GoogleConfiguration;

import java.util.Arrays;

import javax.inject.Inject;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    public static final String CURRENT_USER = "currentUser";
    private static final int RC_SIGN_IN = 9001;

    NavController controller;
    @Inject
    ViewModelProviderFactory providerFactory;
    LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private GoogleConfiguration googleConfiguration;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViews(view);
        controller = Navigation.findNavController(view);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        loginViewModel = new ViewModelProvider(this, providerFactory).get(LoginViewModel.class);
    }

    @Override
    public void onClick(View v) {
        String email = binding.userEmailEt.getText().toString();
        String password = binding.passwordEt.getText().toString();
        switch (v.getId()) {
            case R.id.forgetPasswordTv:
                forgetPassword();
                break;
            case R.id.signInBtn:
                signInWithEmailAndPasswordView(email, password);
                break;
            case R.id.facebookImageView:
                signInWithFacebookView();
                break;
            case R.id.googleImageView:
                signInWithGoogleView();
                break;
            case R.id.signUpTv:
                goToSignUpActivity();
                break;
        }
    }

//Google----------------------------------------------------------

    private void signInWithGoogleView() {
        googleConfiguration = GoogleConfiguration.getInstance(getActivity());
        googleConfiguration.configureGoogle();
        mGoogleSignInClient = googleConfiguration.getGoogleClient();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = googleConfiguration.getGoogleAuthCredential(account);
                loginViewModel.signInWithGoogle(credential).observe(this, user -> {
                    if (user != null) {
                        updateUi(user);
                    } else {
                        updateUi(null);
                    }
                });
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

//----------------------------------------------------------------------------------------

//Facebook------------------------------------------------------------------------
    private void signInWithFacebookView() {
        configureFacebook();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }
    private void configureFacebook() {
      //  FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Success", "Login");
                loginViewModel.signInWithEFacebookVM(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.i("Cancel", "Cancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.i("Error", "error");
            }
        });
    }
//-------------------------------------------------------------------------------------------------------------

//Normal SignIn ------------------------------------------------------------------------------------------
    private void signInWithEmailAndPasswordView(String email, String password) {
        if(isValidData(email,password)){
            signInWithEmailAndPasswordViewModel(binding.userEmailEt.getText().toString(),binding.passwordEt.getText().toString());
        }
    }

    private void signInWithEmailAndPasswordViewModel(String email,String password){
        loginViewModel.signInWithEmailAndPasswordVM(email,password).observe(this, currentUser->{
            if(currentUser!=null){
                updateUi(currentUser);
            }else{
                updateUi(null);
            }
        });
    }
//-----------------------------------------------------------------------------------------------------

//Forget Password ---------------------------------------------------------------
    private void forgetPassword() {
        controller.navigate(R.id.action_loginFragment_to_homeFragment);
    }
//-----------------------------------------------------------------------------

//Sign UP------------------------------------------------------------
    private void goToSignUpActivity() {
        controller.navigate(R.id.action_loginFragment_to_registerFragment);
    }
//---------------------------------------------------------------

//Utils-----------------------------------------------------------------
    private boolean isValidData(String email, String password) {
        if(email.isEmpty()){
            binding.userEmailEt.setError(getString(R.string.enteremail));
            return false;
        }
        if(password.isEmpty()){
            binding.passwordEt.setError(getString(R.string.enterpassword));
            return false;
        }
        return true;
    }

    private void updateUi(User currentUser) {
        if (currentUser != null) {
            controller.navigate(R.id.action_loginFragment_to_homeFragment);
            //sendData?
            //gotoHomeActivity(currentUser);
        } else {
            displayError();
        }
    }

    private void displayError() {
        Log.i(TAG, "Login Failed !");
        //TODO:RESPOND TO DIFFERENT EXCEPTION
    }

    private void setUpViews(View view) {
        binding.forgetPasswordTv.setOnClickListener(this);
        binding.signInBtn.setOnClickListener(this);
        binding.facebookImageView.setOnClickListener(this);
        binding.googleImageView.setOnClickListener(this);
        binding.signUpTv.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
