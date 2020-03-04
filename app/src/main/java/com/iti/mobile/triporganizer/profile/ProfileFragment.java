package com.iti.mobile.triporganizer.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.app.TripOrganizerApp;
import com.iti.mobile.triporganizer.app.ViewModelProviderFactory;
import com.iti.mobile.triporganizer.base.MainActivity;
import com.iti.mobile.triporganizer.dagger.module.controller.ControllerModule;
import com.iti.mobile.triporganizer.data.entities.User;
import com.iti.mobile.triporganizer.databinding.FragmentProfileBinding;
import com.iti.mobile.triporganizer.login.LoginViewModel;
import com.iti.mobile.triporganizer.utils.GoogleConfiguration;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.iti.mobile.triporganizer.utils.FirestoreConstatnts.USERS_COLLECTION;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ProfileFragment";

    private NavController controller;

    @Inject
    ViewModelProviderFactory providerFactory;
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    private boolean isEditable=false;
    private String providerId="";

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
        controller = Navigation.findNavController(view);
        ((TripOrganizerApp) getActivity().getApplication()).getComponent().newControllerComponent(new ControllerModule(getActivity())).inject(this);
        profileViewModel = new ViewModelProvider(this, providerFactory).get(ProfileViewModel.class);

        profileViewModel.getCurrentUserVm().observe(getViewLifecycleOwner(),user -> {
            if(user!=null){
                setUserDate(user);
            }else{
                goToSignIn();
            }

        });
        setUpViews();
        Log.i("test","ONVIEWCreated IsEditable................"+isEditable);
        if(isEditable){
            editProfile();
        }else{
            viewProfile();
        }
    }

    private void goToSignIn() {
        controller.navigate(R.id.action_profileFragment3_to_loginFragment);
    }

    private void setUserDate(User user) {
        Log.i(TAG,"........................username : "+user.getUserName());
        Log.i(TAG,"........................useremail : "+user.getEmail());
        Log.i(TAG,"........................user id  : "+user.getId());
        Log.i(TAG,"........................provider id : "+user.getProvider_id());
        Log.i(TAG,"........................profile picture: "+user.getProfilePicUrl());
        providerId=user.getProvider_id();
        binding.userNameTv.setText(user.getUserName());
        binding.userEmailEt.setText(user.getEmail());
        binding.userEmailViewTv.setText(user.getEmail());
        binding.userNameEt.setText(user.getUserName());
        binding.userNameViewTv.setText(user.getUserName());
        if(!user.getProfilePicUrl().isEmpty()){
            Picasso.get().load(user.getProfilePicUrl()).into(binding.profileImageView);
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
            case R.id.logoutBtn:
                logout();
                break;
        }
    }

    private void logout() {
        profileViewModel.signOutVM().observe(this,provider_id->{
            switch (provider_id){
                case "google.com":
                    signOutGoogle();
                    break;
            }
        });
    }

    private void signOutGoogle(){
        GoogleConfiguration.getInstance(getContext()).getGoogleClient().signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "you signed out successfully!", Toast.LENGTH_SHORT).show();
                goToSignIn();
            }
        });
    }

    private void saveProfile() {
        isValidData();
        if(isValidData()){
            //  TODO:UPDATE DATA BASE FIRE STORE!!
            profileViewModel.changeEmail(binding.userEmailEt.getText().toString().trim()).observe(this,isUpdated->{
                if(isUpdated){
                    showToast(getResources().getString(R.string.email_success_updated));
                }else{
                    showToast(getResources().getString(R.string.email_failed_updated));
                }
            });
            profileViewModel.changePassword(binding.passwordEt.getText().toString().trim()).observe(this,isUpdated->{
                if(isUpdated){
                    showToast(getResources().getString(R.string.pass_success_updated));
                }else{
                    showToast(getResources().getString(R.string.pass_failed_updated));
                }
            });


        }
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private boolean isValidData() {
        if(!isValidEmail(binding.userEmailEt.getText().toString().trim())){
            binding.userEmailEt.setError(getString(R.string.valid_email));
            return false;
        }
        if(binding.userNameEt.getText().toString().trim().isEmpty()){
            binding.userNameEt.setError(getString(R.string.valid_userName));
            return false;
        }
        if(binding.passwordEt.getText().toString().isEmpty()){
            binding.passwordEt.setError(getResources().getString(R.string.valid_password));
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    private void checkStatus(boolean isEditable) {
        if(isEditable){
            editProfile();
        }else{
            viewProfile();
        }
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
        binding.passwordEt.setVisibility(VISIBLE);
        binding.passwordTextInputLayout.setVisibility(VISIBLE);
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
        binding.passwordEt.setVisibility(GONE);
        binding.passwordTextInputLayout.setVisibility(GONE);
    }

    private void showProfileTextView() {
        binding.userNameViewTv.setVisibility(VISIBLE);
        binding.userEmailViewTv.setVisibility(VISIBLE);
    }

    public void setUpViews(){
        binding.taskToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_profileFragment3_to_homeFragment);
            }
        });
        binding.editImgView.setOnClickListener(this);
        binding.saveBtn.setOnClickListener(this);
        binding.logoutBtn.setOnClickListener(this);
    }
}
