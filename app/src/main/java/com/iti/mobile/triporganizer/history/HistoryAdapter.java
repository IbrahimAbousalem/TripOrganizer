package com.iti.mobile.triporganizer.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.details.NoteAdapter;
import com.iti.mobile.triporganizer.utils.Constants;
import com.iti.mobile.triporganizer.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<TripAndLocation> tripsList;

    public HistoryAdapter(List<TripAndLocation> tripsList) {
        this.tripsList = tripsList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_trip_card, parent,false);
        return new HistoryViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.setTripNameTV(tripsList.get(position).getTrip().getTripName());
        holder.setTripDateTV(DateUtils.simpleDateFormatForYears_MonthsHours_Minutes.format(tripsList.get(position).getLocationDataList().getStartDate()));
        holder.setTripLocationTV(tripsList.get(position).getLocationDataList().getStartTripAddressName());
        holder.setTripStatusTV(tripsList.get(position).getTrip().getStatus());

        holder.itemView.setOnClickListener(
                view -> {
                    HistoryFragmentDirections.ActionHistoryFragmentToHistoryDetailFragment historyFragmentDirections = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment(tripsList.get(position));
                    Navigation.findNavController(view).navigate(historyFragmentDirections);
                });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void setData(List<TripAndLocation> tripsList){
        this.tripsList = tripsList;
        notifyDataSetChanged();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
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
            if(tripStatus.equals(Constants.CANCELED)){
                this.tripStatusBtn.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.orange_rounded_btn));
            }else{
                this.tripStatusBtn.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.blue_rounded_btn));
            }
            this.tripStatusBtn.setText(tripStatus);
        }
    }
}
