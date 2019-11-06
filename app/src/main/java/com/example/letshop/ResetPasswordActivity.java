package com.example.letshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitile, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;


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

    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if(check.equals("settings")){
            pageTitile.setText("Set Questions");
            titleQuestions.setText("Please set Answers for the following questions");
            verifyButton.setText("Set");

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer1 = question1.getText().toString();
                    String answer2 = question2.getText().toString();

                    if(TextUtils.isEmpty(answer1) || TextUtils.isEmpty(answer2)){
                        Toast.makeText(ResetPasswordActivity.this,"Please answer both questions",Toast.LENGTH_LONG).show();
                    }else{
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("")
                    }
                }
            });


        }else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
        }
    }
}
