package com.example.letshop.Sellers.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.letshop.MainActivity;
import com.example.letshop.R;
import com.google.firebase.auth.FirebaseAuth;


public class LogoutFragment extends Fragment {

    private LogoutViewModel logoutViewModel;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        logoutViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//
        InitialUI();
        mAuth.signOut();

        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
        return root;
    }

    private void InitialUI() {
        mAuth = FirebaseAuth.getInstance();
    }
}