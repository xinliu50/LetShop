package com.example.letshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn;
    private EditText name, price, description;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        InitialUI();
    }

    private void InitialUI() {
        applyChangesBtn = (Button)findViewById(R.id.ally_changes_btn);
        name = (EditText)findViewById(R.id.product_name_maintain);
        price = (EditText)findViewById(R.id.product_Price_maintain);
        description = (EditText)findViewById(R.id.product_Description_maintain);
        imageView = (ImageView) findViewById(R.id.product_image_maintain);
    }
}
