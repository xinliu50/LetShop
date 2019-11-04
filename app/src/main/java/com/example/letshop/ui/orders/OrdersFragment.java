package com.example.letshop.ui.orders;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.R;
import com.example.letshop.ui.items.ItemsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    private View root;

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button ConfirmOrderBtn;
    private double totalAmount = 0.0;
    private TextView totalView;


    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.orders_fragment, container, false);
        InitialUI();
        return root;
    }

    private void InitialUI() {
        ConfirmOrderBtn = (Button)root.findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText)root.findViewById(R.id.shippment_name);
        phoneEditText = (EditText)root.findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText)root.findViewById(R.id.shippment_address);
        cityEditText = (EditText)root.findViewById(R.id.shippment_city);
        totalView = (TextView)root.findViewById(R.id.shippment_total);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            totalAmount = bundle.getDouble("Total Price");
            //totalAmount = bundle.getString("Total Price");
            Toast.makeText(getActivity(),"Total: $ " + totalAmount,Toast.LENGTH_LONG).show();
            totalView.setText("Your Total is $ "+totalAmount);
        }

        ConfirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(getActivity(),"Please provide your full name", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(getActivity(),"Please provide your Phone Number", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(getActivity(),"Please provide your Address", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(getActivity(),"Please provide your City", Toast.LENGTH_LONG).show();
        }else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {

        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentDate = currentDate.format(calForDate.getTime());
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String, Object> ordersMap =new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart_List")
                            .child("User_View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(),"Your final order has been placed successfully",Toast.LENGTH_LONG).show();

                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.nav_host_fragment, new ItemsFragment());
                                        transaction.commit();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        // TODO: Use the ViewModel
    }

}
