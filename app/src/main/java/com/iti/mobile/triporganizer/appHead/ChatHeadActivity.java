package com.iti.mobile.triporganizer.appHead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.details.NoteAdapter;
import com.sha.apphead.AppHead;
import com.sha.apphead.BadgeView;
import com.sha.apphead.DismissView;
import com.sha.apphead.Head;
import com.sha.apphead.HeadView;


import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class ChatHeadActivity extends AppCompatActivity {

    ImageView it ;
  Button   btnShowReadHead;
  Button btnUpdateBadge ;
  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_head);
        Button btnShowHead = findViewById(R.id.btnShowHead);
        it = findViewById(R.id.headImageView);
        btnShowReadHead = findViewById(R.id.btnShowReadHead);
        btnUpdateBadge= findViewById(R.id.btnUpdateBadge);
        btnShowHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDefault();
            }
        });




    }




    public void showDefault() {
        // build BadgeView


        HeadView.Args headViewArgs = new HeadView.Args()
                .layoutRes(R.layout.app_head_red, R.id.headImageView)
                .onLongClick(headView -> {
                    // your logic
                    return Unit.INSTANCE;
                })
                .alpha(0.9f)
                .allowBounce(false)
                .onFinishInflate(headView -> {
                    // your logic
                    recyclerView     = headView.findViewById(R.id.RV);
                    List<String> strings = new ArrayList<>();
                    strings.add("saturday");
                    strings.add("saturday");
                    strings.add("saturday");
                    strings.add("saturday");
                    strings.add("saturday");
                    strings.add("saturday");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                   // NoteAdapter noteAdapter = new NoteAdapter(this,strings);
                   // recyclerView.setAdapter(noteAdapter);
                    return Unit.INSTANCE;
                })
                .setupImage(imageView -> {
                    // your logic
                    return Unit.INSTANCE;
                })
                .onDismiss(headView -> {
                    // your logic
                    return Unit.INSTANCE;
                })
                .dismissOnClick(false)
                .preserveScreenLocation(false)
                .onClick(headView -> {
                    // your logic
                    if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);
                    else
                        recyclerView.setVisibility(View.GONE);

                    // showMessenger();
                    return Unit.INSTANCE;
                })
                ;

        // build DismissView
        DismissView.Args dismissViewArgs = new DismissView.Args()
                .alpha(0.5f)
                .scaleRatio(1.0)
                .drawableRes(R.drawable.ic_dismiss)
                .onFinishInflate(dismissView -> {
                    // your logic
                    return Unit.INSTANCE;
                })
                .setupImage(imageView -> {
                    // your logic
                    return Unit.INSTANCE;
                });

//        BadgeView.Args badgeViewArgs = new BadgeView.Args()
//                .layoutRes(R.layout.badge_view, R.id.tvCount)
//                .position(BadgeView.Position.BOTTOM_END)
//                .count("100");

        Head.Builder builder = new Head.Builder(R.drawable.ic_messenger_red)
                .headView(headViewArgs)
                .dismissView(dismissViewArgs);
                //.badgeView(badgeViewArgs);

        new AppHead(builder).show(this);
    }



}
