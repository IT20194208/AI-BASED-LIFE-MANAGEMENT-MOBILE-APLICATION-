package com.example.smartdiary;

import static com.example.smartdiary.ChatGPTAPI.sendRequestToChatGPT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Smartdiary3Activity extends AppCompatActivity {

    private static final long INITIAL_BACKOFF_DELAY = 1000; // 1 second
    private static final double BACKOFF_MULTIPLIER = 2.0;
    private static final long MAX_BACKOFF_DELAY = 60000; // 60 seconds

    private long backoffDelay = INITIAL_BACKOFF_DELAY;
    private List<DiaryItem2> items = new LinkedList<>();
    private Button button,mon,tue,wed,thu,fri,sat,sun;
    private Button button2,customize;
    DrawerLayout drawerLayout;
    ImageView menu;
    private ProgressDialog progressDialog;
    LinearLayout home,setting,notification,smartdiary,dailydiary,makeroutine,improveday;
    private DatabaseReference RoutineRef;
    private DatabaseReference QuestionRef;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdiary3);
        mon= findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri= findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);


        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Monday");
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Tuesday");
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Wednesday");
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Thursday");
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Friday");
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Saturday");
            }
        });
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndSaveData("Sunday");
            }
        });



        //recycle view
        List<DiaryItem2> items = new LinkedList<>();
        items.add(new DiaryItem2("01what Lorem ipsum dolor sit amet consectetur adip", "7am-9am"));
        items.add(new DiaryItem2("02what Lorem ipsum dolor sit amet consectetur adip", "10am-11am"));
        items.add(new DiaryItem2("03what Lorem ipsum dolor sit amet consectetur adip", "12am-1pm"));
        items.add(new DiaryItem2("04what Lorem ipsum dolor sit amet consectetur adip", "1pm-2pm"));

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
// Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //RecyclerView recyclerView=findViewById(R.id.recyclerView2);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recycleviewAdapter2 adapter= new recycleviewAdapter2(items);
       // recyclerView.setAdapter(adapter);

        button = findViewById(R.id.button1);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu=findViewById(R.id.menu);
        home=findViewById(R.id.home);
        smartdiary=findViewById(R.id.smartdiary);
        dailydiary=findViewById(R.id.dailydiary);
        makeroutine=findViewById(R.id.makeroutine);
        improveday=findViewById(R.id.improveday);


        button = findViewById(R.id.button1);
        customize = findViewById(R.id.customize);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Note:Before the save you need to select date", Snackbar.LENGTH_SHORT).show();

                openMenuActivity();
            }
        });
        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity2();
            }
        });

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
                redirectActivity( Smartdiary3Activity.this,HomeActivity.class);
            }
        });
        smartdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( Smartdiary3Activity.this,CardActivity2.class);
            }
        });
        improveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( Smartdiary3Activity.this,dailytext2Activity.class);
            }
        });
        makeroutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( Smartdiary3Activity.this,CardActivity1.class);
            }
        });
        dailydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity( Smartdiary3Activity.this,dailydiaryActivity.class);
            }
        });

        // Initialize Firebase Database reference
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            RoutineRef = FirebaseDatabase.getInstance().getReference().child("Routine_response").child(currentUserId).child("result");
            fetchResponseFromFirebase();
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
                            List<DiaryItem2> items = new LinkedList<>();
                            for (String line : lines) {
                                String[] parts = line.split(" - ");
                                if (parts.length == 2) {
                                    String time = parts[0].trim();
                                    String text = parts[1].trim();
                                    items.add(new DiaryItem2(text, time));
                                }
                            }
                            // Set the list to the RecyclerView adapter
                            recycleviewAdapter2 adapter = new recycleviewAdapter2(items);
                            recyclerView.setAdapter(adapter);
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

    public void openMenuActivity() {
        Intent intent = new Intent(this, dailydiaryActivity.class);
        startActivity(intent);
    }


    private void fetchAndSaveData(final String day) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference()
                    .child("Routine_response").child(currentUserId).child("result");

            dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String data = dataSnapshot.getValue(String.class);
                    if (data != null) {
                        saveDataToFirebase(day, data);
                    } else {
                        Log.d("Firebase", "No data found for " + day);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("Firebase", "User is not logged in");
        }
    }
    private void saveDataToFirebase(String day, String data) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference dayNodeRef = FirebaseDatabase.getInstance().getReference()
                    .child(day).child(currentUserId).child("result");

            dayNodeRef.setValue(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "Data saved successfully for " + day);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Error saving data for " + day, e);
                        }
                    });
        } else {
            Log.e("Firebase", "User is not logged in");
        }
    }
    public void openMenuActivity2() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set your message here
        progressDialog.setCancelable(false); // Set if dialog is cancelable
        progressDialog.show();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference routineRef = FirebaseDatabase.getInstance().getReference()
                    .child("Routine_response").child(currentUserId).child("result");

            routineRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String data = dataSnapshot.getValue(String.class);
                    if (data != null) {
                        sendRequestAndSaveResponse(data);
                    } else {
                        Log.d("Firebase", "No data found for customization");
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data for customization: " + databaseError.getMessage());
                    dismissProgressDialog();
                }
            });
        } else {
            Log.e("Firebase", "User is not logged in");
            dismissProgressDialog();
        }
    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void sendRequestAndSaveResponse(final String requestData) {


        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create request body
        //RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonInput.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestData);


        // Create HTTP request
        Request request = new Request.Builder()
                .url("https://asia-south1-life-management-app-417006.cloudfunctions.net/OpenAI")
                .post(body)
                .build();

        // Execute request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    saveResponseToFirebase(responseData);
                } else {
                    Log.e("HTTP Request", "Failed to get response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("HTTP Request", "Request failed: " + e.getMessage());
            }
        });
    }
    private void saveResponseToFirebase(final String responseData) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            DatabaseReference customizeRef = FirebaseDatabase.getInstance().getReference()
                    .child("customize_response").child(currentUserId).child("result");

            customizeRef.setValue(responseData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "Customized response saved successfully");
                            dismissProgressDialog();
                            openSmartDiary4Activity();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Error saving customized response", e);
                            dismissProgressDialog();
                        }
                    });
        } else {
            Log.e("Firebase", "User is not logged in");
        }
    }
    private void openSmartDiary4Activity() {
        Intent intent = new Intent(this, Smartdiary4Activity.class);
        startActivity(intent);
    }
}