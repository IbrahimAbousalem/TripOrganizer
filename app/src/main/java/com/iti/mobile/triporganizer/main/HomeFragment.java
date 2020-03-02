package com.iti.mobile.triporganizer.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.LocationData;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.entities.Trip;
import com.iti.mobile.triporganizer.data.entities.TripAndLocation;
import com.iti.mobile.triporganizer.utils.RecyclerItemTouchHelper;

import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    public static final String TAG = "HomeFragment";
    private RecyclerView tripsRecyclerView;
    private NavController controller;
    @Inject
    ViewModelProviderFactory providerFactory;
    private TripsViewModel tripsViewModel;
    @Inject
    FirebaseAuth firebaseAuth;
    TripsAdapter tripsAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        tripsViewModel = new ViewModelProvider(this, providerFactory).get(TripsViewModel.class);
        controller = Navigation.findNavController(view);
        tripsAdapter = new TripsAdapter();
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripsRecyclerView.setAdapter(tripsAdapter);
        String userId = tripsViewModel.getCurrentUserId();
        tripsViewModel.getUpComingTripsFromRoom(userId).observe(requireActivity(), tripAndLocationList -> {
            if (tripAndLocationList.isEmpty()) {
                tripsViewModel.getTripsFromFirebase(userId).observe(requireActivity(), tripAndLocationList1 ->{

                    tripsAdapter.submitList(tripAndLocationList);
                });
            }else {
                tripsAdapter.submitList(tripAndLocationList);
                Log.d("data", "we have trips .. ");
            }
        });

    }

    private void initViews(View view) {
        tripsRecyclerView = view.findViewById(R.id.tripsRecyclerView);
        tripsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tripsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(tripsRecyclerView);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//        if (viewHolder instanceof TripsAdapter.UpcomingTripViewHolder){
//            viewHolder = (TripsAdapter.UpcomingTripViewHolder) viewHolder;
//
//        }else if (viewHolder instanceof TripsAdapter.TripsViewHolder){
//            viewHolder = (TripsAdapter.TripsViewHolder) viewHolder;
//
//        }else {
//            viewHolder = (TripsAdapter.UpcomingTripsTextViewHolder) viewHolder;
//
//        }
            // get the removed item name to display it in snack bar
            List<TripAndLocation> tripAndLocationList = tripsAdapter.getCurrentList();

            // backup of removed item for undo purpose
            TripAndLocation deletedItem = new TripAndLocation();
            TripAndLocation mdeletedItem = tripAndLocationList.get(viewHolder.getAdapterPosition());
            deletedItem.setLocationDataList(mdeletedItem.getLocationDataList());
            deletedItem.setTrip(mdeletedItem.getTrip());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            tripsViewModel.deleteTrip(mdeletedItem.getTrip());
//            tripAndLocationList.remove(deletedIndex);
  //          tripsAdapter.notifyItemRemoved(deletedIndex);

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(view, deletedItem.getTrip().getTripName() + " removed from trips!", Snackbar.LENGTH_LONG);
        /*
        snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    tripsViewModel.addTrip(deletedItem.getTrip());
                  //  tripAndLocationList.add(deletedIndex, deletedItem);
                    //tripsAdapter.notifyItemInserted(deletedIndex);
                }
            });
         */
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

    }
}
