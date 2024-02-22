package com.example.smartdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CardActivity2 extends AppCompatActivity {

    // Declare selectedDate as a class-level variable
    private String selectedDate;
    private Button button;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, setting, notification, smartdiary, dailydiary, makeroutine, improveday;
    // Add a FirebaseFirestore instance
    FirebaseDatabase database;
    private DatabaseReference diaryReference;
    // Assuming you have FirebaseAuth instance
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Retrieve the currently signed-in user
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailytext);
        TextInputEditText text = findViewById(R.id.textView5);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);



        button = findViewById(R.id.button2);
        // Initialize FirebaseDatabase
        database = FirebaseDatabase.getInstance();

        // Reference to the "diary" node in the database
        if (currentUser != null) {
            // Get the user's UID
            String uid = currentUser.getUid();

            // Initialize diaryReference when user is not null
            diaryReference = database.getReference("diary").child(uid);
        }

        // Reference to the "diary" node in the database
        //diaryReference = database.getReference("daily text");
        // diaryReference = database.getReference("dairy/"+"/"+uid+"/"+selectedDate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrRetrieveData();
                String inputText = text.getText().toString().trim();
                Log.d("CardActivity2", "Input Text: " + inputText);
                if (!inputText.isEmpty()) {
                    progressDialog.show();
                    new PostDataTask().execute(inputText);
                } else {
                    Toast.makeText(CardActivity2.this, "Please enter some text", Toast.LENGTH_SHORT).show();
                }

            }
        });


        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        smartdiary = findViewById(R.id.smartdiary);
        dailydiary = findViewById(R.id.dailydiary);
        makeroutine = findViewById(R.id.makeroutine);
        improveday = findViewById(R.id.improveday);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity2.this, HomeActivity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity2.this, CardActivity1.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity2.this, dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity2.this, CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity2.this, dailydiaryActivity.class);
            }
        });


        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar currdate = Calendar.getInstance();
        calendarView.setMaxDate(currdate.getTimeInMillis());

        int currentYear = currdate.get(Calendar.YEAR);
        int currentMonth = currdate.get(Calendar.MONTH);
        int currentDay = currdate.get(Calendar.DAY_OF_MONTH);
        String current_date_string = currentDay + "-" + (currentMonth + 1) + "-" + currentYear;
        selectedDate = current_date_string;
        getSelectedDateData();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                button.setEnabled(current_date_string.equals(selectedDate));

                // Handle the selected date
                Toast.makeText(CardActivity2.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();

                // Update diaryReference with the selectedDate
                if (currentUser != null) {
                    getSelectedDateData();
                }


            }
        });


//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMenuActivity();
//            }
//        });
    }

    private void getSelectedDateData() {
        Log.d("CardActivity2", "getSelectedDateData() called");
        DatabaseReference userDateReference = database.getReference("diary")
                .child(currentUser.getUid())
                .child(selectedDate);
        userDateReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CardActivity2", "onDataChange() called");
                // Check if data exists for the selected user and date
                if (snapshot.exists()) {
                    Log.d("CardActivity2", "Data snapshot exists");
                    DiaryEntry value = snapshot.getValue(DiaryEntry.class);
                    // Data exists, retrieve and display in input field
                    //  String existingData = snapshot.getValue(String.class);
                    displayExistingData(value.getText());
                } else {
                    Log.d("CardActivity2", "Data snapshot does not exist");
                    displayExistingData("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if necessary
                Log.e("CardActivity2", "Error retrieving data: " + error.getMessage());

                Toast.makeText(CardActivity2.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveOrRetrieveData() {
        // Check if selectedDate and currentUser are not null
        if (selectedDate != null && currentUser != null) {
            // Reference to the "diary" node for the specific user and date
            DatabaseReference userDateReference = database.getReference("diary")
                    .child(currentUser.getUid()).child(selectedDate);

            // Add a ValueEventListener to retrieve data
            userDateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("CardActivity2", "onDataChange() called");
                    Log.d("CardActivity2", "Selected Date: " + selectedDate);

                    // Check if data exists for the selected user and date
                    if (snapshot.exists()) {
                        Log.d("CardActivity2", "Data snapshot exists");

                        // Log the snapshot key and value for debugging purposes
                        Log.d("CardActivity2", "Snapshot Key: " + snapshot.getKey());
                        Log.d("CardActivity2", "Snapshot Value: " + snapshot.getValue());

                        DiaryEntry existingEntry = snapshot.getValue(DiaryEntry.class);
                        String existingText = existingEntry != null ? existingEntry.getText() : "";
                        CalendarView calendarView = findViewById(R.id.calendarView);
                        Calendar currdate = Calendar.getInstance();
                        calendarView.setMaxDate(currdate.getTimeInMillis());

                        int currentYear = currdate.get(Calendar.YEAR);
                        int currentMonth = currdate.get(Calendar.MONTH);
                        int currentDay = currdate.get(Calendar.DAY_OF_MONTH);
                        String current_date_string = currentDay + "-" + (currentMonth + 1) + "-" + currentYear;
                        // Display existing data for editing
                        if (selectedDate.equals(current_date_string)) {
                            saveDataToFirebase(userDateReference);
                        } else {
                            displayExistingData(existingText);
                        }
                    } else {
                        Log.d("CardActivity2", "Data snapshot does not exist");

                        // Data doesn't exist, proceed to save new data
                        saveDataToFirebase(userDateReference);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if necessary
                    Toast.makeText(CardActivity2.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void saveDataToFirebase(DatabaseReference userDateReference) {

        if (selectedDate == null) return;
        // Get the selected date from the CalendarView
        CalendarView calendarView = findViewById(R.id.calendarView);
        long selectedDateInMillis = calendarView.getDate();
        //String selectedDate = formatDate(selectedDateInMillis);

        // Get the text from the TextInputEditText
        TextInputEditText editText = findViewById(R.id.textView5);
        String inputText = editText.getText().toString();

        // Create a DiaryEntry object to be stored in Firebase
        DiaryEntry diaryEntry = new DiaryEntry(inputText, selectedDate);

        // Push the data to Firebase (creates a unique entry ID)
        userDateReference.setValue(diaryEntry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CardActivity2.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CardActivity2.this, "Error saving data", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void displayExistingData(String existingData) {
        // Display existing data in the TextInputEditText
        TextInputEditText editText = findViewById(R.id.textView5);
        editText.setText(existingData);

        // Inform the user that existing data is displayed
        Toast.makeText(CardActivity2.this, "Existing data for the selected date is displayed", Toast.LENGTH_SHORT).show();
    }


    // Helper method to format the date
    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    private class PostDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                Log.d("PostDataTask", "Starting doInBackground");
                URL url = new URL("https://asia-south1-life-management-app-417006.cloudfunctions.net/smartdiary");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Log.d("PostDataTask", "Connection opened");

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                Log.d("PostDataTask", "Request properties set");

                String jsonInputString = "{\"text\": \"" + params[0] + "\"}";
                Log.d("PostDataTask", "Request JSON: " + jsonInputString);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                    Log.d("PostDataTask", "Request sent");
                }
                StringBuilder responseBuilder = new StringBuilder(); // Declare responseBuilder here


                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
//                    StringBuilder responseBuilder = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseBuilder.append(responseLine.trim());
                    }
                    response = responseBuilder.toString();
                    Log.d("PostDataTask", "Response received: " + response);
                }
                conn.disconnect();


                // Save the response to Firebase Realtime Database
                saveResponseToDatabase(response);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("PostDataTask", "IOException: " + e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d("Response: ", "response" + result);
            // Pass the response to Mind_relaxplan_Activity
            Intent intent = new Intent(CardActivity2.this, dailytext2Activity.class);
            intent.putExtra("response", result);
            startActivity(intent);
        }
        private void saveResponseToDatabase(String response) {
            Log.d("PostDataTask", "Saving response to database...");
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String currentUserId = currentUser.getUid();

                // Get the reference to the database node where you want to store the response
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("ImproveDay").child(currentUserId);

                // Store the response under a specific node, for example "responses"
                userRef.child("responses").setValue(response)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("PostDataTask", "Response saved to database successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("PostDataTask", "Failed to save response to database: " + e.getMessage());
                            }
                        });
            }
        }
    }
}