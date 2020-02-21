package com.iti.mobile.triporganizer.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.base.MainActivity;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;

import javax.inject.Inject;

public class TestHomeActivity extends AppCompatActivity {

    private Button signout_btn;
    private TextView user_tv;
    private LoginViewModel loginViewModel;
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

        Intent intent=getIntent();
        User currentUser=(User)intent.getSerializableExtra("currentUser");

        user_tv.setText("Current user is id is "+currentUser.getId()
                +"\n Current User name is "+currentUser.getUserName()
        +"\n LoginMethod is : " + currentUser.getProvider_id()
        +"\n photourl"+ currentUser.getProfilePicUrl()
        +"\n email"+currentUser.getEmail()
        );

        Log.i("testurl",currentUser.getProfilePicUrl());
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.signOutVM();
                finish();
            }
        });
    }
}