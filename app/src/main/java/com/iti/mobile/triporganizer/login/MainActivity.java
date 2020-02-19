package com.iti.mobile.triporganizer.login;

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

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.data.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    //ViewModelProviderFactory factory;
    private EditText userEmailEt;
    private EditText passwordEt;
    private TextView forgetPasswordTv;
    private Button signInBtn;
    private ImageView facebookImageView;
    private ImageView googleImgView;
    private TextView signUpTv;

    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
       // authViewModel = ViewModelProvider(this, factory).get(AuthViewModel.class);
        authViewModel= ViewModelProviders.of(this).get(AuthViewModel.class);
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
                signInWithFacebook();
                break;
            case R.id.googleImageView:
                signInWithGoogle();
                break;
            case R.id.signUpTv:
                goToSignUpActivity();
                break;

        }
    }

    private void goToSignUpActivity() {
    }
    private void signInWithGoogle() {
    }
    private void signInWithFacebook() {
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
