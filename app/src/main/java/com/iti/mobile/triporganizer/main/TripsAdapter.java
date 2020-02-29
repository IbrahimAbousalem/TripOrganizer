package com.iti.mobile.triporganizer.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;

import java.util.Objects;

public class TripsAdapter extends ListAdapter<TripAndLocation,  RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_Text = 2;

    public TripsAdapter() {
        super(diffCallback);
    }

    private static final DiffUtil.ItemCallback<TripAndLocation> diffCallback = new DiffUtil.ItemCallback<TripAndLocation>() {
        @Override
        public boolean areItemsTheSame(@NonNull TripAndLocation oldItem, @NonNull TripAndLocation newItem) {
            return Objects.equals(oldItem.getTrip().getId(), newItem.getTrip().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull TripAndLocation oldItem, @NonNull TripAndLocation newItem) {
            return oldItem.getTrip().getStatus().equals(newItem.getTrip().getStatus())&&
                    oldItem.getTrip().getUserId().equals(newItem.getTrip().getUserId())&&
                    oldItem.getLocationDataList().getStartDate().getTime()==newItem.getLocationDataList().getStartDate().getTime();
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                View upcomingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_trip_card, parent, false);
                return new UpcomingTripViewHolder(upcomingView);
            case TYPE_ITEM:
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_trip_card, parent, false);
                return new TripsViewHolder(textView);
            case TYPE_Text:
                View pastTripsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomig_trips_text_item, parent, false);
                return new UpcomingTripsTextViewHolder(pastTripsView);
            default: return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  UpcomingTripViewHolder){
            TripAndLocation trip = getItem(position);
            UpcomingTripViewHolder upcomingTripViewHolder = (UpcomingTripViewHolder) holder;
            upcomingTripViewHolder.setTripNameTv(trip.getTrip().getTripName());
            upcomingTripViewHolder.setTripDateTv(trip.getLocationDataList().getStartDate().toString());
            upcomingTripViewHolder.setTripTimeTv(trip.getLocationDataList().getStartDate().getHours() +":"+ trip.getLocationDataList().getStartDate().getMinutes());
            upcomingTripViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());
        }else if (holder instanceof  TripsViewHolder){
            TripAndLocation trip = getItem(position);
            TripsViewHolder tripsViewHolder = (TripsViewHolder) holder;
            tripsViewHolder.setTripNameTv(trip.getTrip().getTripName());
            tripsViewHolder.setTripDateTv(trip.getLocationDataList().getStartDate().toString());
            tripsViewHolder.setTripStatusTv(trip.getTrip().getStatus());
            tripsViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());
        }else {
            TripAndLocation trip = getItem(position);
            UpcomingTripsTextViewHolder upcomingTripsTextViewHolder = (UpcomingTripsTextViewHolder) holder;
            upcomingTripsTextViewHolder.setTripNameTv(trip.getTrip().getTripName());
            upcomingTripsTextViewHolder.setTripDateTv(trip.getLocationDataList().getStartDate().toString());
            upcomingTripsTextViewHolder.setTripStatusTv(trip.getTrip().getStatus());
            upcomingTripsTextViewHolder.setTripLocTv(trip.getLocationDataList().getStartTripAddressName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0: return TYPE_HEADER;
            case 1: return TYPE_Text;
            default: return TYPE_ITEM;
        }

    }

     public class UpcomingTripViewHolder extends RecyclerView.ViewHolder {
        private TextView tripNameTv, tripDateTv, tripTimeTv, tripLocTv;
        private Button startBtn, viewBtn;
        private UpcomingTripViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripTimeTv = itemView.findViewById(R.id.tripTimeTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
            startBtn = itemView.findViewById(R.id.startBtn);
            viewBtn = itemView.findViewById(R.id.viewBtn);
           // itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment2_to_detailsFragment));
        }


        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripTimeTv(String time) {
            tripTimeTv.setText(time);
        }
        public void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
    private class TripsViewHolder extends RecyclerView.ViewHolder{
        private TextView tripNameTv, tripDateTv, tripLocTv;
        private Button statusTv;
        private TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
            statusTv = itemView.findViewById(R.id.statusBtn);
          //  itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_mainFragment2_to_detailsFragment));
        }

        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripStatusTv(String status) {
            statusTv.setText(status);
        }
        private void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
    private class UpcomingTripsTextViewHolder extends RecyclerView.ViewHolder{
        private TextView upcomig_textView;
        private TextView tripNameTv, tripDateTv, tripLocTv;
        private Button statusTv;
        private UpcomingTripsTextViewHolder(@NonNull View itemView) {
            super(itemView);
            upcomig_textView = itemView.findViewById(R.id.upcomig_textView);
            tripNameTv = itemView.findViewById(R.id.tripNameTv);
            tripDateTv = itemView.findViewById(R.id.tripDateTv);
            tripLocTv = itemView.findViewById(R.id.tripLocTv);
            statusTv = itemView.findViewById(R.id.statusBtn);
           // itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_historyFragment));
        }
        private void setTripNameTv(String tripName) {
            tripNameTv.setText(tripName);
        }
        private void setTripDateTv(String tripDate) {
            tripDateTv.setText(tripDate);
        }
        private void setTripStatusTv(String status) {
            statusTv.setText(status);
        }
        private void setTripLocTv(String location) {
            tripLocTv.setText(location);
        }
    }
}
