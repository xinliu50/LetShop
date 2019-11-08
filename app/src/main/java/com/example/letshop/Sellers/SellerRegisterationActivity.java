package com.example.letshop.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letshop.R;

public class SellerRegisterationActivity extends AppCompatActivity {
    private Button sellerLoginBegin;

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
    }

    private void InitialUI() {
        sellerLoginBegin = findViewById(R.id.seller_already_have_account_btn);
    }

}
