//package com.example.smartdiary;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class navActivity extends AppCompatActivity {
//    FirebaseAuth auth;
//    Button button;
//    FirebaseUser user;
//    TextView textView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.nav_drawer);
//
//        auth=FirebaseAuth.getInstance();
//        button=findViewById(R.id.logout);
//        textView=findViewById(R.id.user_details);
//
//        user=auth.getCurrentUser();
//        if(user==null){
//            openMainActivity2();
//            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//            startActivity(intent);
//            finish();

//
//        }
//        else {
//            textView.setText(user.getEmail());
//        }
//        button.setOnClickListener(new View.OnClickListener(){
//           @Override
//           public void onClick(View view) {
//           FirebaseAuth.getInstance().signOut();
//               openMainActivity2();
//               Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//               startActivity(intent);
//               finish();

//
//           }
//        });
//
//    }
//    public void openMainActivity2() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
//}