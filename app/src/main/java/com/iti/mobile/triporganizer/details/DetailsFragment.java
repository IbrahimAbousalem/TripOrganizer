package com.iti.mobile.triporganizer.details;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.iti.mobile.triporganizer.utils.Flags.EDIT_TRIP_FLAG;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_ROUND;
import static com.iti.mobile.triporganizer.utils.Flags.TRIP_TYPE_SINGLE;
import static com.iti.mobile.triporganizer.utils.Flags.VIEW_TRIP_FLAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DetailsFragment";

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

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        setUpViews(root);
        showNotesList(root);
        return root;
    }

    private void showNotesList(View root) {
        notesList.add("one");
        notesList.add("two");
        if (notesList.size() > 0) {
            notes_recyclerview = root.findViewById(R.id.notes_recyclerview);
            noteAdapter = new NoteAdapter(getContext(), notesList);
            notes_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            notes_recyclerview.setAdapter(noteAdapter);
        }
    }

    private void setUpViews(View root) {
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

    public void recieveTripType(int tripType, int tripTag) {
        Log.i(TAG, "------------------------------------------recievetriptype" + tripType);
        switch (tripTag) {
            case VIEW_TRIP_FLAG:
                disableEditText();
                showTripType(tripType);
                break;
            case EDIT_TRIP_FLAG:
                enableEditText();
                showTripType(tripType);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleBtn:
                showSingleTrip();
                break;
            case R.id.roundBtn:
                showRoundTrip();
                break;
        }
    }

    private void addNote(String note) {
        notesList.add(note);
        Log.i("test", "...........................................................noteslist...................................................." + notesList.size());
    }
}
