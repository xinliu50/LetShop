package com.example.letshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountBtn;
    private EditText InputName, InputPhoneNumber, InputPassword, ConfirmPassword;
    private ProgressDialog loadingBar;
    final String TAG="RegisterActivity,status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitialUI();

        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void InitialUI() {
        CreateAccountBtn = (Button)findViewById(R.id.register_btn);
        InputName = (EditText)findViewById(R.id.register_username_input);
        InputPhoneNumber = (EditText)findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText)findViewById(R.id.register_password_input);
        ConfirmPassword = (EditText) findViewById(R.id.register_confirm_password_input);
        loadingBar = new ProgressDialog(this);
    }


    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password= InputPassword.getText().toString();
        String confirmPassword = ConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this,"Please write your username...",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your phone number",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this,"Please confirm your password",Toast.LENGTH_LONG).show();
        }else if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Passwords have to be match!",Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Congratulations! Your account has been created...",Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            }else{
                                Log.d(TAG,"Error Occurred..."+task.getException().getMessage());
                                Toast.makeText(RegisterActivity.this,"Error Occurred..."+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"Phone Number " + phone + " already exits",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Please try again use another phone number...",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
