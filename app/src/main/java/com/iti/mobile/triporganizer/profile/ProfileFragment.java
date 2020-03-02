package com.iti.mobile.triporganizer.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.databinding.FragmentProfileBinding;
import com.iti.mobile.triporganizer.login.LoginViewModel;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    @Inject
    ViewModelProviderFactory providerFactory;
    private FragmentProfileBinding binding;

    private boolean isEditable=false;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        setUpViews();
        Log.i("test","ONVIEWCreated IsEditable................"+isEditable);
        if(isEditable){
            editProfile();
        }else{
            viewProfile();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.editImgView:
                isEditable =! isEditable;
                checkStatus(isEditable);
                break;
            case R.id.saveBtn:
                saveProfile();
                break;
        }
    }

    private void checkStatus(boolean isEditable) {
        if(isEditable){
            editProfile();
        }else{
            viewProfile();
        }
    }

    private void saveProfile() {

    }

    private void editProfile() {
        binding.editImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_orange_24dp));
        showProfileEditText();
        hideProfileTextViews();
        binding.logoutBtn.setVisibility(GONE);
        binding.saveBtn.setVisibility(VISIBLE);
    }

    private void hideProfileTextViews() {
        binding.userNameViewTv.setVisibility(GONE);
        binding.userEmailViewTv.setVisibility(GONE);
    }

    private void showProfileEditText() {
        binding.userEmailEt.setVisibility(VISIBLE);
        binding.userNameEt.setVisibility(VISIBLE);
    }

    private void viewProfile(){
        binding.editImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_gray_24dp));
        showProfileTextView();
        hideProfileEditText();
        binding.logoutBtn.setVisibility(VISIBLE);
        binding.saveBtn.setVisibility(GONE);
    }

    private void hideProfileEditText() {
        binding.userEmailEt.setVisibility(GONE);
        binding.userNameEt.setVisibility(GONE);
    }

    private void showProfileTextView() {
        binding.userNameViewTv.setVisibility(VISIBLE);
        binding.userEmailViewTv.setVisibility(VISIBLE);
    }

    public void setUpViews(){
        binding.editImgView.setOnClickListener(this);
        binding.saveBtn.setOnClickListener(this);
    }
}
