package com.iti.mobile.triporganizer.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;

public class TestHomeActivity extends AppCompatActivity {

    private Button signout_btn;
    private TextView user_tv;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_home);
        signout_btn=findViewById(R.id.button);
        user_tv=findViewById(R.id.textView);
        Intent intent=getIntent();
        String currentUserId=intent.getStringExtra(MainActivity.CURRENT_USER);
        user_tv.setText("Current user is "+currentUserId);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.signOutVM();
                finish();
            }
        });
    }
}
