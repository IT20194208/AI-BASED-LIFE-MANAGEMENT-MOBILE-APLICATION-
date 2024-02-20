package com.example.smartdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Calendar;

public class dailytext2Activity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;
    private DatabaseReference ImproveDayRef;

    TextView text1,text2,text3,text4;
    ImageView imamge01,imamge02,imamge03,imamge04;

    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailytext2);
        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);
        text3= findViewById(R.id.text3);
        text4= findViewById(R.id.text4);
        imamge01=findViewById(R.id.image01);
        imamge02=findViewById(R.id.image02);
        imamge03=findViewById(R.id.image03);
        imamge04=findViewById(R.id.image04);



        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        smartdiary=findViewById(R.id.smartdiary);
        dailydiary=findViewById(R.id.dailydiary);
        makeroutine=findViewById(R.id.makeroutine);
        improveday=findViewById(R.id.improveday);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("my notification","my notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailytext2Activity.this,HomeActivity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailytext2Activity.this,CardActivity1.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailytext2Activity.this,dailytext2Activity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailytext2Activity.this,CardActivity2.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(dailytext2Activity.this,dailydiaryActivity.class);
            }
        });
        // Initialize Firebase Database reference
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            ImproveDayRef = FirebaseDatabase.getInstance().getReference().child("ImproveDay").child(currentUserId).child("responses");
            fetchResponseFromFirebase();
            fetchreminderFirebase();
        }

    }
    public void Settext2(String response) {
        Log.d("Mind_relaxplan_Activity", "fetchAudioFiles: Response - " + response);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        switch (response) {
            case "{\"label\":\"ANGRY\"}":
                // Set text and image resources
                String angryVideoId = "r19_Dq3TOyM";
                loadVideo(angryVideoId);
                break;
            case "{\"label\":\"HAPPY\"}":
                // Set text and image resources
                String happyVideoId = "XLBKy7g3yTc";
                loadVideo(happyVideoId);
                break;
            case "{\"label\":\"SAD\"}":
                // Set text and image resources
                String SADVideoId = "hBzP8MtJf04";
                loadVideo(SADVideoId);
                break;
            case "{\"label\":\"GRATEFUL\"}":
                // Set text and image resources
                String GRATEFULVideoId = "Eyfa1yR8tx0";
                loadVideo(GRATEFULVideoId);
                break;
            case "{\"label\":\"DISAPPOINTEMENT\"}":
                // Set text and image resources
                String DISAPPOINTEMENTVideoId = "m5aCdGn0_7M";
                loadVideo(DISAPPOINTEMENTVideoId);
                break;
            case "{\"label\":\"SURPRISE\"}":
                // Set text and image resources
                String SURPRISE = "Y4ObnLRrklo";
                loadVideo(SURPRISE);
                break;
            case "{\"label\":\"ANXIOUS\"}":
                // Set text and image resources
                String ANXIOUS = "";
                loadVideo(ANXIOUS);
                break;
            case "{\"label\":\"JOYFUL\"}":
                // Set text and image resources
                String JOYFULVideoId = "Jf6CIruHdP4";
                loadVideo(JOYFULVideoId);
                break;
            case "{\"label\":\"SUICIDE\"}":
                // Set text and image resources
                String SUICIDEVideoId = "Tuw8hxrFBH8";
                loadVideo(SUICIDEVideoId);
                break;
            case "{\"label\":null}":
                // Set text and image resources
                String NULLVideoId = "tbnzAVRZ9Xc";
                loadVideo(NULLVideoId);
                break;
            default:
                Log.e("Settext", "Unexpected response: " + response);
                break;

        }
    }

    private void loadVideo(String videoId) {
        Log.d("YouTubePlayer", "Loading video with ID: " + videoId);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0);
            }
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                Log.e("YouTubePlayer", "Error loading video: " + error.name());
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
    private void fetchResponseFromFirebase() {
        if (ImproveDayRef != null) {
            ImproveDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String response = dataSnapshot.getValue(String.class);
                    if (response != null) {
                        // Handle the response value here
                        Log.d("Response from Firebase", response);
                        Settext(response);
                        Settext2(response);

                    } else {
                        Log.d("Response from Firebase", "No response found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase Database", "Error fetching response: " + databaseError.getMessage());
                }
            });
        } else {
            Log.d("Firebase Database", "musicRef is null");
        }
    }
    public void Settext(String response) {
        Log.d("Mind_relaxplan_Activity", "fetchAudioFiles: Response - " + response);

        switch (response) {
            case "{\"label\":\"ANGRY\"}":
                text1.setText("Pause and Breathe");
                text2.setText("Physical Activity");
                text3.setText("Take a Break");
                text4.setText("Practice Gratitude");
                imamge01.setImageResource(R.drawable.im1);
                imamge02.setImageResource(R.drawable.dailytext2);
                imamge03.setImageResource(R.drawable.dailytext3);
                imamge04.setImageResource(R.drawable.im2);


                ;

                break;
            case "{\"label\":\"HAPPY\"}":
                text1.setText("Healthy Breakfast");
                text2.setText("Morning Affirmations");
                text3.setText("Mindful Movement");
                text4.setText("Plan Your Day");
                imamge01.setImageResource(R.drawable.im3);
                imamge02.setImageResource(R.drawable.im1);
                imamge03.setImageResource(R.drawable.im4);
                imamge04.setImageResource(R.drawable.dailydiary);

                ;

                break;
            case "{\"label\":\"SAD\"}":
                text1.setText("Practice Gratitude");
                text2.setText("Morning Meditation");
                text3.setText("Positive Affirmations");
                text4.setText("Connect with Nature");
                imamge01.setImageResource(R.drawable.im2);
                imamge02.setImageResource(R.drawable.im4);
                imamge03.setImageResource(R.drawable.im5);
                imamge04.setImageResource(R.drawable.im6);

                ;

                break;
            case "{\"label\":\"DISAPPOINTEMENT\"}":
                text1.setText("Positive Affirmations");
                text2.setText("Practice Gratitude");
                text3.setText("Mindful Reflection");
                text4.setText("Engage in a Hobby");
                imamge01.setImageResource(R.drawable.im5);
                imamge02.setImageResource(R.drawable.im2);
                imamge03.setImageResource(R.drawable.im1);
                imamge04.setImageResource(R.drawable.dailytext2);

                ;

                break;
            case "{\"label\":\"GRATEFUL\"}":
                text1.setText("Mindful Breathing");
                text2.setText("Talk with friends");
                text3.setText("Express Kindness");
                text4.setText("Connect with Nature");
                imamge01.setImageResource(R.drawable.im1);
                imamge02.setImageResource(R.drawable.dailytext5);
                imamge03.setImageResource(R.drawable.im2);
                imamge04.setImageResource(R.drawable.im6);


                ;
                break;
            case "{\"label\":\"SURPRISE\"}":
                text1.setText("Plan Your Day");
                text2.setText("Take a Break");
                text3.setText("Express Kindness");
                text4.setText("Mindful Movement");
                imamge01.setImageResource(R.drawable.dailydiary);
                imamge02.setImageResource(R.drawable.dailytext3);
                imamge03.setImageResource(R.drawable.im2);
                imamge04.setImageResource(R.drawable.im4);


                ;
                break;
            case "{\"label\":\"ANXIOUS\"}":
                text1.setText("Healthy Breakfast");
                text2.setText("Plan Your Day");
                text3.setText("Engage in a Hobby");
                text4.setText("Mindful Movement");
                imamge01.setImageResource(R.drawable.im3);
                imamge02.setImageResource(R.drawable.dailydiary);
                imamge03.setImageResource(R.drawable.dailytext4);
                imamge04.setImageResource(R.drawable.im4);


                ;
                break;
            case "{\"label\":\"JOYFUL\"}":
                text1.setText("Healthy Breakfast");
                text2.setText("Engage in a Hobby");
                text3.setText("Plan Your Day");
                text4.setText("Take a Break");
                imamge01.setImageResource(R.drawable.im3);
                imamge02.setImageResource(R.drawable.dailytext4);
                imamge03.setImageResource(R.drawable.dailydiary);
                imamge04.setImageResource(R.drawable.im1);


                ;
                break;
            case "{ \"label\":null}":
                text1.setText("Healthy Breakfast");
                text2.setText("Engage in a Hobby");
                text3.setText("Plan Your Day");
                text4.setText("Take a Break");
                imamge01.setImageResource(R.drawable.im3);
                imamge02.setImageResource(R.drawable.dailytext3);
                imamge03.setImageResource(R.drawable.dailydiary);
                imamge04.setImageResource(R.drawable.im1);

                ;
                break;
            case "{\"label\":\"SUICIDE\"}":
                text1.setText("Healthy Breakfast");
                text2.setText("Engage in a Hobby");
                text3.setText("Plan Your Day");
                text4.setText("Take a Break");
                imamge01.setImageResource(R.drawable.im3);
                imamge02.setImageResource(R.drawable.dailytext3);
                imamge03.setImageResource(R.drawable.dailydiary);
                imamge04.setImageResource(R.drawable.im1);

                ;
                break;
            default:
                // Handle unexpected response values
                break;
        }


    }
    private void fetchreminderFirebase() {
        if (ImproveDayRef != null) {
            ImproveDayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String response = dataSnapshot.getValue(String.class);
                    if (response != null) {
                        // Handle the response value here
                        Log.d("Response from Firebase", response);
                        DatabaseReference nodeRef = null;
                        switch (response) {
                            case "{\"label\":\"HAPPY\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("HAPPY");

                                break;
                            case "{\"label\":\"SAD\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("SAD");
                                break;
                            case "{\"label\":\"SUICIDE\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("SUICIDE");
                                checkPermissionAndSendSMS();

                                break;
                            case "{\"label\":\"DISAPPOINTEMENT\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("DISAPPOINTEMENT");
                                break;
                            case "{\"label\":\"GRATEFUL\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("GRATEFUL");
                                break;
                            case "{\"label\":\"SURPRISE\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("SURPRISE");
                                break;
                            case "{\"label\":\"ANXIOUS\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("ANXIOUS");
                                break;
                            case "{\"label\":\"JOYFUL\"}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("JOYFUL");
                                break;
                            case "{ \"label\":null}":
                                nodeRef = FirebaseDatabase.getInstance().getReference().child("null");
                                break;

                        }
                        if (nodeRef != null) {
                            nodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
                                            String time = timeSnapshot.getKey();
                                            String message = timeSnapshot.child("text").getValue(String.class);
                                            // Log the retrieved reminder data
                                            Log.d("Reminder",  ", Time: " + time + ", Message: " + message);

                                            // Process the retrieved data
                                            // Here, you can set reminders using the retrieved time, label, and message
                                            setReminder(time, message);
                                        }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase Database", "Error fetching node data: " + databaseError.getMessage());
                                }
                            });
                        } else {
                            // Handle other response values
                            Settext(response);
                            Settext2(response);
                        }
                    } else {
                        Log.d("Response from Firebase", "No response found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase Database", "Error fetching response: " + databaseError.getMessage());
                }
            });
        } else {
            Log.d("Firebase Database", "ImproveDayRef is null");
        }
    }
    private void setReminder(String time, String message) {
        Log.d("setReminder", "Time received from Firebase: " + time);
        // Split the time string into hours and minutes
        String[] timeParts = time.split(":");
        if (timeParts.length != 2) {
            Log.e("setReminder", "Invalid time format: " + time);
            return;
        }

        try {
            int hourOfDay = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Convert date and time strings to Calendar object

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.MINUTE, 1);
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
//            calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3)));
//            Log.d("setReminder", "Setting reminder for " + hourOfDay + ":" + minute);

            // Create an intent for the AlarmReceiver class
            Intent intent = new Intent(dailytext2Activity.this, AlarmReceiver2.class);
            // Pass message to the intent
            intent.putExtra("text", message);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(dailytext2Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the alarm
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("setReminder", "Reminder set successfully.");
        } catch (NumberFormatException e) {
            Log.e("setReminder", "NumberFormatException: " + e.getMessage());
        }
    }

    private void checkPermissionAndSendSMS() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            sendMessage();
        }
    }
    private void sendMessage() {
        String message ="Your friend in SUICIDE emotion ";
        String phoneNumber = "+940765906262";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}