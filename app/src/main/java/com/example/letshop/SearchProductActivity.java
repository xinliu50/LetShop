package com.example.letshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SearchProductActivity extends AppCompatActivity {
    
//    private Button SearchBtn;
//    private EditText inputText;
//    private RecyclerView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        
        InitialUI();
    }

    private void InitialUI() {

//        SearchBtn = (Button)findViewById(R.id.search_btn);
//        inputText = (EditText)findViewById(R.id.search_product_name);
//        searchList = (RecyclerView) findViewById(R.id.search_list);
//        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));
    }
}

