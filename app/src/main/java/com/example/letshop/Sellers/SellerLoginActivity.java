package com.example.letshop.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        InitialUI();

        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeller();
            }
        });
    }

    private void InitialUI() {
        loginSellerBtn = findViewById(R.id.seller_login_btn);

        passwordInput = findViewById(R.id.seller_login_password);
        emailInput = findViewById(R.id.seller_login_email);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void loginSeller() {
        final String email = emailInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)){
            Toast.makeText(SellerLoginActivity.this,"Please complete the login form...",Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(SellerLoginActivity.this, SellerHomeActivity.class));
                        finish();
                    }else{
                        loadingBar.dismiss();
                    }
                }
            });

        }
    }
}
