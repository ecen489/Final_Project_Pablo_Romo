package com.example.final_project_pablo_romo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;

public class CreateNewItemActivity extends AppCompatActivity {

    private String current_creation = "Ingredient";

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;
    FirebaseAuth mAuth;

    static final int CAPTURE_IMAGE_REQUEST = 1;
    Bitmap myBitmap;
    String mCurrentPhotoPath;

    File photoFile = null;
    ImageView img;

    private String barcode;
    private String setname;
    private String name;
    List<String> ingredients = new ArrayList<String>();
    List<String> meals = new ArrayList<String>();
    List<String> days = new ArrayList<>();
    List<String> foodweeks = new ArrayList<>();

    Button BG1;
    Button BG2;
    Button BG3;
    Button BG4;
    Button BG5;

    Button BM1;
    Button BM2;
    Button BM3;
    Button BM4;
    Button BM5;
    Button BM6;
    Button BM7;
    Button BM8;
    Button BM9;
    Button BM10;

    Button BreakBtn;
    Button LunchBtn;
    Button DineBtn;
    Button SnacksBtn;

    TextView select;

    private ScrollView G;
    private ScrollView S;
    private ScrollView M;
    private ScrollView D;
    private ScrollView W;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_item);

        G = findViewById(R.id.scrollforingredients);
        S = findViewById(R.id.scrollforsnacks);
        M = findViewById(R.id.scrollformeals);
        D = findViewById(R.id.scrollfordays);
        W = findViewById(R.id.scrollforweeks);

        G.setVisibility(View.VISIBLE);
        S.setVisibility(View.GONE);
        M.setVisibility(View.GONE);
        D.setVisibility(View.GONE);
        W.setVisibility(View.GONE);

        img = findViewById(R.id.barcodeimg);

        BG1 = findViewById(R.id.bg1);
        BG2 = findViewById(R.id.bg2);
        BG3 = findViewById(R.id.bg3);
        BG4 = findViewById(R.id.bg4);
        BG5 = findViewById(R.id.bg5);

        BM1 = findViewById(R.id.bm1);
        BM2 = findViewById(R.id.bm2);
        BM3 = findViewById(R.id.bm3);
        BM4 = findViewById(R.id.bm4);
        BM5 = findViewById(R.id.bm5);
        BM6 = findViewById(R.id.bm6);
        BM7 = findViewById(R.id.bm7);
        BM8 = findViewById(R.id.bm8);
        BM9 = findViewById(R.id.bm9);
        BM10 = findViewById(R.id.bm10);

        select = findViewById(R.id.selection);
        select.setText("New Ingredient");

        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference ikey = dbrf.child("MealPreppersDatabase/Ingredients");
        Query iQ = ikey.orderByChild("name");
        iQ.addListenerForSingleValueEvent(glistener);
    }

    public void ondone(View view) {
        // Sends data over to server and brings user back to SplashActivity

        switch(current_creation) {
            case "Ingredient" :
                databasechecker("Ingredients");
                break;
            case "Snack" :
                databasechecker("Snacks");
                break;
            case "Meal" :
                databasechecker("Meals");
                break;
            case "Day" :
                databasechecker("Days");
                break;
            case "Week" :
                databasechecker("FoodWeeks");
                break;
        }

        Intent splash = new Intent(CreateNewItemActivity.this,SplashActivity.class);
        startActivity(splash);
    }

    /////////////////////////INSERTION METHODS//////////////////////////////////////////////////////

    public void ingredientinsertion() {
        // Inserts all of the pertinent data for Ingredients

        // Instantiate all of the views
        EditText n = findViewById(R.id.Gname);
        EditText s = findViewById(R.id.serving);
        EditText c = findViewById(R.id.cals);
        EditText p = findViewById(R.id.protein);
        EditText car = findViewById(R.id.carbs);
        EditText f = findViewById(R.id.fat);

        // Get Data from views
        String name = n.getText().toString();
        String serving = s.getText().toString();
        int cals = Integer.parseInt(c.getText().toString());
        int protein = Integer.parseInt(p.getText().toString());
        int carbs = Integer.parseInt(car.getText().toString());
        int fat = Integer.parseInt(f.getText().toString());

        /*
        // Make a new Ingredient to insert easily into the firebase
        Ingredient i = new Ingredient(name,serving,barcode,cals,protein,carbs,fat);
        */

        // Creating entry to database
        DatabaseReference ingredientkey = dbrf.child("MealPreppersDatabase/Ingredients");
        DatabaseReference newG = ingredientkey.push();

        newG.child("name").setValue(name);
        newG.child("serving").setValue(serving);
        newG.child("cals").setValue(cals);
        newG.child("protein").setValue(protein);
        newG.child("carbs").setValue(carbs);
        newG.child("fat").setValue(fat);
        newG.child("barcode").setValue(barcode);
    }

    public void snackinsertion() {
        // Inserts all of the pertinent data for Snacks

        // Instantiate all of the Views
        EditText seto = findViewById(R.id.setname);

        // Get data from Views
        String s = seto.getText().toString();
        String n1 = BG1.getText().toString();
        String n2 = BG2.getText().toString();
        String n3 = BG3.getText().toString();
        String n4 = BG4.getText().toString();
        String n5 = BG5.getText().toString();

        DatabaseReference snackkey = dbrf.child("MealPreppersDatabase/Snacks");
        DatabaseReference newS = snackkey.push();

        newS.child("setname").setValue(s);
        newS.child("n1").setValue(n1);
        newS.child("n2").setValue(n2);
        newS.child("n3").setValue(n3);
        newS.child("n4").setValue(n4);
        newS.child("n5").setValue(n5);

    }

    public void mealinsertion() {
        // Inserts all of the pertinent data for Meals

        // Instantiate all of the Views
        EditText mea = findViewById(R.id.mealname);

        String i1 = BM1.getText().toString();
        String i2 = BM2.getText().toString();
        String i3 = BM3.getText().toString();
        String i4 = BM4.getText().toString();
        String i5 = BM5.getText().toString();
        String i6 = BM6.getText().toString();
        String i7 = BM7.getText().toString();
        String i8 = BM8.getText().toString();
        String i9 = BM9.getText().toString();
        String i10 = BM10.getText().toString();

        DatabaseReference mealkey = dbrf.child("MealPreppersDatabase/Meals");
        DatabaseReference meals = mealkey.push();

        meals.child("name").setValue(mea);
        meals.child("i1").setValue(i1);
        meals.child("i2").setValue(i2);
        meals.child("i3").setValue(i3);
        meals.child("i4").setValue(i4);
        meals.child("i5").setValue(i5);
        meals.child("i6").setValue(i6);
        meals.child("i7").setValue(i7);
        meals.child("i8").setValue(i8);
        meals.child("i9").setValue(i9);
        meals.child("i10").setValue(i10);
    }

    public void dayinsertion() {
        // Inserts all of the pertinent data for Days

        // Instantiate all of the Views
        EditText dayn = findViewById(R.id.dayname);

        String breakfast = BreakBtn.getText().toString();
        String lunch = LunchBtn.getText().toString();
        String dine = DineBtn.getText().toString();
        String snak = SnacksBtn.getText().toString();

        // use function to find the days with those names in database and get their object to insert

        DatabaseReference daykey = dbrf.child("MealPreppersDatabase/Meals");
        DatabaseReference days = daykey.push();

        days.child("name").setValue(dayn);
        days.child("Breakfast").setValue(breakfast);
        days.child("Lunch").setValue(lunch);
        days.child("Dinner").setValue(dine);
        days.child("Snacks").setValue(snak);
    }

    public void weekinsertion() {
        // Inserts all of the pertinent data for Weeks

        // Instantiate all of the Views

        // Use function to find the days with those names in database and get their objects to insert

        DatabaseReference fwkey = dbrf.push();
        DatabaseReference fw = fwkey.push();

        //fw.child();
    }

    ////////////////STUFF FOR CHECKING DATABASE FOR DIFFERENT TYPE OF INPUTS////////////////////////

    public void databasechecker(String s) {
        // checks to see if the database is clear to add a new Ingredient

        DatabaseReference ikey = dbrf.child("MealPreppersDatabase/" + s);

        switch(s) {
            case "Ingredients" :
                Query iQ = ikey.orderByChild("barcode");
                iQ.addListenerForSingleValueEvent(ingredientlistener);
                break;
            case "Snacks" :
                EditText set = findViewById(R.id.setname);
                setname = set.getText().toString();
                Query sQ = ikey.orderByChild("setname");
                sQ.addListenerForSingleValueEvent(snacklistener);
                break;
            case "Meals" :
                EditText editmeal = findViewById(R.id.mealname);
                name = editmeal.getText().toString();
                Query mQ = ikey.orderByChild("name");
                mQ.addListenerForSingleValueEvent(mlistener);
                break;
            case "Days" :
                EditText editday = findViewById(R.id.dayname);
                name = editday.getText().toString();
                Query dQ = ikey.orderByChild("name");
                dQ.addListenerForSingleValueEvent(dlistener);
                break;
            case "FoodWeeks" :
                EditText editfw = findViewById(R.id.fwname);
                name = editfw.getText().toString();
                Query fwQ = ikey.orderByChild("name");
                fwQ.addListenerForSingleValueEvent(fwlistener);
                break;
        }

    }

    ///////////////////////////////POPUP MENUS//////////////////////////////////////////////////////

    public void showIngredientPopup1(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG1.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIngredientPopup2(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG2.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIngredientPopup3(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG3.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIngredientPopup4(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG4.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIngredientPopup5(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    // These are the ingredients for the meals
    public void showIBMPopup1(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup2(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup3(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup4(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup5(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup6(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup7(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup8(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup9(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showIBMPopup10(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BG5.setText(item.toString());
                return false;
            }
        });
        for(String s : ingredients ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    // These are the meals and snacks for a day
    public void showBreakfastPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BreakBtn.setText(item.toString());
                return false;
            }
        });
        for(String s : meals ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showLunchPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LunchBtn.setText(item.toString());
                return false;
            }
        });
        for(String s : meals ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showDinnerPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DineBtn.setText(item.toString());
                return false;
            }
        });
        for(String s : meals ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    public void showSnacksPopup(View v) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SnacksBtn.setText(item.toString());
                return false;
            }
        });
        for(String s : meals ) {
            popup.getMenu().add(s);
        }
        popup.show();
    }

    ///////////////////////////VALUE EVENT LISTENERS////////////////////////////////////////////////

    ValueEventListener ingredientlistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredient g = snapshot.getValue(Ingredient.class);
                    if(!g.barcode.equals(barcode)) {
                        ingredientinsertion();
                    }
                }
            } else {
                ingredientinsertion();
            }
            toaster("Replacing the old Ingredient associated with this barcode!");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener snacklistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Snacks s = snapshot.getValue(Snacks.class);
                    if(!s.setname.equals(setname)) {
                        snackinsertion();
                    }
                }
            } else {
                snackinsertion();
            }
            toaster("Replacing the old Snacks associated with this setname!");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener glistener = new ValueEventListener() {
        // Adds ingredients from firebase to the ingredients List

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ingredient g = snapshot.getValue(Ingredient.class);
                    if(!g.name.equals("")) {
                        ingredients.add(g.name);
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener mlistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Meal m = snapshot.getValue(Meal.class);
                    if(!m.name.equals(name)) {
                        mealinsertion();
                    }
                }
            } else {
                mealinsertion();
            }
            toaster("Replacing the old Snacks associated with this setname!");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener dlistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Day d = snapshot.getValue(Day.class);
                    if(!d.name.equals(name)) {
                        dayinsertion();
                    }
                }
            } else {
                dayinsertion();
            }
            toaster("Replacing the old Snacks associated with this setname!");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener fwlistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodWeek fw = snapshot.getValue(FoodWeek.class);
                    if(!fw.name.equals(name)) {
                        snackinsertion();
                    }
                }
            } else {
                snackinsertion();
            }
            toaster("Replacing the old Snacks associated with this setname!");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ///////////////STUFF REGARDING TAKING PICTURE AND READING BARCODE///////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);
            readbarcode();
        } else {
            toaster("Request cancelled or something went wrong");
        }
    }

    private void captureImage() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
        else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null ) {

                // Create File where photo should go
                try{
                    photoFile = createImageFile();

                    if(photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,"com.example.mp9_pablo_romo", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                }
                catch (Exception ex) {
                    toaster(ex.getMessage());
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void getbarcode(View view) {
        // Lets user take a picture and then read the barcode within the picture
        captureImage();
    }

    public void readbarcode() {
        // Read what the barcode has and give the value to the user

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(myBitmap);

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector();

        Task<List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {

                        for (FirebaseVisionBarcode bar: barcodes) {
                            // See API reference for complete list of supported types
                            switch (bar.getFormat()) {
                                case 512 :
                                    // FORMAT_UPC_A
                                    toaster("Format" + Integer.toString(bar.getFormat()));
                                    toaster("rawValue is " + bar.getRawValue());
                                    barcode = bar.getRawValue();
                                    break;
                                case 1024 :
                                    // FORMAT_UPC_E
                                    toaster("Format" + Integer.toString(bar.getFormat()));
                                    toaster("rawValue is " + bar.getRawValue());
                                    barcode = bar.getRawValue();
                                    break;
                                default :
                                    toaster("Couldn't find anything");
                                    break;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        toaster("lol you failed");
                    }
                });
    }

    //////////////////////////SET VISIBILITY////////////////////////////////////////////////////////

    public void setIngredient(View view) {
        // Changes the Scrollview to scrollforingredients and sets current_creation

        TextView select = findViewById(R.id.selection);

        current_creation = "Ingredient";

        G.setVisibility(View.VISIBLE);
        S.setVisibility(View.GONE);
        M.setVisibility(View.GONE);
        D.setVisibility(View.GONE);
        W.setVisibility(View.GONE);

        select.setText("New Ingredient");
    }

    public void setSnack(View view) {
        // Changes the ScrollView to scrollforsnacks and sets current_creation

        TextView select = findViewById(R.id.selection);

        current_creation = "Snack";

        G.setVisibility(View.GONE);
        S.setVisibility(View.VISIBLE);
        M.setVisibility(View.GONE);
        D.setVisibility(View.GONE);
        W.setVisibility(View.GONE);

        select.setText("New Snacks");
    }

    public void setMeal(View view) {
        // Changes the Scrollview to scrollformeals and sets current_creation

        TextView select = findViewById(R.id.selection);

        current_creation = "Meal";

        G.setVisibility(View.GONE);
        S.setVisibility(View.GONE);
        M.setVisibility(View.VISIBLE);
        D.setVisibility(View.GONE);
        W.setVisibility(View.GONE);

        select.setText("New Meal");
    }

    public void setDay(View view) {
        // Changes the ScrollView to scrollfordays and sets current_creation

        TextView select = findViewById(R.id.selection);

        current_creation = "Day";

        G.setVisibility(View.GONE);
        S.setVisibility(View.GONE);
        M.setVisibility(View.GONE);
        D.setVisibility(View.VISIBLE);
        W.setVisibility(View.GONE);

        select.setText("New Day");
    }

    public void setWeek(View view) {
        // Changes the ScrollView to scrollforweeks and sets the current_creation

        TextView select = findViewById(R.id.selection);

        current_creation = "Week";

        G.setVisibility(View.GONE);
        S.setVisibility(View.GONE);
        M.setVisibility(View.GONE);
        D.setVisibility(View.GONE);
        W.setVisibility(View.VISIBLE);

        select.setText("New Week");
    }

    public void toaster(String s) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
    }
}
