package com.iti.mobile.triporganizer.utils;

import android.content.res.Resources;
import android.widget.EditText;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.databinding.FragmentAddTripBinding;
import com.iti.mobile.triporganizer.databinding.FragmentDetailsBinding;

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
    public static HashMap<String,String> generateDates(String date1,String date2,String time1,String time2){
        HashMap<String, String> hash_map = new HashMap<String, String>();
        hash_map.put(STARTDATE,date1);
        hash_map.put(ENDDATE,date2);
        hash_map.put(STARTTIME,time1);
        hash_map.put(ENDTIME,time2);
        return hash_map;
    }
    public static boolean isValidData(Trip trip, boolean isRound, FragmentAddTripBinding addTripBinding,FragmentDetailsBinding detailsBinding,
                                      AutocompleteSupportFragment startPointAutocompleteFragment, AutocompleteSupportFragment endPointAutocompleteFragment,
                                      HashMap<String,String> errorMessages,int bindingType,
                                      HashMap<String,String> dates){
        switch (bindingType){
            case 0:
                return handleAddTripBinding(trip,isRound,addTripBinding,errorMessages,dates,startPointAutocompleteFragment,endPointAutocompleteFragment);
            case 1:
                return handleEditTripBinding(trip,isRound,detailsBinding,errorMessages,dates,startPointAutocompleteFragment,endPointAutocompleteFragment);
        }

        return true;
    }
    private static boolean handleEditTripBinding(Trip trip, boolean isRound, FragmentDetailsBinding detailsBinding, HashMap<String, String> errorMessages,HashMap<String ,String> dates,AutocompleteSupportFragment startPointAutocompleteFragment,AutocompleteSupportFragment endPointAutocompleteFragment) {
        if (trip.getTripName().isEmpty()) {
            detailsBinding.tripNameEt.setError(errorMessages.get(TRIP_NAME));
            return false;
        } else {
            detailsBinding.tripNameEt.setError(null);
        }
        if (trip.getLocationData().getStartTripAddressName() == null) {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(START_POINT));
            return false;
        } else {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(null);
        }
        if (trip.getLocationData().getStartTripEndAddressName() == null) {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(END_POINT));
            return false;
        } else {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(null);
        }
        if (dates.get(STARTDATE).isEmpty()) {
            detailsBinding.date1Tv.setError(errorMessages.get(DATE));
            return false;
        } else {
            detailsBinding.date1Tv.setError(null);
        }
        if (dates.get(STARTTIME).isEmpty()) {
            detailsBinding.time1Tv.setError(errorMessages.get(TIME));
            return false;
        } else {
            detailsBinding.time1Tv.setError(null);
        }
        if(isRound){
            if (dates.get(ENDDATE).isEmpty()) {
                detailsBinding.date2Tv.setError(errorMessages.get(DATE));
                return false;
            } else {
                detailsBinding.date1Tv.setError(null);
            }
            if (dates.get(ENDTIME).isEmpty()) {
                detailsBinding.time2Tv.setError(errorMessages.get(TIME));
                return false;
            } else {
                detailsBinding.time2Tv.setError(null);
            }
            if (trip.getLocationData().getStartDate().compareTo(trip.getLocationData().getRoundDate()) > 0) {
                detailsBinding.date1Tv.setError(errorMessages.get(DATE_COMPARE));
                return false;
            } else {
                detailsBinding.date1Tv.setError(null);
            }
            if (trip.getLocationData().getStartDate().getTime() > trip.getLocationData().getRoundDate().getTime()) {
                detailsBinding.time1Tv.setError(errorMessages.get(TIME_COMPARE));
                return false;
            } else {
                detailsBinding.date1Tv.setError(null);
            }
        }
        return true;
    }
    private static boolean handleAddTripBinding(Trip trip, boolean isRound, FragmentAddTripBinding addTripBinding, HashMap<String, String> errorMessages,HashMap<String ,String> dates,AutocompleteSupportFragment startPointAutocompleteFragment,AutocompleteSupportFragment endPointAutocompleteFragment) {
        if (trip.getTripName().isEmpty()) {
            addTripBinding.tripNameEt.setError(errorMessages.get(TRIP_NAME));
            return false;
        } else {
            addTripBinding.tripNameEt.setError(null);
        }
        if (trip.getLocationData().getStartTripAddressName() == null) {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(START_POINT));
            return false;
        } else {
            ((EditText) startPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(null);
        }
        if (trip.getLocationData().getStartTripEndAddressName() == null) {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(errorMessages.get(END_POINT));
            return false;
        } else {
            ((EditText) endPointAutocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input))
                    .setError(null);
        }
        if (dates.get(STARTDATE).isEmpty()) {
            addTripBinding.date1Tv.setError(errorMessages.get(DATE));
            return false;
        } else {
            addTripBinding.date1Tv.setError(null);
        }
        if (dates.get(STARTTIME).isEmpty()) {
            addTripBinding.time1Tv.setError(errorMessages.get(TIME));
            return false;
        } else {
            addTripBinding.time1Tv.setError(null);
        }
        if(isRound){
            if (dates.get(ENDDATE).isEmpty()) {
                addTripBinding.date2Tv.setError(errorMessages.get(DATE));
                return false;
            } else {
                addTripBinding.date1Tv.setError(null);
            }
            if (dates.get(ENDTIME).isEmpty()) {
                addTripBinding.time2Tv.setError(errorMessages.get(TIME));
                return false;
            } else {
                addTripBinding.time2Tv.setError(null);
            }
            if (trip.getLocationData().getStartDate().compareTo(trip.getLocationData().getRoundDate()) > 0) {
                addTripBinding.date1Tv.setError(errorMessages.get(DATE_COMPARE));
                return false;
            } else {
                addTripBinding.date1Tv.setError(null);
            }
            if (trip.getLocationData().getStartDate().getTime() > trip.getLocationData().getRoundDate().getTime()) {
                addTripBinding.time1Tv.setError(errorMessages.get(TIME_COMPARE));
                return false;
            } else {
                addTripBinding.date1Tv.setError(null);
            }
        }
        return true;
    }

}
