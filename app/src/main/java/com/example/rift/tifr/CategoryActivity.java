package com.example.rift.tifr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.rift.tifr.Adapter.CustomAdapter;
import com.example.rift.tifr.Event.Event;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "Category Activity";
    private static int CODE = 1; //declare as FIELD
    //Declare the adapter, RecyclerView and our custom ArrayList
    RecyclerView recyclerView;
    CustomAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        this.getSupportActionBar().setTitle(intent.getStringExtra("categoryObject"));

        final ArrayList<Event> events = (ArrayList<Event>) intent.getSerializableExtra("eventObject");
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
        adapter = new CustomAdapter(CategoryActivity.this,events,0);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

}
