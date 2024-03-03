package com.example.smartdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextInputEditText editTextEmail,editTextPassword,editTextUsername;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openMenuActivity();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth=FirebaseAuth.getInstance();
        button = findViewById(R.id.buttonsignup);
        button2 = findViewById(R.id.button4);
        editTextEmail= findViewById(R.id.email);
        editTextPassword= findViewById(R.id.password);
        editTextUsername= findViewById(R.id.username);
        progressBar= findViewById(R.id.progressBar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,username;
                email= String.valueOf(editTextEmail.getText());
                password= String.valueOf(editTextPassword.getText());
                username= String.valueOf(editTextUsername.getText());

//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(SignupActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // Validate email
                if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
//                if(TextUtils.isEmpty(password)){
//                    Toast.makeText(SignupActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // Validate password
                if (TextUtils.isEmpty(password) || password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
//                if(TextUtils.isEmpty(username)){
//                    Toast.makeText(SignupActivity.this,"Enter User name",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Validate username (add your own validation criteria)
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignupActivity.this, "Enter a username", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Get the current user
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Get the user's UID
                                    String userId = user.getUid();
                                    // Store user information in the Realtime Database
                                    storeUserInfo(userId, username, email);

                                    Toast.makeText(SignupActivity.this, "Account created",
                                            Toast.LENGTH_SHORT).show();
                                           openMenuActivity();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
//
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity2();
            }
        });
    }
    public void openMenuActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMainActivity2() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void storeUserInfo(String userId, String username, String email) {
        // Create a reference to the "user" node in the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");

        // Create a HashMap to store user information
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("name", username);
        userMap.put("email", email);
        // Add more fields as needed

        // Store the user information in the database under the user's UID
        userRef.child(userId).setValue(userMap);
    }

}