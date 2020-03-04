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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.iti.mobile.triporganizer.databinding.FragmentLoginBinding;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.GoogleConfiguration;

import java.util.Arrays;

import javax.inject.Inject;

import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    public static final String CURRENT_USER = "currentUser";
    private static final int RC_SIGN_IN = 9001;

    private NavController controller;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        loginViewModel = new ViewModelProvider(this, providerFactory).get(LoginViewModel.class);
//
//        loginViewModel.getCurrentUserId().observe(getViewLifecycleOwner(), userId -> {
//            if(!userId.equals(Constants.NO_DATA)){
//                controller.navigate(R.id.action_loginFragment_to_main2Activity);
//            }
//        });

        String userId = loginViewModel.getCurrentUserId();
        if(!userId.equals(Constants.NO_DATA)){
            controller.navigate(R.id.action_loginFragment_to_homeFragment);
        }

        setUpViews();
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

    private void showProgressBar(){
        binding.progressBar.setVisibility(VISIBLE);
    }
    private void hideProgressBar(){
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
//Google----------------------------------------------------------

    private void signInWithGoogleView() {
        googleConfiguration = GoogleConfiguration.getInstance(getActivity().getApplicationContext());
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
                showProgressBar();
                loginViewModel.signInWithGoogle(credential).observe(this, user -> {
                   updateUi(user);
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
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
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
            showProgressBar();
            signInWithEmailAndPasswordViewModel(binding.userEmailEt.getText().toString(),binding.passwordEt.getText().toString());
        }
    }

    private void signInWithEmailAndPasswordViewModel(String email,String password){
        loginViewModel.signInWithEmailAndPasswordVM(email,password).observe(this, currentUser->{
            updateUi(currentUser);
        });
    }
//-----------------------------------------------------------------------------------------------------

//Forget Password ---------------------------------------------------------------
    private void forgetPassword() {
        //controller.navigate(R.id.action_loginFragment_to_homeFragment);
        //controller.navigate(R.id.action_loginFragment_to_profileFragment3);
        //controller.navigate(R.id.action_loginFragment_to_detailsFragment);
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

    private void updateUi(String message) {
        hideProgressBar();
        if (message != null&&!message.isEmpty()&&!message.contains("Error")) {
            controller.navigate(R.id.action_loginFragment_to_homeFragment);
        } else {
            displayError(message);
        }
    }

    private void displayError(String message) {
        if(message.contains("email")||message.contains("Email")){
            binding.userEmailEt.setError(message);
        }else if(message.contains("password")||message.contains("Password")){
            binding.passwordEt.setError(message);
        }else {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpViews() {
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
