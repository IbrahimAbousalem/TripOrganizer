package com.iti.mobile.triporganizer.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.data.viewmodel.AuthViewModel;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final String CURRENT_USER = "currentUser";

    //ViewModelProviderFactory factory;
    private EditText userEmailEt;
    private EditText passwordEt;
    private TextView forgetPasswordTv;
    private Button signInBtn;
    private ImageView facebookImageView;
    private ImageView googleImgView;
    private TextView signUpTv;

    private AuthViewModel authViewModel;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();

        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Success", "Login");
                handleFacebookAccessToken(loginResult.getAccessToken());
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

        // authViewModel = ViewModelProvider(this, factory).get(AuthViewModel.class);
        authViewModel= ViewModelProviders.of(this).get(AuthViewModel.class);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    public void setUpViews(){
        userEmailEt=findViewById(R.id.userEmailEt);
        passwordEt=findViewById(R.id.passwordEt);
        forgetPasswordTv=findViewById(R.id.forgetPasswordTv);
        forgetPasswordTv.setOnClickListener(this);
        signInBtn=findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(this);
        facebookImageView =findViewById(R.id.facebookImageView);
        facebookImageView.setOnClickListener(this);
        googleImgView=findViewById(R.id.googleImageView);
        googleImgView.setOnClickListener(this);
        signUpTv=findViewById(R.id.signUpTv);
        signUpTv.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authViewModel.signInWithGoogleVM(account).observe(this,currentUserId->{
                    if(!currentUserId.isEmpty()){
                        updateUi(currentUserId);
                    }else{
                        updateUi("");
                    }
                });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authViewModel.getCurrentUserId().observe(this,currentUserId->{
            if(!currentUserId.isEmpty()){
                updateUi(currentUserId);
            }else{
                updateUi("");
            }
        });
    }

    private void signInWithEmailAndPasswordViewModel(String email,String password){
        authViewModel.signInWithEmailAndPasswordVM(email,password).observe(this,currentUserId->{
            if(!currentUserId.isEmpty()){
                updateUi(currentUserId);
            }else{
                updateUi("");
            }
        });
    }
    private void signInWithGoogleView() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void updateUi(String currentUserId) {
        if(!currentUserId.isEmpty()){
            gotoHomeActivity(currentUserId);
        }else{
            displayError();
        }
    }

    private void displayError() {
        Log.i(TAG,"Login Failed !");
        //TODO:RESPOND TO DIFFERENT EXCEPTION
    }

    private void gotoHomeActivity(String currentUserId) {
        Log.i(TAG,"Current user id is "+currentUserId);
        Intent intent=new Intent(MainActivity.this,TestHomeActivity.class);
        intent.putExtra(CURRENT_USER,currentUserId);
        startActivity(intent);
        //TODO:Need to send currentUserId to home activity to later retrieve trips attached to this id
    }

    @Override
    public void onClick(View v) {
        String email=userEmailEt.getText().toString();
        String password=passwordEt.getText().toString();
        switch (v.getId()){
            case R.id.forgetPasswordTv:
                forgetPassword();
                break;
            case R.id.signInBtn:
                signInWithEmailAndPasswordView(email,password);
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

    private void goToSignUpActivity() {
    }
    private void signInWithFacebookView() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }
    private void forgetPassword() {
    }
    private void signInWithEmailAndPasswordView(String email,String password) {
        isValidData(email,password);
        if(isValidData(email,password)){
            signInWithEmailAndPasswordViewModel(userEmailEt.getText().toString(),passwordEt.getText().toString());
        }
    }

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
}
