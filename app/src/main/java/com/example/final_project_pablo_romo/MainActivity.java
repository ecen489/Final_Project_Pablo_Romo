package com.example.final_project_pablo_romo;
// Also known as the login activity, what the user sees first

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;

    FirebaseAuth mAuth;
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }

    public void loggingin(View view) {
        // Logs in the user and moves to the Splash Activity

        EditText username = findViewById(R.id.inputemail);
        EditText password = findViewById(R.id.inputpass);

        if(username.getText().toString().equals("")) {
            toaster("Please enter in a username");
        }
        if(password.getText().toString().equals("")) {
            toaster("Please enter in a password");
        } else {
            // Login the user
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                toaster("Login Successful!");
                                user = mAuth.getCurrentUser(); //The user is signed in

                                // Start Pull Activity
                                Intent splash = new Intent(MainActivity.this,SplashActivity.class);
                                startActivity(splash);
                            } else {
                                toaster("Something went wrong");
                            }
                        }
                    });
        }
    }

    public void signingup(View view) {
        // Signs the user up and moves to the Splash Activity

        EditText username = findViewById(R.id.inputemail);
        EditText password = findViewById(R.id.inputpass);

        if(username.getText().toString().equals("")) {
            toaster("Please enter in a username");
        }
        if(password.getText().toString().equals("")) {
            toaster("Please enter in a password");
        } else {
            mAuth.createUserWithEmailAndPassword(username.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                toaster("Signup Successful!");
                                user = mAuth.getCurrentUser(); //The newly created user is already signed in

                                // Start Pull Activity
                                Intent splash = new Intent(MainActivity.this,SplashActivity.class);
                                startActivity(splash);
                            }
                            else{
                                toaster("Something went wrong");
                            }
                        }
                    });
        }
    }

    public void toaster(String s) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show();
    }
}
