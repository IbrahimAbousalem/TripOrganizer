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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
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

    NavController controller;
    @Inject
    ViewModelProviderFactory providerFactory;
    LoginViewModel loginViewModel;

    private TextInputEditText userEmailEt;
    private TextInputEditText passwordEt;
    private TextView forgetPasswordTv;
    private Button signInBtn;
    private ProgressBar progressBar;
    private ImageView facebookImageView;
    private ImageView googleImgView;
    private TextView signUpTv;

    private GoogleConfiguration googleConfiguration;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
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
        String email = userEmailEt.getText().toString();
        String password = passwordEt.getText().toString();
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
        progressBar.setVisibility(VISIBLE);
    }
    private void hideProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
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
            signInWithEmailAndPasswordViewModel(userEmailEt.getText().toString(),passwordEt.getText().toString());
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
            userEmailEt.setError(getString(R.string.enteremail));
            return false;
        }
        if(password.isEmpty()){
            passwordEt.setError(getString(R.string.enterpassword));
            return false;
        }
        return true;
    }

    private void updateUi(User currentUser) {
        hideProgressBar();
        if (currentUser != null) {
           // controller.navigate(R.id.action_loginFragment_to_homeFragment);
            getActivity().startActivity(new Intent(getActivity(), TestHomeActivity.class) );
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
        userEmailEt = view.findViewById(R.id.userEmailEt);
        passwordEt = view.findViewById(R.id.passwordEt);
        forgetPasswordTv = view.findViewById(R.id.forgetPasswordTv);
        forgetPasswordTv.setOnClickListener(this);
        signInBtn = view.findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        progressBar=view.findViewById(R.id.progressBar);
        facebookImageView = view.findViewById(R.id.facebookImageView);
        facebookImageView.setOnClickListener(this);
        googleImgView = view.findViewById(R.id.googleImageView);
        googleImgView.setOnClickListener(this);
        signUpTv = view.findViewById(R.id.signUpTv);
        signUpTv.setOnClickListener(this);
    }


}
