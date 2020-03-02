package com.iti.mobile.triporganizer.history;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.utils.DateUtils;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HistoryDetailFragment extends Fragment implements
        OnMapReadyCallback {
    SupportMapFragment mapFragment ;
    private GoogleMap mMap;
    MarkerOptions origin, destination;

    BottomSheetBehavior bottomSheetBehavior ;
    private TripAndLocation receivedTripAndLocationHistory;
    private TextView bottomSheetTripNameTV,bottomSheetTripSourceTV,bottomSheetTripDestinationTV,bottomSheetTripDateTV,bottomSheetTripTimeTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_detail, container, false);
        receivedTripAndLocationHistory = HistoryDetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getTripAndLocationHistory();
        initViews(view);
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        origin = new MarkerOptions().position(new LatLng(receivedTripAndLocationHistory.getLocationDataList().getStartTripStartPointLat(),
                receivedTripAndLocationHistory.getLocationDataList().getStartTripStartPointLng())).title(receivedTripAndLocationHistory.getTrip().getTripName())
                .snippet(receivedTripAndLocationHistory.getLocationDataList().getStartTripAddressName());
        destination = new MarkerOptions().position(new LatLng(receivedTripAndLocationHistory.getLocationDataList().getStartTripEndPointLat(),
                receivedTripAndLocationHistory.getLocationDataList().getStartTripEndPointLng())).title(receivedTripAndLocationHistory.getTrip().getTripName())
                .snippet(receivedTripAndLocationHistory.getLocationDataList().getStartTripEndAddressName());

       //  Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        View bottomSheet = view.findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                       // btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                      //  btnBottomSheet.setText("Expand Sheet");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        return view;
    }

    private void initViews(View view) {
        bottomSheetTripNameTV = view.findViewById(R.id.bottomSheetTripNameTV) ;
        bottomSheetTripSourceTV = view.findViewById(R.id.bottomSheetTripSourceTV);
        bottomSheetTripDestinationTV = view.findViewById(R.id.bottomSheetTripDestinationTV);
        bottomSheetTripDateTV = view.findViewById(R.id.bottomSheetTripDateTV);
        bottomSheetTripTimeTV = view.findViewById(R.id.bottomSheetTripTimeTV);

        bottomSheetTripNameTV.setText(receivedTripAndLocationHistory.getTrip().getTripName());
        bottomSheetTripSourceTV.setText(receivedTripAndLocationHistory.getLocationDataList().getStartTripAddressName());
        bottomSheetTripDestinationTV.setText(receivedTripAndLocationHistory.getLocationDataList().getStartTripEndAddressName());
        bottomSheetTripDateTV.setText(DateUtils.simpleDateFormatForYears_Months.format(receivedTripAndLocationHistory.getLocationDataList().getStartDate()));
        bottomSheetTripTimeTV.setText(DateUtils.simpleDateFormatForHours_Minutes.format(receivedTripAndLocationHistory.getLocationDataList().getStartDate()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap ;
        mMap.addMarker(origin);
        mMap.addMarker(destination);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin.getPosition(), 12));


    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            HistoryDetailFragment.ParserTask parserTask = new HistoryDetailFragment.ParserTask();
            parserTask.execute(result);
        }

    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Setting mode
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDrhEBrA9oj5N0zD7VAwk0EHv0exMbc0Cc";

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map

            if (points.size() != 0)
                mMap.addPolyline(lineOptions);
        }
    }
}
