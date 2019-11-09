package com.example.letshop.Sellers;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;

import com.example.letshop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class SellerHomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

//        Toolbar toolbar = findViewById(R.id.seller_toolbar);
//        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.seller_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_add, R.id.navigation_seller_logout)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_seller_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }



}
