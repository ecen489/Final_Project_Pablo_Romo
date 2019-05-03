package com.example.final_project_pablo_romo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;

    String weekDay;

    FirebaseAuth mAuth;

    // TextViews
    TextView day;
    TextView bitem;
    TextView bcals;
    TextView bprotein;
    TextView bcarbs;
    TextView bfat;

    TextView litem;
    TextView lcals;
    TextView lprotein;
    TextView lcarbs;
    TextView lfat;

    TextView ditem;
    TextView dcals;
    TextView dprotein;
    TextView dcarbs;
    TextView dfat;

    TextView snack1;
    TextView snack2;
    TextView snack3;
    TextView snack4;
    TextView snack5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Day settings
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        day = findViewById(R.id.day);
        day.setText(weekDay);

        // Set the TextViews for Breakfast
        bitem = findViewById(R.id.breakfastitem);
        bcals = findViewById(R.id.fwBcals);
        bprotein = findViewById(R.id.bprotein);
        bcarbs = findViewById(R.id.bcarbs);
        bfat = findViewById(R.id.bfat);

        // Set the TextViews for Lunch
        litem = findViewById(R.id.lunchitem);
        lcals = findViewById(R.id.lcals);
        lprotein = findViewById(R.id.lprotein);
        lcarbs = findViewById(R.id.lcarbs);
        lfat = findViewById(R.id.lfat);

        // Set the TextViews for Dinner
        ditem = findViewById(R.id.dinnertiem);
        dcals = findViewById(R.id.dcals);
        dprotein = findViewById(R.id.dprotein);
        dcarbs = findViewById(R.id.dcarbs);
        dfat = findViewById(R.id.dfat);

        // Set the TextViews for Snacks
        snack1 = findViewById(R.id.snack1);
        snack2 = findViewById(R.id.snack2);
        snack3 = findViewById(R.id.snack3);
        snack4 = findViewById(R.id.snack4);
        snack5 = findViewById(R.id.snack5);

        // Database things
        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();
        mAuth = FirebaseAuth.getInstance();

        toaster(mAuth.getCurrentUser().getEmail());

        //DatabaseReference breakfastkey = dbrf.child("MealPreppersDatabase/FoodWeeks");

        //Query bquery = breakfastkey.orderByChild("user");
        //bquery.addListenerForSingleValueEvent(breakfastlistener);
        /*
        DatabaseReference newFW = breakfastkey.push();

        String[] snacks = {"PBJ","Banana","Trail Mix"};
        List snacklist = new ArrayList<String>(Arrays.asList(snacks));

        newFW.child("user").setValue(1);
        newFW.child("name").setValue("example1");

        newFW.child("Monday").child("Breakfast").setValue("Eggs");
        newFW.child("Monday").child("Lunch").setValue("Cheapotle");
        newFW.child("Monday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Monday").child("Snacks").setValue(snacklist);

        newFW.child("Tuesday").child("Breakfast").setValue("Eggs");
        newFW.child("Tuesday").child("Lunch").setValue("Cheapotle");
        newFW.child("Tuesday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Tuesday").child("Snacks").setValue(snacklist);

        newFW.child("Wednesday").child("Breakfast").setValue("Eggs");
        newFW.child("Wednesday").child("Lunch").setValue("Cheapotle");
        newFW.child("Wednesday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Wednesday").child("Snacks").setValue(snacklist);

        newFW.child("Thursday").child("Breakfast").setValue("Eggs");
        newFW.child("Thursday").child("Lunch").setValue("Cheapotle");
        newFW.child("Thursday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Thursday").child("Snacks").setValue(snacklist);

        newFW.child("Friday").child("Breakfast").setValue("Eggs");
        newFW.child("Friday").child("Lunch").setValue("Cheapotle");
        newFW.child("Friday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Friday").child("Snacks").setValue(snacklist);

        newFW.child("Saturday").child("Breakfast").setValue("Eggs");
        newFW.child("Saturday").child("Lunch").setValue("Cheapotle");
        newFW.child("Saturday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Saturday").child("Snacks").setValue(snacklist);

        newFW.child("Sunday").child("Breakfast").setValue("Eggs");
        newFW.child("Sunday").child("Lunch").setValue("Cheapotle");
        newFW.child("Sunday").child("Dinner").setValue("Teriyaki Tofu");
        newFW.child("Sunday").child("Snacks").setValue(snacklist);
        */
    }

    ValueEventListener breakfastlistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodWeek currfw = snapshot.getValue(FoodWeek.class);
                    bitem.setText(currfw.Tuesday.Breakfast.name);
                    toaster("Made it to the Breakfastlistener");
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void out(View view) {
        // Signs the user out
        mAuth.signOut();

        Intent main = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(main);
    }

    public void gotofoodweek(View view) {
        // Moves the user to the FoodWeekActivity
        Intent foodweek = new Intent(SplashActivity.this,FoodWeekActivity.class);
        startActivity(foodweek);
    }

    public void gotoshoplist(View view) {
        // Moves the user to the ShopListActivity
        Intent shop = new Intent(SplashActivity.this,ShopListActivity.class);
        startActivity(shop);
    }

    public void gotoprices(View view) {
        // Moves the user to the PriceActivity
        Intent price = new Intent(SplashActivity.this,PriceActivity.class);
        startActivity(price);
    }

    public void gotocreate(View view) {
        // Moves the user to the CreateNewItemActivity
        Intent create = new Intent(SplashActivity.this,CreateNewItemActivity.class);
        startActivity(create);
    }

    public void toaster(String s) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
    }
}
