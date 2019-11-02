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
import com.example.letshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class productDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    private View root;

    private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription,productName;

    public static productDetailsFragment newInstance() {
        return new productDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       root = inflater.inflate(R.layout.product_details_fragment, container, false);

       InitialUI();
       return root;
    }

    private void InitialUI() {
        addToCartBtn = (FloatingActionButton)root.findViewById(R.id.add_product_to_cart_btn);
        numberButton = (ElegantNumberButton)root.findViewById(R.id.number_btn);
        productImage = (ImageView)root.findViewById(R.id.product_image_details);
        productName = (TextView)root.findViewById(R.id.product_name_details);
        productDescription = (TextView)root.findViewById(R.id.product_description_details);
        productPrice = (TextView)root.findViewById(R.id.product_price_details);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}
