package com.iti.mobile.triporganizer.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.home.HomeActivity;
import com.iti.mobile.triporganizer.login.MainActivity;

import static com.iti.mobile.triporganizer.utils.Flags.EDIT_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, Communicator {

    private static final String TAG = "DetailsFragment";
    private Button editBtn;
    private Button viewBtn;
    private ImageView bkImageView;
    private CheckBox selectTripCheckBox;
    private ConstraintLayout detailsFragmentContainer;
    private FloatingActionButton deleteFabBtn;
    private int tripTypeIntent;
    private int tripTagIntent;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setUpViews();
        intent = getIntent();
        tripTypeIntent = intent.getIntExtra(MainActivity.TRIP_TYPE, 4);
        tripTagIntent = intent.getIntExtra(MainActivity.TRIP_TAG, 3);
        setupDetailsFragment();

    }

    public void setupDetailsFragment() {
        DetailsFragment detailsFragment = new DetailsFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.deatilsFragmentContainer, detailsFragment, TAG);
        ft.commit();
    }

    public void setUpViews() {
        bkImageView = findViewById(R.id.bkImageView);
        bkImageView.setOnClickListener(this);
        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
        viewBtn = findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(this);
        deleteFabBtn = findViewById(R.id.deleteFab);
        deleteFabBtn.setOnClickListener(this);
        detailsFragmentContainer = findViewById(R.id.deatilsFragmentContainer);

        selectTripCheckBox = findViewById(R.id.selectTripCheckBox);
        selectTripCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (tripTagIntent) {
            case VIEW_TRIP_FLAG:
                viewTrip(tripTypeIntent, tripTagIntent);
                break;
            case EDIT_TRIP_FLAG:
                editTrip(tripTypeIntent, tripTagIntent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bkImageView:
                goToHomeActivity();
            case R.id.editBtn:
                editTrip(tripTypeIntent, tripTagIntent);
                break;
            case R.id.viewBtn:
                viewTrip(tripTypeIntent, tripTagIntent);
                break;
            case R.id.deleteFab:
                deleteTrip();
                break;

        }
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void viewTrip(int tripType, int tripTag) {
        tripTag = VIEW_TRIP_FLAG;
        focusViewButton();
        sendTripType(tripType, tripTag);

    }

    private void editTrip(int tripType, int tripTag) {
        tripTag = EDIT_TRIP_FLAG;
        focusEditButton();
        sendTripType(tripType, tripTag);
    }

    private void deleteTrip() {

    }

    private void focusEditButton() {
        editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        editBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_white_24dp, 0, 0, 0);
        viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        viewBtn.setTextColor(getResources().getColor(R.color.darktxt));
        viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_gray_24dp, 0, 0, 0);

    }

    private void focusViewButton() {
        viewBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        viewBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        viewBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_white_24dp, 0, 0, 0);
        editBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        editBtn.setTextColor(getResources().getColor(R.color.darktxt));
        editBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_gray_24dp, 0, 0, 0);
    }

    @Override
    public void sendTripType(int tripType, int tripTag) {
        ((DetailsFragment) getSupportFragmentManager().findFragmentByTag(TAG)).recieveTripType(tripType, tripTag);
    }
}
