package com.example.letshop.Buyers.ui.items;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letshop.Model.Products;
import com.example.letshop.R;
import com.example.letshop.ViewHolder.ProductViewHolder;
import com.example.letshop.Buyers.ui.productDetails.productDetailsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemsFragment extends Fragment {

    private ItemsViewModel mViewModel;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private View root;
//    private String type = "";

    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = this.getArguments();
//        if(bundle != null){
//            type = bundle.getString("key");
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.items_fragment, container, false);
        InitialUI();
        DisplayItems();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ItemsViewModel.class);
        // TODO: Use the ViewModel
    }

    private void InitialUI() {
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = root.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void DisplayItems() {

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef.orderByChild("productState").equalTo("Approved"),Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                holder.txtProductDescription.setText(model.getDescription());

                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if(type.equals("Admin")){
//
//                            Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
//                            intent.putExtra("pid",model.getPid());
//                            startActivity(intent);
//                        }else {
                            Bundle bundle = new Bundle();
                            bundle.putString("pid", model.getPid());

                            Fragment productDetailFragment = new productDetailsFragment();
                            productDetailFragment.setArguments(bundle);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.nav_host_fragment, productDetailFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    //}
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
