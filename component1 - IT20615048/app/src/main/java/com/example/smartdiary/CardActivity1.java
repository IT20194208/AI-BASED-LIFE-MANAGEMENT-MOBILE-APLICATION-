package com.example.smartdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CardActivity1 extends AppCompatActivity {
    private Button Submit;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdiary);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        Submit = findViewById(R.id.submit);
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
                redirectActivity(CardActivity1.this,HomeActivity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity1.this,CardActivity2.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity1.this,dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity1.this,CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(CardActivity1.this,dailydiaryActivity.class);
            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                if (validateAnswers()) {
                    progressDialog.show();
                    RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
                    int selectedRadioButtonId = radioGroup1.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    String answer1 = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";


                    RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
                    int selectedRadioButtonId2 = radioGroup2.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton2 = findViewById(selectedRadioButtonId2);
                    String answer2 = selectedRadioButton2 != null ? selectedRadioButton2.getText().toString() : "";

                    RadioGroup radioGroup3 = findViewById(R.id.radioGroup3);
                    int selectedRadioButtonId3 = radioGroup3.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton3 = findViewById(selectedRadioButtonId3);
                    String answer3 = selectedRadioButton3 != null ? selectedRadioButton3.getText().toString() : "";

                    RadioGroup radioGroup4 = findViewById(R.id.radioGroup4);
                    int selectedRadioButtonId4 = radioGroup4.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton4 = findViewById(selectedRadioButtonId4);
                    String answer4 = selectedRadioButton4 != null ? selectedRadioButton4.getText().toString() : "";

                    RadioGroup radioGroup5 = findViewById(R.id.radioGroup5);
                    int selectedRadioButtonId5 = radioGroup5.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton5 = findViewById(selectedRadioButtonId5);
                    String answer5 = selectedRadioButton5 != null ? selectedRadioButton5.getText().toString() : "";

                    RadioGroup radioGroup6 = findViewById(R.id.radioGroup6);
                    int selectedRadioButtonId6 = radioGroup6.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton6 = findViewById(selectedRadioButtonId6);
                    String answer6 = selectedRadioButton6 != null ? selectedRadioButton6.getText().toString() : "";


                    // Log the answers
                    Log.d("Answers", "Answer 1: " + answer1);
                    Log.d("Answers", "Answer 2: " + answer2);
                    Log.d("Answers", "Answer 3: " + answer3);
                    Log.d("Answers", "Answer 1: " + answer4);
                    Log.d("Answers", "Answer 2: " + answer5);
                    Log.d("Answers", "Answer 3: " + answer6);


                    //send data
                    new SendDataTask().execute(answer1, answer2, answer3, answer4, answer5, answer6);
                } else {
                    Toast.makeText(CardActivity1.this, "Please answer all questions", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean validateAnswers() {
        RadioGroup[] radioGroups = {
                findViewById(R.id.radioGroup1),
                findViewById(R.id.radioGroup2),
                findViewById(R.id.radioGroup3),
                findViewById(R.id.radioGroup4),
                findViewById(R.id.radioGroup5),
                findViewById(R.id.radioGroup6)
        };
        for (RadioGroup radioGroup : radioGroups) {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                return false; // At least one question is not answered
            }
        }
        return true; // All questions are answered
    }

    private class SendDataTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String answer1 = params[0];
            String answer2 = params[1];
            String answer3 = params[2];
            String answer4 = params[3];
            String answer5 = params[4];
            String answer6 = params[5];


            String response = "";

            try {
                // Create connection
                URL url = new URL("https://asia-south1-life-management-app-417006.cloudfunctions.net/dailyrouting");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Prepare the data
                String data = "{\"msg\":\"" + answer1 + " " + answer2 + " " + answer3 + " " + answer4 + " " + answer5 + " " + answer6 + "\"}";
                Log.d("SendDataTask", "Data to be sent: " + data);
                // Send the data
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = data.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Get the response
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Read the response from the server
                    StringBuilder responseBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                    }
                    response = responseBuilder.toString();
                } else {
                    response = "Error: " + connection.getResponseMessage();
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                response = "Error: " + e.getMessage();
            }

            return response;

        }



        @Override
        protected void onPostExecute(String result) {
            Log.d("SendDataTask", result);
            saveResponseToFirebase(result);

        }
    }
    private void saveResponseToFirebase(String response) {
        // Get current user ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Save response under routine_response node with the current user ID
        databaseReference.child("Routine_response").child(currentUserId).child("result").setValue(response);
        progressDialog.dismiss();
        openSmartDiaryActivity3();
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
    public void openMenuActivity() {
        Intent intent = new Intent(this, Smartdiary2Activity.class);
        startActivity(intent);
    }
    private void openSmartDiaryActivity3() {
        Intent intent = new Intent(CardActivity1.this, Smartdiary3Activity.class);
        startActivity(intent);
    }
}