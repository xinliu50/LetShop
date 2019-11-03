package com.example.letshop.ui.orders;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letshop.R;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        // TODO: Use the ViewModel
    }

}
