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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegisterationActivity extends AppCompatActivity {
    private Button sellerLoginBegin;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registeration);

        InitialUI();

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerRegisterationActivity.this,SellerLoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void InitialUI() {
        sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);
        registerButton = findViewById(R.id.seller_register_btn);

        nameInput = findViewById(R.id.seller_name);
        phoneInput = findViewById(R.id.seller_phone);
        emailInput = findViewById(R.id.seller_email);
        passwordInput = findViewById(R.id.seller_password);
        addressInput = findViewById(R.id.seller_address);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

    }

    private void registerSeller() {
        final String name = nameInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        final String address = addressInput.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(address)){
            Toast.makeText(SellerRegisterationActivity.this,"Please complete the registeration form...",Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait....");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final DatabaseReference root = FirebaseDatabase.getInstance().getReference();

                                String sid = mAuth.getCurrentUser().getUid();

                                HashMap<String, Object>sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);

                                root.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(SellerRegisterationActivity.this, "You are Registered successfully", Toast.LENGTH_LONG).show();

                                                    startActivity(new Intent(SellerRegisterationActivity.this, SellerLoginActivity.class));
                                                    finish();
                                                }else{
                                                    Toast.makeText(SellerRegisterationActivity.this, "Something wrong, try again", Toast.LENGTH_LONG).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                            }
                        }
                    });
            }
        }


}
