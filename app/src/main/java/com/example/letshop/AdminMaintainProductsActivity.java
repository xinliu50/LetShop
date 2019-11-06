package com.example.letshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn;
    private EditText name, price, description;
    private ImageView imageView;
    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        InitialUI();

        displayProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
    }

    private void InitialUI() {
        applyChangesBtn = (Button)findViewById(R.id.ally_changes_btn);
        name = (EditText)findViewById(R.id.product_name_maintain);
        price = (EditText)findViewById(R.id.product_Price_maintain);
        description = (EditText)findViewById(R.id.product_Description_maintain);
        imageView = (ImageView) findViewById(R.id.product_image_maintain);


        productID = getIntent().getStringExtra("pid");
        Log.d("status","pid"+productID);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
    }

    private void displayProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPice);
                    description.setText(pDescription);

                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if(TextUtils.isEmpty(pName)){
            Toast.makeText(AdminMaintainProductsActivity.this,"Please Write Product Name",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pPrice)){
            Toast.makeText(AdminMaintainProductsActivity.this,"Please Write price of the product",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(pDescription)){
            Toast.makeText(AdminMaintainProductsActivity.this,"Please Write description of the product",Toast.LENGTH_LONG).show();
        }else{

            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("pid",productID);
            productMap.put("description",pDescription);
           // productMap.put("image",downloadImageUrl);
            //productMap.put("category",CategoryName);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,"Changes applied...",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdminMaintainProductsActivity.this,AdminHomeActivity.class));
                        finish();
                    }
                }
            });
        }
    }

}
