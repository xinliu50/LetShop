package com.example.letshop.Buyers.ui.productDetails;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.letshop.Model.Products;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.R;
import com.example.letshop.Buyers.ui.items.ItemsFragment;
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

public class productDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    private View root;


    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription,productName;
    private String productID = "", state = "Normal";
    private Button addToCartButton;

    public static productDetailsFragment newInstance() {
        return new productDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.product_details_fragment, container, false);

       InitialUI();

       getProductDetails(productID);

       addToCartButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(state.equals("Order Placed") || state.equals("Order Shipped")){
                   Toast.makeText(getActivity(),"You can order more product once your order is confirmed",Toast.LENGTH_LONG).show();
               }else{
                   addingToCartList();
               }
           }
       });
       CheckOrderState();
       return root;
    }

    private void InitialUI() {
//        addToCartBtn = (FloatingActionButton)root.findViewById(R.id.add_product_to_cart_btn);
        numberButton = (ElegantNumberButton)root.findViewById(R.id.number_btn);
        productImage = (ImageView)root.findViewById(R.id.product_image_details);
        productName = (TextView)root.findViewById(R.id.product_name_details);
        productDescription = (TextView)root.findViewById(R.id.product_description_details);
        productPrice = (TextView)root.findViewById(R.id.product_price_details);
        addToCartButton = (Button)root.findViewById(R.id.pd_add_to_cart_button);


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


    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentDate = currentDate.format(calForDate.getTime());
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart_List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User_View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin_View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(),"Added to Cart List",Toast.LENGTH_LONG).show();

                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                transaction.replace(R.id.nav_host_fragment, new ItemsFragment());
                                                transaction.addToBackStack(null);
                                                transaction.commit();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped")){
                        state = "Order Shipped";

                    }else if(shippingState.equals("not shipped")){
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
