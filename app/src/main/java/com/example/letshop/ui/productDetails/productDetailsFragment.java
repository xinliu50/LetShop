package com.example.letshop.ui.productDetails;

import androidx.lifecycle.ViewModelProviders;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.letshop.Model.Products;
import com.example.letshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class productDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    private View root;

    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription,productName;
    private String productID = "";

    public static productDetailsFragment newInstance() {
        return new productDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.product_details_fragment, container, false);

       InitialUI();

       getProductDetails(productID);
       return root;
    }

    private void InitialUI() {
//        addToCartBtn = (FloatingActionButton)root.findViewById(R.id.add_product_to_cart_btn);
        numberButton = (ElegantNumberButton)root.findViewById(R.id.number_btn);
        productImage = (ImageView)root.findViewById(R.id.product_image_details);
        productName = (TextView)root.findViewById(R.id.product_name_details);
        productDescription = (TextView)root.findViewById(R.id.product_description_details);
        productPrice = (TextView)root.findViewById(R.id.product_price_details);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            productID = bundle.getString("pid");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
