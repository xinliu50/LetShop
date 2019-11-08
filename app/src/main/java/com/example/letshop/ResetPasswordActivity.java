package com.example.letshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshop.Admin.AdminHomeActivity;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.ui.settings.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitile, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;
    private String UserType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        InitialUI();
    }

    private void InitialUI() {
        check = getIntent().getStringExtra("check");
        pageTitile = findViewById(R.id.page_title);
        titleQuestions = findViewById(R.id.title_questions);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_btn);

        Paper.init(this);
        UserType = Paper.book().read(Prevalent.UserType);
    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if(check.equals("settings")){
            pageTitile.setText("Set Questions");
            titleQuestions.setText("Please set Answers for the following questions");
            verifyButton.setText("Set");
            displayPreviousAnswers();


            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                }
            });


        }else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
        }
    }

    protected void setAnswers(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();


        if(TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2)){
            Toast.makeText(ResetPasswordActivity.this,"Please answer both questions",Toast.LENGTH_LONG).show();
        }else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child(UserType).child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);

            ref.child("Security_Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this,"You have answer security questions successfully",Toast.LENGTH_LONG).show();
                                if(UserType.equals("Users")) {
                                    finish();
                                }
                                else if(UserType.equals("Admins")) {
                                    startActivity(new Intent(ResetPasswordActivity.this, AdminHomeActivity.class));
                                    finish();
                                }
                            }
                        }
                    });
        }
    }

    protected void displayPreviousAnswers(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child(UserType).child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security_Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
