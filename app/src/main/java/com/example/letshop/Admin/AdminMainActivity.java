package com.example.letshop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letshop.MainActivity;
import com.example.letshop.R;
import com.example.letshop.ResetPasswordActivity;
import com.example.letshop.Sellers.SellerProductCategoryActivity;

import io.paperdb.Paper;

public class AdminMainActivity extends AppCompatActivity {
    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn,SettingsBtn,checkApproveProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        InitialUI();
    }

    private void InitialUI() {

        LogoutBtn = (Button)findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button)findViewById(R.id.check_order_btn);
        maintainProductsBtn = (Button)findViewById(R.id.maintain_btn);
        SettingsBtn = (Button)findViewById(R.id.admin_settings);
        checkApproveProductsBtn = (Button)findViewById(R.id.check_approve_products_btn);

        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminMainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, AdminHomeActivity.class));
            }
        });

        checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, AdminCheckNewProductsActivity.class));
            }
        });
    }
}
