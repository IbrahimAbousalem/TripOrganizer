package com.iti.mobile.triporganizer.dialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.iti.mobile.triporganizer.R;

public class TestDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);
    }

    public void startDialog(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BuyItemsDialog buyItemsDialog = new BuyItemsDialog();
        buyItemsDialog.show(fragmentManager,null);
    }

    public void showDeleteDialog(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DeleteTripDialog buyItemsDialog = new DeleteTripDialog();
        buyItemsDialog.show(fragmentManager,null);
    }
}
