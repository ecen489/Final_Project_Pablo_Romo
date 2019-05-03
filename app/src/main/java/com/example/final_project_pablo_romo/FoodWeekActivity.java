package com.example.final_project_pablo_romo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class FoodWeekActivity extends AppCompatActivity {

    private RecyclerView FoodWeekRecycler;
    FoodWeek FW = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_week);

        FoodWeekRecycler = findViewById(R.id.FoodWeekRecycler);
        FWRecyclerAdapter adapter = new FWRecyclerAdapter(this,FW);
        FoodWeekRecycler.setAdapter(adapter);
        FoodWeekRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
