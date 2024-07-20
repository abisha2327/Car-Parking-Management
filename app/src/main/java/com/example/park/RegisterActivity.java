package com.example.park;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, confirm, platenumber;
    Button register;

    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://parksavvy-7dd4f-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.password1);
        platenumber = findViewById(R.id.numplate);
        register = findViewById(R.id.loginbutton);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("User Data");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = username.getText().toString();
                String e = email.getText().toString();
                String p = password.getText().toString();
                String c = confirm.getText().toString();
                String a = platenumber.getText().toString();

                if (n.isEmpty()) {
                    username.setError("Please enter your name");
                } else if (e.isEmpty()) {
                    email.setError("Please enter email");
                } else if (a.isEmpty()) {
                    platenumber.setError("Please enter your number");
                } else if (p.isEmpty()) {
                    password.setError("Please enter password");
                } else if (c.isEmpty()) {
                    confirm.setError("please confirm password");
                } else {
                    // Check if platenumber exists in the database
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(a)) {
                                Toast.makeText(RegisterActivity.this, "platenumber is registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // Store user data in the database
                                DatabaseReference userRef = databaseReference.child("users").child(a);
                                userRef.child("username").setValue(n);
                                userRef.child("email").setValue(e);
                                userRef.child("password").setValue(p);
                                userRef.child("confirm").setValue(c);
                                userRef.child("platenumber").setValue(a);

                                // Create user with email and password
                                auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent i = new Intent(getApplicationContext(), sendotp.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}