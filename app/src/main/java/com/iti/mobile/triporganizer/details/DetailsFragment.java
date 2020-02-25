package com.iti.mobile.triporganizer.details;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.mobile.triporganizer.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.iti.mobile.triporganizer.utils.Flags.EDIT_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_ROUND;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_SINGLE;
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "DetailsFragment";
    private Button editBtn;
    private Button viewBtn;
    private ImageView bkImageView;
    private FloatingActionButton deleteFabBtn;

    private EditText tripTitleEt;
    private EditText endPointEt;
    private EditText startPointEt;
    private EditText date1Et;
    private EditText timeEt1;
    private EditText dateEt2;
    private EditText timeEt2;
    private TextView date1Tv;
    private TextView time1Tv;
    private TextView date2Tv;
    private TextView time2Tv;
    private Button singleBtn;
    private Button roundBtn;
    private ImageView addNoteImageView;
    private RecyclerView notes_recyclerview;
    private List<String> notesList;

    NoteAdapter noteAdapter;
    private int tripTypeChoice;
    private int tripActionChoice;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tripTypeChoice=TRIP_TYPE_SINGLE;
        tripActionChoice=VIEW_TRIP_FLAG;

        switch (tripActionChoice) {
            case VIEW_TRIP_FLAG:
                viewTrip(tripTypeChoice, tripActionChoice);
                break;
            case EDIT_TRIP_FLAG:
                editTrip(tripTypeChoice, tripActionChoice);
                break;
        }
    }

    private void showTripType(int tripType) {
        switch (tripType) {
            case TRIP_TYPE_SINGLE:
                showSingleTrip();
                break;
            case TRIP_TYPE_ROUND:
                showRoundTrip();
                break;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_details, container, false);
        setUpViews(root);
        showNotesList(root);
        return root;
    }

    private void showNotesList(View root) {
        notesList=new ArrayList<>();
        notesList.add("one");
        notesList.add("two");
        if (notesList.size() > 0) {
            notes_recyclerview = root.findViewById(R.id.notes_recyclerview);
            noteAdapter = new NoteAdapter(getContext(), notesList);
            notes_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            notes_recyclerview.setAdapter(noteAdapter);
        }
    }

    public void setUpViews(View root) {
        bkImageView = root.findViewById(R.id.bkImageView);
        bkImageView.setOnClickListener(this);
        editBtn = root.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(this);
        viewBtn = root.findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(this);
        deleteFabBtn = root.findViewById(R.id.deleteFab);
        deleteFabBtn.setOnClickListener(this);
        tripTitleEt = root.findViewById(R.id.tripTitleEt);
        startPointEt = root.findViewById(R.id.startPointEt);
        endPointEt = root.findViewById(R.id.endPointEt);
        date1Et = root.findViewById(R.id.date1Et);
        dateEt2 = root.findViewById(R.id.dateEt2);
        timeEt1 = root.findViewById(R.id.timeEt1);
        timeEt2 = root.findViewById(R.id.timeEt2);
        date1Tv = root.findViewById(R.id.date1Tv);
        time1Tv = root.findViewById(R.id.time1Tv);
        date2Tv = root.findViewById(R.id.date2Tv);
        time2Tv = root.findViewById(R.id.time2Tv);
        singleBtn = root.findViewById(R.id.singleBtn);
        singleBtn.setOnClickListener(this);
        roundBtn = root.findViewById(R.id.roundBtn);
        roundBtn.setOnClickListener(this);
        addNoteImageView = root.findViewById(R.id.addNoteImageView);
        addNoteImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bkImageView:
                goToHomeActivity();
                break;
            case R.id.editBtn:
                editTrip(tripTypeChoice, tripActionChoice);
                break;
            case R.id.viewBtn:
                viewTrip(tripTypeChoice, tripActionChoice);
                break;
            case R.id.deleteFab:
                deleteTrip();
                break;
            case R.id.singleBtn:
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundTrip();
                break;

        }
    }

    private void goToHomeActivity() {

    }

    private void deleteTrip() {

    }

    private void viewTrip(int tripTypeChoice, int tripActionChoice) {
        tripActionChoice = VIEW_TRIP_FLAG;
        focusViewButton();
        disableEditText();
        showTripType(tripTypeChoice);

    }

    private void editTrip(int tripTypeChoice, int tripActionChoice) {
       tripActionChoice= EDIT_TRIP_FLAG;
       focusEditButton();
       enableEditText();
       showTripType(tripTypeChoice);

    }

    private void disableEditText() {
        tripTitleEt.setEnabled(false);
        startPointEt.setEnabled(false);
        endPointEt.setEnabled(false);
        date1Et.setEnabled(false);
        timeEt1.setEnabled(false);
        dateEt2.setEnabled(false);
        timeEt2.setEnabled(false);
        addNoteImageView.setVisibility(GONE);

    }

    private void enableEditText() {
        tripTitleEt.setEnabled(true);
        startPointEt.setEnabled(true);
        endPointEt.setEnabled(true);
        date1Et.setEnabled(true);
        timeEt1.setEnabled(true);
        dateEt2.setEnabled(true);
        timeEt2.setEnabled(true);
        addNoteImageView.setVisibility(VISIBLE);
    }

    private void showRoundTrip() {
        focusRoundButton();
        showSecondDateTimeViews();
    }

    private void showSecondDateTimeViews() {
        date2Tv.setVisibility(VISIBLE);
        dateEt2.setVisibility(VISIBLE);
        time2Tv.setVisibility(VISIBLE);
        timeEt2.setVisibility(VISIBLE);
    }

    private void showSingleTrip() {
        focusSingleButton();
        hideSecondDateTimeViews();
    }

    private void hideSecondDateTimeViews() {
        date2Tv.setVisibility(GONE);
        dateEt2.setVisibility(GONE);
        time2Tv.setVisibility(GONE);
        timeEt2.setVisibility(GONE);
    }
    private void focusSingleButton() {
        singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        singleBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        roundBtn.setTextColor(getResources().getColor(R.color.darktxt));
    }

    private void focusRoundButton() {
        roundBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_clr_orange));
        roundBtn.setTextColor(getResources().getColor(R.color.whiteclr));
        singleBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn_no_clr));
        singleBtn.setTextColor(getResources().getColor(R.color.darktxt));
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


}
