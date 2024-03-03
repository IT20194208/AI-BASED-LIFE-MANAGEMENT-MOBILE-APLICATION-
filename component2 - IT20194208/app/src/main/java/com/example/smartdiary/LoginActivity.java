package com.example.smartdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextInputEditText editTextEmail,editTextPassword;

    // google authentication
    ImageView googleAuth;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;


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
        setContentView(R.layout.login);

        //google authentication
        googleAuth=findViewById(R.id.btnGoogle);
        auth =FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        googleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               googleSignIn();
            }
        });


        mAuth=FirebaseAuth.getInstance();
        editTextEmail= findViewById(R.id.email);
        editTextPassword= findViewById(R.id.password);
        progressBar= findViewById(R.id.progressBar);
        button = findViewById(R.id.buttonLogin);
        button2 = findViewById(R.id.button4);

       // FirebaseAuth.getInstance().signOut();
      //  Log.d("FirebaseAuth", FirebaseAuth.getInstance().getCurrentUser().getUid());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password,username;
                email= String.valueOf(editTextEmail.getText());
                password= String.valueOf(editTextPassword.getText());


//                if(TextUtils.isEmpty(email)){
//                    Toast.makeText(LoginActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(password)){
//                    Toast.makeText(LoginActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                 mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                    openMenuActivity();
                                } else {

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

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
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    // google authentication
    private void googleSignIn(){
         Intent intent= mGoogleSignInClient.getSignInIntent();
         startActivityForResult(intent,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,@NonNull Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
               GoogleSignInAccount account= task.getResult(ApiException.class);
               firebaseAuth(account.getIdToken());
            }catch(Exception e){
               Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void firebaseAuth(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user= auth.getCurrentUser();

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("id",user.getUid());
                            map.put("name",user.getDisplayName());
                            map.put("profile",user.getPhotoUrl().toString());

                            database.getReference().child("user").child(user.getUid()).setValue(map);
                             Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                             startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}