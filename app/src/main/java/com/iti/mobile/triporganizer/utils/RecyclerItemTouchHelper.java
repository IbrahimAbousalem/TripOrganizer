package com.iti.mobile.triporganizer.utils;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.main.TripsAdapter;

import static com.iti.mobile.triporganizer.main.TripsAdapter.TYPE_HEADER;
import static com.iti.mobile.triporganizer.main.TripsAdapter.TYPE_Text;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView= null;
            switch (viewHolder.getAdapterPosition()){
                case  0:
                    foregroundView = ((TripsAdapter.UpcomingTripViewHolder) viewHolder).itemView;
                    break;
                case  1:
                    foregroundView = ((TripsAdapter.UpcomingTripsTextViewHolder) viewHolder).itemView;
                    break;
                case  2:
                    foregroundView = ((TripsAdapter.TripsViewHolder) viewHolder).itemView;
                    break;
            }
            if (foregroundView != null)
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        View foregroundView= null;
        switch (viewHolder.getAdapterPosition()){
            case  0:
                foregroundView = ((TripsAdapter.UpcomingTripViewHolder) viewHolder).itemView;
                break;
            case  1:
                foregroundView = ((TripsAdapter.UpcomingTripsTextViewHolder) viewHolder).itemView;
                break;
            case  2:
                foregroundView = ((TripsAdapter.TripsViewHolder) viewHolder).itemView;
                break;
        }
        if (foregroundView != null)
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView= null;
        switch (viewHolder.getAdapterPosition()){
            case  0:
                foregroundView = ((TripsAdapter.UpcomingTripViewHolder) viewHolder).itemView;
                break;
            case  1:
                foregroundView = ((TripsAdapter.UpcomingTripsTextViewHolder) viewHolder).itemView;
                break;
            case  2:
                foregroundView = ((TripsAdapter.TripsViewHolder) viewHolder).itemView;
                break;
        }
        if (foregroundView != null)
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        View foregroundView= null;
        switch (viewHolder.getAdapterPosition()){
            case  0:
                foregroundView = ((TripsAdapter.UpcomingTripViewHolder) viewHolder).itemView;
                break;
            case  1:
                foregroundView = ((TripsAdapter.UpcomingTripsTextViewHolder) viewHolder).itemView;
                break;
            case  2:
                foregroundView = ((TripsAdapter.TripsViewHolder) viewHolder).itemView;
                break;
        }
        if (foregroundView != null)
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}

