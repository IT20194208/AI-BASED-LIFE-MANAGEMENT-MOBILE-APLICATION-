package com.example.smartdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Smartdiary2Activity extends AppCompatActivity {
    private Button button;
    private Button button2;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdiary2);

        button = findViewById(R.id.button1);

        button2 = findViewById(R.id.button);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        smartdiary=findViewById(R.id.smartdiary);
        dailydiary=findViewById(R.id.dailydiary);
        makeroutine=findViewById(R.id.makeroutine);
        improveday=findViewById(R.id.improveday);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary2Activity.this,HomeActivity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary2Activity.this,CardActivity2.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary2Activity.this,dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary2Activity.this,CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary2Activity.this,dailydiaryActivity.class);
            }
        });


        button = findViewById(R.id.button1);
        button2 = findViewById(R.id.button);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openMenuActivity();
           }
       });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity2();
            }
        });

    }
   public void openMenuActivity() {
       Intent intent = new Intent(this, Smartdiary3Activity.class);
        startActivity(intent);
   }
    public void openMenuActivity2() {
        Intent intent = new Intent(this, CardActivity1.class);
        startActivity(intent);
    }
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);}
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}