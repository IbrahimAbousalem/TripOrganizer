package com.iti.mobile.triporganizer.register;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iti.mobile.triporganizer.R;
import com.iti.mobile.triporganizer.data.entities.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private FirebaseFirestore firebaseFirestore;
    private TextInputLayout emailTextInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputEditText confirmPasswordEditText;
    private Button signUpButton;
    private TextView goToSignInTV;
    private boolean userIsExist = false ;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        setUpViews(view);
          initFireStore();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if(!isValidEmail(email)){
                    emailTextInputLayout.setError(getString(R.string.valid_email));
                    return;
                }
                String password = passwordEditText.getText().toString();
                if (password.length() <= 3){
                    passwordTextInputLayout.setError(getString(R.string.valid_password));
                    return;
                }
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (!password.equals(confirmPassword)){
                    confirmPasswordTextInputLayout.setError(getString(R.string.valid_confirm_password));
                    return;
                }
                Map<String, Object> user = new HashMap<>();

                user.put("email", email);
                user.put("password", password);


                firebaseFirestore.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if (document.getData().containsValue(email)) {

                                            Toast.makeText(getActivity(),"This email is already exist before",Toast.LENGTH_LONG).show();
                                            return;
                                        } else {

                                            firebaseFirestore.collection("users")
                                                    .add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.e(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Error adding document", e);

                                                }
                                            });
                                            return;
                                        }


                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });










            }
        });
        return view;
    }

    private void setUpViews(View view) {
         emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
         emailEditText = view.findViewById(R.id.emailEditText);
        passwordTextInputLayout= view.findViewById(R.id.passwordTextInputLayout);
         passwordEditText= view.findViewById(R.id.passwordEditText);
         confirmPasswordTextInputLayout= view.findViewById(R.id.confirmPasswordTextInputLayout);
         confirmPasswordEditText= view.findViewById(R.id.confirmPasswordEditText);
         signUpButton= view.findViewById(R.id.signupBtn);
         goToSignInTV= view.findViewById(R.id.goToSignInTV);
    }

    private void initFireStore() {

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private  boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }




}
