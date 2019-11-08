package com.example.letshop.Buyers.ui.cart;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshop.Model.Cart;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.R;
import com.example.letshop.ViewHolder.CartViewHolder;
import com.example.letshop.Buyers.ui.orders.OrdersFragment;
import com.example.letshop.Buyers.ui.productDetails.productDetailsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private View root;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;
    private double overTotalPrice = 0.0;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.cart_fragment, container, false);
        InitialUI();

        CheckOrderState();
        DisplayList();
        return root;
    }

    private void InitialUI() {
        recyclerView = root.findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        NextProcessBtn = (Button)root.findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView)root.findViewById(R.id.total_price);
        txtMsg1 = (TextView)root.findViewById(R.id.msg1);

        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(overTotalPrice != 0){
                    Bundle bundle = new Bundle();
                    bundle.putDouble("Total Price",overTotalPrice);

                    Fragment orderFragment = new OrdersFragment();
                    orderFragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment, orderFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    Toast.makeText(getActivity(),"Order Cart is empty...",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

    }

    private void DisplayList() {

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart_List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User_View").child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity = "+model.getQuantity());
                holder.txtProductPrice.setText("Price "+model.getPrice()+"$");
                holder.txtProductName.setText(model.getPname());

                double oneTypeProductTPrice = ((Double.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                  "Edit",
                                  "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Cart Options:");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("pid",model.getPid());

                                    Fragment productDetailFragment = new productDetailsFragment();
                                    productDetailFragment.setArguments(bundle);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.nav_host_fragment, productDetailFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                                if(which == 1){
                                    cartListRef.child("User_View")
                                            .child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getActivity(),"Item removed...", Toast.LENGTH_LONG).show();

                                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                        transaction.replace(R.id.nav_host_fragment, new CartFragment());
                                                        transaction.addToBackStack(null);
                                                        transaction.commit();

                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        builder.show();
                    }
                });
                txtTotalAmount.setText("Total Price = $"+ String.valueOf(overTotalPrice));
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped")){
                        txtTotalAmount.setText("Dear "+userName+"\n order is shipped successfully..");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will receive your order");
                        NextProcessBtn.setVisibility(View.GONE);

                    }else if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Shipping state: Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
