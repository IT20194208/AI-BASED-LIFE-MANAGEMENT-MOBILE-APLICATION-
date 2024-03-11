package com.example.smartdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public class Smartdiary4Activity extends AppCompatActivity  {
    private Button button;
    private Button savedb;
    DrawerLayout drawerLayout;
    ImageView menu;
    private Button selectDateButton;
    private TextView dateTextView;
    private List<DiaryItem> items;
    private recycleviewAdapter adapter;

    private DatabaseReference RoutineRef;
    private RecyclerView recyclerView;

    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;

    // recycle view
    DiaryItem[] data = {
            new DiaryItem("Enter your routine here", 7, 00,"07:00"),
            new DiaryItem("Enter your routine here", 7, 00,"07:00"),
            new DiaryItem("Enter your routine here", 7, 00,"07:00")
    };
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdiary4);


        // Initialize the items list
        items = new LinkedList<>();
        items.add(new DiaryItem("01what Lorem ipsum dolor sit amet consectetur adip", 7, 00,"07:00"));
        items.add(new DiaryItem("02what Lorem ipsum dolor sit amet consectetur adip", 10, 00,"07:00"));

        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recycleviewAdapter(items); // Assign to class-level variable
        recyclerView.setAdapter(adapter);

//        RecyclerView recyclerView=findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recycleviewAdapter adapter= new recycleviewAdapter(items);
//        recyclerView.setAdapter(adapter);

        findViewById(R.id.Add).setOnClickListener(view -> {
            items.add(data[counter%3]);
            counter++;
            adapter.notifyItemRemoved(items.size()-1);

        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            RoutineRef = FirebaseDatabase.getInstance().getReference().child("customize_response").child(currentUserId).child("result");
            fetchResponseFromFirebase();
        }
        button = findViewById(R.id.save);
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
                redirectActivity(Smartdiary4Activity.this,HomeActivity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary4Activity.this,CardActivity2.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary4Activity.this,dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary4Activity.this,CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Smartdiary4Activity.this,dailydiaryActivity.class);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected date
                String selectedDate = dateTextView.getText().toString();
                // Check if a date has been selected

                if (selectedDate.isEmpty()) {
                    // Inform the user to select a date
                    Toast.makeText(Smartdiary4Activity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else {
                    // Get current user ID
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = currentUser.getUid();


                    // Create a Firebase reference to the location where to store the data
                    DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference("reminder").child(userId).child(selectedDate);
                    // Clear existing data under the selected date
                    remindersRef.removeValue();

                    // Iterate through each item in the items list
                    for (DiaryItem item : items) {
                        String message = item.getText(); // Get message from the item
                        String selectedTime = item.getSelectedTime(); // Get selected time from the item

                        // Create a new child under the selected date, with time as the key
                        DatabaseReference timeRef = remindersRef.child(selectedTime);

                        // Set the message under the time node
                        timeRef.child("message").setValue(message);

                    }
                    // Set reminders after saving data to Firebase
                    for (DiaryItem item : items) {
                        String message = item.getText(); // Get message from the item
                        String selectedTime = item.getSelectedTime(); // Get selected time from the item

                        // Call setReminder() method to schedule reminders
                        setReminder(selectedDate, selectedTime, message);
                    }

                    // Inform the user that data has been saved successfully (Optional)
                    Toast.makeText(Smartdiary4Activity.this, "Data saved to Firebase", Toast.LENGTH_SHORT).show();
                }
                openMenuActivity();
                // Get selected date



            }
        });

        //date picker
        selectDateButton = findViewById(R.id.selectDateButton);
        dateTextView = findViewById(R.id.dateTextView);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //reminder
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("my notification","my notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Get current user ID
        FirebaseUser currentUser2 = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser2.getUid();

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference remindersRef = database.getReference("reminder").child(userId);

        // Assume selectedDate is the date you want to retrieve data for
        String selectedDate = dateTextView.getText().toString();

        // Attach a listener to the reminders node for the selected date
        remindersRef.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through each child under the selected date
                for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
                    String selectedTime = timeSnapshot.getKey(); // Get the selected time

                    // Get the message for the selected time
                    String message = timeSnapshot.child("message").getValue(String.class);

                    // Now you have retrieved the selected time and message, you can use them as needed
                    Log.d("FirebaseData", "Selected Time: " + selectedTime + ", Message: " + message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("FirebaseData", "Failed to retrieve data: " + databaseError.getMessage());
            }
        });





    }
    public void openMenuActivity() {
        // Get selected date
        String selectedDate = dateTextView.getText().toString();

        // Check if a date has been selected
        if (selectedDate.isEmpty()) {
            // Inform the user to select a date
            Toast.makeText(Smartdiary4Activity.this, "Please select a date", Toast.LENGTH_SHORT).show();
        } else {
            // Convert selected date to day name (e.g., Monday, Tuesday)
            String dayName = getDayName(selectedDate);

            // Get current user ID
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String userId = currentUser.getUid();

            // Create a Firebase reference to the location where to store the data
            DatabaseReference remindersRef = FirebaseDatabase.getInstance().getReference(dayName).child(userId).child("result");

            // Clear existing data under the selected date
            remindersRef.removeValue();

            // Construct the message string from items list
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("{\"msg\": \"Morning:\\n\\n");
            for (DiaryItem item : items) {
                String time = item.getSelectedTime();
                String text = item.getText();
                messageBuilder.append(time).append(" - ").append(text).append("\\n");
            }
            messageBuilder.append("\"}");
            // Store the JSON object under the appropriate node in Firebase

            // Store the message under the appropriate node in Firebase
            remindersRef.setValue(messageBuilder.toString());

            remindersRef.setValue(messageBuilder.toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Inform the user that data has been saved successfully
                            Toast.makeText(Smartdiary4Activity.this, "Data saved to Firebase", Toast.LENGTH_SHORT).show();

                            // Open the dailydiaryActivity activity page
                            Intent intent = new Intent(Smartdiary4Activity.this, dailydiaryActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to save data to Firebase
                            Toast.makeText(Smartdiary4Activity.this, "Failed to save data to Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private String getDayName(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parsedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Convert numerical representation of day to day name
            String[] dayNames = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            return dayNames[dayOfWeek - 1]; // Adjust for 0-based index
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void fetchResponseFromFirebase() {
        if (RoutineRef != null) {
            RoutineRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String response = dataSnapshot.getValue(String.class);
                    if (response != null) {
                        // Handle the response value here
                        Log.d("Response from Firebase", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("msg");
                            String[] lines = message.split("\n");
//                            List<DiaryItem> items = new LinkedList<>();
                            items.clear();
                            for (String line : lines) {
                                String[] parts = line.split(" - ");
                                if (parts.length == 2) {
                                    String text = parts[1].trim();
                                    items.add(new DiaryItem(text,7, 00,"07:00"));
                                }
                            }
                            // Set the list to the RecyclerView adapter
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //sendRequestToServer(response);
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
    private void showDatePickerDialog() {
        // Get current date to set as default date in the DatePickerDialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display the selected date in your TextView
                        String selectedDate = year+ "-" + (monthOfYear + 1) + "-" +dayOfMonth;
                        dateTextView.setText(selectedDate);


                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    //reminder
// Reminder setting method
    private void setReminder(String date, String selectedTime, String message) {
        if (!selectedTime.isEmpty()) {
            // Extract hour and minute from the selectedTime string
            String[] timeParts = selectedTime.split(":");
            int hourOfDay = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            // Split the date string into day, month, and year parts
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int dayOfMonth = Integer.parseInt(dateParts[2]);

            // Create a Calendar instance and set its date and time components
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // Create an intent for the AlarmReceiver class
            Intent intent = new Intent(Smartdiary4Activity.this, AlarmReceiver.class);
            intent.putExtra("message", message);

            // Generate a unique request code based on the selected time
            int uniqueId = hourOfDay * 60 + minute;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Smartdiary4Activity.this, uniqueId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Schedule the alarm
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}