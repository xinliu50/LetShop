package com.example.letshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        InitialUI();
    }

    private void InitialUI() {
        CategoryName = getIntent().getExtras().get("category").toString();

        Toast.makeText(this,CategoryName,Toast.LENGTH_LONG).show();
    }
}
