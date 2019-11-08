package com.example.letshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshop.Admin.AdminCategoryActivity;
import com.example.letshop.Buyers.HomeActivity;
import com.example.letshop.Model.Users;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.Sellers.SellerRegisterationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitialUI();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SellerRegisterationActivity.class));
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        String UserType = Paper.book().read(Prevalent.UserType);

        if(UserPhoneKey != "" && UserPasswordKey != "" && UserType != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) && !TextUtils.isEmpty(UserType)){
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                AllowAccess(UserPhoneKey,UserPasswordKey,UserType);
            }
        }
    }

    private void InitialUI() {
        joinNowButton = (Button)findViewById(R.id.main_join_now_btn);
        loginButton = (Button)findViewById(R.id.main_login_btn);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
        sellerBegin = (TextView)findViewById(R.id.seller_begin);
    }

    private void AllowAccess(final String phone, final String password, final String type) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        final String userType = type;


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userType).child(phone).exists()){
                    Users usersData = dataSnapshot.child(userType).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this,"Logged in successfully ",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();

                            Prevalent.currentOnlineUser = usersData;
                            if(userType.equals("Users"))
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            else if(userType.equals("Admins"))
                                startActivity(new Intent(MainActivity.this, AdminCategoryActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this,"Password incorrect ",Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                        }
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Account with this "+phone+" doesn't exist!!",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
