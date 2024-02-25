package com.example.smartdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class dailydiaryActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;


    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private CardView cardView5;
    private CardView cardView6;
    private CardView cardView7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailydiary);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        smartdiary=findViewById(R.id.smartdiary);
        dailydiary=findViewById(R.id.dailydiary);
        makeroutine=findViewById(R.id.makeroutine);
        improveday=findViewById(R.id.improveday);
       // notification=findViewById(R.id.notification);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailydiaryActivity.this,HomeActivity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailydiaryActivity.this,CardActivity2.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailydiaryActivity.this,dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailydiaryActivity.this,CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailydiaryActivity.this,dailydiaryActivity.class);
            }
        });


        cardView1 = findViewById(R.id.cardmon);
        cardView2 = findViewById(R.id.cardtue);
        cardView3 = findViewById(R.id.cardwen);
        cardView4= findViewById(R.id.cardthu);
        cardView5= findViewById(R.id.cardfri);
        cardView6= findViewById(R.id.cardsat);
        cardView7= findViewById(R.id.cardsun);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity1();
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity2();
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity3();
            }
        });
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity4();
            }
        });
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity5();
            }
        });
        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity6();
            }
        });
        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCardActivity7();
            }
        });



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


    private void openCardActivity1() {
        Intent intent = new Intent(this, mondayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity2() {
        Intent intent = new Intent(this, tuesdayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity3() {
        Intent intent = new Intent(this, wednsdayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity4() {
        Intent intent = new Intent(this, thursdayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity5() {
        Intent intent = new Intent(this, fridayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity6() {
        Intent intent = new Intent(this, saturdayActivity.class);
        startActivity(intent);
    }
    private void openCardActivity7() {
        Intent intent = new Intent(this, sundayActivity.class);
        startActivity(intent);
    }

}