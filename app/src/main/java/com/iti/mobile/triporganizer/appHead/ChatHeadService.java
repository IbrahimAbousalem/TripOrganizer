package com.iti.mobile.triporganizer.appHead;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.dagger.module.controller.ChatHeadServiceControllerModule;
import com.iti.mobile.triporganizer.data.entities.Note;
import com.iti.mobile.triporganizer.data.room.dao.NoteDao;
import com.iti.mobile.triporganizer.data.room.dao.TripDao;
import com.iti.mobile.triporganizer.data.shared_prefs.SharedPreferenceUtility;
import com.iti.mobile.triporganizer.details.NoteAdapter;
import com.iti.mobile.triporganizer.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import static com.iti.mobile.triporganizer.utils.Constants.NO_DATA;
import static com.iti.mobile.triporganizer.utils.Constants.USER_ID;

public class ChatHeadService extends Service   {
    private WindowManager mWindowManager;
    private View mFloatingView;
    RecyclerView recyclerView;
    @Inject
    SharedPreferences sharedPref;
    @Inject
    NoteDao noteDao;
    @Inject
    TripDao tripDao;
    String tripId;
    public ChatHeadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        tripId = intent.getStringExtra("tripId");
        ((TripOrganizerApp) getApplication()).getComponent().newServiceControllerComponent(new ChatHeadServiceControllerModule(this)).inject(this);
        List<Note> noteList = noteDao.getAllNoteNotLive(Integer.parseInt(tripId));
        if (noteList != null && noteList.size() > 0) {
            NoteAdapter noteAdapter = new NoteAdapter(getApplicationContext(), noteList);
            recyclerView.setAdapter(noteAdapter);
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.chat_head_service, null);
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            params.x = 0;
            params.y = 100;
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mFloatingView, params);

        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT);


            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            params.x = 0;
            params.y = 100;
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mFloatingView, params);
        }
        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);

        //Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                tripDao.updateStatus(Integer.parseInt(tripId), sharedPref.getString(USER_ID,NO_DATA), Constants.FINISHED);
                stopSelf();
            }
        });

        //Set the view while floating view is expanded.
        //Set the play button.

       recyclerView     = mFloatingView.findViewById(R.id.RV);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Button endTripBT = mFloatingView.findViewById(R.id.endTripBT);

        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }


    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

}
