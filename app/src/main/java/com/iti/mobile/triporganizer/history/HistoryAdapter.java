package com.iti.mobile.triporganizer.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends ListAdapter<Trip, HistoryAdapter.HistoryViewHolder> {

    protected HistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Trip> DIFF_CALLBACK = new DiffUtil.ItemCallback<Trip>() {
        @Override
        public boolean areItemsTheSame(@NonNull Trip oldItem, @NonNull Trip newItem) {
            return oldItem.getId()!= newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Trip oldItem, @NonNull Trip newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_trip_card, parent,false);
        return new HistoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.setTripNameTV(getItem(position).getTripName());
        holder.setTripDateTV(getItem(position).getLocationData().getStartDate().toString());
        holder.setTripLocationTV(getItem(position).getLocationData().getStartTripAddressName());
        holder.setTripStatusTV(getItem(position).getStatus());
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tripNameTV, tripDateTV, tripLocationTV;
        private Button tripStatusBtn;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTV = itemView.findViewById(R.id.tripNameTv);
            tripDateTV = itemView.findViewById(R.id.tripDateTv);
            tripLocationTV = itemView.findViewById(R.id.tripLocTv);
            tripStatusBtn = itemView.findViewById(R.id.statusBtn);
        }

        public void setTripNameTV(String tripName) {
            this.tripNameTV.setText(tripName);
        }

        public void setTripDateTV(String tripDate) {
            this.tripDateTV.setText(tripDate);
        }

        public void setTripLocationTV(String tripLocation) {
            this.tripLocationTV.setText(tripLocation);
        }

        public void setTripStatusTV(String tripStatus) {
            if(tripStatus.equals("Canceled")){
                this.tripStatusBtn.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.orange_rounded_btn));
            }else{
                this.tripStatusBtn.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.blue_rounded_btn));
            }
            this.tripStatusBtn.setText(tripStatus);
        }
    }
}
