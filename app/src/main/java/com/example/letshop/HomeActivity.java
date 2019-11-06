package com.example.letshop;

import android.content.Intent;
import android.os.Bundle;

import com.example.letshop.Model.Products;
import com.example.letshop.Prevalent.Prevalent;
import com.example.letshop.ViewHolder.ProductViewHolder;
import com.example.letshop.ui.cart.CartFragment;
import com.example.letshop.ui.productDetails.productDetailsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private View headerView;
    TextView userNameTextView;
    CircleImageView profileImageView;
    private DatabaseReference ProductRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawer;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_orders, R.id.nav_categories,
                R.id.nav_settings, R.id.nav_logout,R.id.nav_items,R.id.nav_search)
                .setDrawerLayout(drawer)
                .build();

        InitialUI();

//        Bundle bundle = new Bundle();
//        bundle.putString("key",type);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        navController.setGraph(R.navigation.mobile_navigation,bundle);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       // NavigationUI.setupActionBarWithNavController(this, navController, drawer);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_cart);
            }
        });
        if(Prevalent.currentOnlineUser != null) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

    }
    private void InitialUI() {
        userNameTextView = headerView.findViewById(R.id.user_profile_name);
        profileImageView = headerView.findViewById(R.id.user_profile_image);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        if(bundle != null){
//            type = getIntent().getExtras().get("Type").toString();
//        }

       // type = getIntent().getExtras().get("Type").toString();


       /* ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        navController.navigate(R.id.nav_items);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawer.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.items:
                navController.navigate(R.id.nav_items);
                break;

            case R.id.cart:
                navController.navigate(R.id.nav_cart);
                break;

            case R.id.orders:
                navController.navigate(R.id.nav_orders);
                break;
            case R.id.categories:
                navController.navigate(R.id.nav_categories);
                break;
            case R.id.settings:
                navController.navigate(R.id.nav_settings);
                break;
            case R.id.search:
                navController.navigate(R.id.nav_search);
                break;
            case R.id.logout:
                navController.navigate(R.id.nav_logout);
                break;

        }
        return true;

    }
}
