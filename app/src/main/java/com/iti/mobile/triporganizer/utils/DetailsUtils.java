package com.iti.mobile.triporganizer.utils;

import android.util.Log;
import android.widget.EditText;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.databinding.FragmentAddTripBinding;
import com.iti.mobile.triporganizer.databinding.FragmentDetailsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.iti.mobile.triporganizer.utils.Tags.DATE;
import static com.iti.mobile.triporganizer.utils.Tags.DATE_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.ENDDATE;
import static com.iti.mobile.triporganizer.utils.Tags.ENDTIME;
import static com.iti.mobile.triporganizer.utils.Tags.END_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.STARTDATE;
import static com.iti.mobile.triporganizer.utils.Tags.STARTTIME;
import static com.iti.mobile.triporganizer.utils.Tags.START_POINT;
import static com.iti.mobile.triporganizer.utils.Tags.TIME;
import static com.iti.mobile.triporganizer.utils.Tags.TIME_COMPARE;
import static com.iti.mobile.triporganizer.utils.Tags.TRIP_NAME;

public class DetailsUtils {
    private static final String TAG = "DetailsUtils";

    public static boolean isValidData(Trip trip, boolean isRound, FragmentAddTripBinding addTripBinding,FragmentDetailsBinding detailsBinding,
                                      AutocompleteSupportFragment startPointAutocompleteFragment, AutocompleteSupportFragment endPointAutocompleteFragment,
                                      HashMap<String,String> errorMessages,int bindingType,
                                      HashMap<String,String> dates){
        switch (bindingType){
            case 0:
                return handleAddTripBinding(trip,isRound,addTripBinding,errorMessages,startPointAutocompleteFragment,endPointAutocompleteFragment,dates);
            case 1:
                return handleEditTripBinding(trip,isRound,detailsBinding,errorMessages,startPointAutocompleteFragment,endPointAutocompleteFragment,dates);
        }

        return true;
    }
    private static boolean handleEditTripBinding(Trip trip, boolean isRound, FragmentDetailsBinding detailsBinding, HashMap<String, String> errorMessages,AutocompleteSupportFragment startPointAutocompleteFragment,AutocompleteSupportFragment endPointAutocompleteFragment,HashMap<String,String> dates) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");

        String startDate=dates.get(STARTDATE);
        String endDate=dates.get(ENDDATE);
        String startTime=dates.get(STARTTIME);
        String endTime=dates.get(ENDTIME);

        Date date1=null;
        Date date2=null;
        Date time1=null;
        Date time2=null;

        try {
            if(!startDate.isEmpty()){
                date1=dateFormat.parse(startDate);
                Log.i(TAG,"////////////////////////////////////string date1 "+startDate);
                Log.i(TAG,"////////////////////////////////////date1 "+date1.toString());
            }
            if(!startTime.isEmpty()){
                time1=timeFormat.parse(startTime);
                Log.i(TAG,"////////////////////////////////////string time1 "+startTime);
                Log.i(TAG,"////////////////////////////////////time1 "+time1.toString());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (trip.getTripName().isEmpty()) {
            detailsBinding.tripNameEt.setError(errorMessages.get(TRIP_NAME));
            return false;
        }
        if(startDate.isEmpty()){
            detailsBinding.date1Tv.setError(errorMessages.get(DATE));
            return false;
        }
        if (startTime.isEmpty()) {
            detailsBinding.time1Tv.setError(errorMessages.get(TIME));
            return false;
        }
        if (trip.getLocationData().getStartTripAddressName() == null) {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(START_POINT));
            return false;
        }

        if (trip.getLocationData().getStartTripEndAddressName() == null) {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(END_POINT));
            return false;
        }
        if(isRound){
            try {
                if(!endDate.isEmpty()){
                    date2=dateFormat.parse(endDate);
                    Log.i(TAG,"//////////////////////////////////// string date2 "+endDate);
                    Log.i(TAG,"//////////////////////////////////// date2 "+date2.toString());
                }
                if(!endTime.isEmpty()){
                    time2=timeFormat.parse(endTime);
                    Log.i(TAG,"//////////////////////////////////// string time2 "+endTime);
                    Log.i(TAG,"//////////////////////////////////// time2"+time2.toString());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (endDate.isEmpty()) {
                detailsBinding.date2Tv.setError(errorMessages.get(DATE));
                return false;
            }
            if (endTime.isEmpty()) {
                detailsBinding.time2Tv.setError(errorMessages.get(TIME));
                return false;
            }
            if (date1.compareTo(date2) > 0) {
                Log.i(TAG,"DATE1.........................................."+date1.toString());
                Log.i(TAG,"DATE2.........................................."+date2.toString());
                Log.i(TAG,"date 2 must be after date 1");
                detailsBinding.date1Tv.setError(errorMessages.get(DATE_COMPARE));
                detailsBinding.date2Tv.setError(errorMessages.get(DATE_COMPARE));
                return false;
            }
            if (time1.compareTo(time2) > 0) {
                detailsBinding.time1Tv.setError(errorMessages.get(TIME_COMPARE));
                detailsBinding.time2Tv.setError(errorMessages.get(TIME_COMPARE));
                return false;
            }
        }
        return true;
    }
    private static boolean handleAddTripBinding(Trip trip, boolean isRound, FragmentAddTripBinding addTripBinding, HashMap<String, String> errorMessages,AutocompleteSupportFragment startPointAutocompleteFragment,AutocompleteSupportFragment endPointAutocompleteFragment,HashMap<String,String> dates) {
        String startDate=dates.get(STARTDATE);
        String endDate=dates.get(ENDDATE);
        String startTime=dates.get(STARTTIME);
        String endTime=dates.get(ENDTIME);

        Date date1=null;
        Date date2=null;
        Date time1=null;
        Date time2=null;

        try {
            if(!startDate.isEmpty()){
                date1=dateFormat.parse(startDate);
                Log.i(TAG,"////////////////////////////////////string date1 "+startDate);
                Log.i(TAG,"////////////////////////////////////date1 "+date1.toString());
            }
            if(!startTime.isEmpty()){
                time1=timeFormat.parse(startTime);
                Log.i(TAG,"////////////////////////////////////string time1 "+startTime);
                Log.i(TAG,"////////////////////////////////////time1 "+time1.toString());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (trip.getTripName().isEmpty()) {
            addTripBinding.tripNameEt.setError(errorMessages.get(TRIP_NAME));
            return false;
        }
        if(startDate.isEmpty()){
            addTripBinding.date1Tv.setError(errorMessages.get(DATE));
            return false;
        }
        if (startTime.isEmpty()) {
            addTripBinding.time1Tv.setError(errorMessages.get(TIME));
            return false;
        }
        if (trip.getLocationData().getStartTripAddressName() == null) {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(START_POINT));
            return false;
        }

        if (trip.getLocationData().getStartTripEndAddressName() == null) {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(END_POINT));
            return false;
        }
        if(isRound){
            try {
                if(!endDate.isEmpty()){
                    date2=dateFormat.parse(endDate);
                    Log.i(TAG,"//////////////////////////////////// string date2 "+endDate);
                    Log.i(TAG,"//////////////////////////////////// date2 "+date2.toString());
                }
                if(!endTime.isEmpty()){
                    time2=timeFormat.parse(endTime);
                    Log.i(TAG,"//////////////////////////////////// string time2 "+endTime);
                    Log.i(TAG,"//////////////////////////////////// time2"+time2.toString());
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (endDate.isEmpty()) {
                addTripBinding.date2Tv.setError(errorMessages.get(DATE));
                return false;
            }
            if (endTime.isEmpty()) {
                addTripBinding.time2Tv.setError(errorMessages.get(TIME));
                return false;
            }
            if (date1.compareTo(date2) > 0) {
                Log.i(TAG,"DATE1.........................................."+date1.toString());
                Log.i(TAG,"DATE2.........................................."+date2.toString());
                Log.i(TAG,"date 2 must be after date 1");
                addTripBinding.date1Tv.setError(errorMessages.get(DATE_COMPARE));
                addTripBinding.date2Tv.setError(errorMessages.get(DATE_COMPARE));
                return false;
            }
            if (time1.compareTo(time2) > 0) {
                addTripBinding.time1Tv.setError(errorMessages.get(TIME_COMPARE));
                addTripBinding.time2Tv.setError(errorMessages.get(TIME_COMPARE));
                return false;
            }
        }
        return true;
    }

}
