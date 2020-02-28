package com.iti.mobile.triporganizer.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.base.MainActivity;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.utils.GoogleConfiguration;

import javax.inject.Inject;

import io.opencensus.tags.Tag;

public class TestHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signout_btn;
    private TextView user_tv;
    private LoginViewModel loginViewModel;
    private User currentUser;
    private static final String TAG = "TestHomeActivity";
    @Inject
    ViewModelProviderFactory providerFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_home);
        signout_btn=findViewById(R.id.button);
        user_tv=findViewById(R.id.textView);

        ((TripOrganizerApp)getApplication()).getComponent().newControllerComponent(new ControllerModule(this)).inject(this);

        loginViewModel = new ViewModelProvider(this, providerFactory).get(LoginViewModel.class);

 //       Intent intent=getIntent();
//        currentUser=(User)intent.getSerializableExtra("currentUser");
//        user_tv.setText("welcome "+currentUser.getUserName());
//        Log.i(TAG,"...............Current User......................");
//        Log.i(TAG,"............profile image "+currentUser.getProfilePicUrl()+"..............");
//        Log.i(TAG,"............username "+currentUser.getUserName()+"..............");
//        Log.i(TAG,"............email "+currentUser.getEmail()+"..............");
//        Log.i(TAG,"............provider id "+currentUser.getProvider_id()+"..............");
        signout_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                signOutUser();
                break;
        }
    }

    private void signOutUser() {
       loginViewModel.signOutVM();
//       if(currentUser.getProvider_id().equals("google.com")){
//           //sign out from google !
//       }
        GoogleConfiguration.getInstance(getApplicationContext()).getGoogleClient().signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "you signed out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}