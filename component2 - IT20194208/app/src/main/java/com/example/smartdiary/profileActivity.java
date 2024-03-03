package com.example.smartdiary;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;


public class profileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView profilePic;
    Button updateProfileBtn;
    ProgressBar progressBar;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri imageUri; // To store the selected image URI
    FirebaseStorage storage;
    StorageReference storageReference;
    EditText mEmail;


    private void setUserData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user.getEmail() != null) {
            String email = user.getEmail();
            //set email
            mEmail.setText(email);
        }
        if (user.getDisplayName() != null) {
            String name = user.getDisplayName();
            //set user name
        }

        StorageReference imageRef = storageReference
                .child("profile_images/user_" + user.getUid() + ".jpg");
        // Use Glide to load and display the image
        imageRef.getDownloadUrl().
                addOnSuccessListener(uri ->
                {
                    String imageUrl = uri.toString();
                    Glide.with(this)
                            .load(imageUrl)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(profilePic);
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        profilePic = findViewById(R.id.profile_img_view);
        updateProfileBtn = findViewById(R.id.profile_update_btn);
        progressBar = findViewById(R.id.progressBar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mEmail = findViewById(R.id.profile_email);
        // Set up the ActivityResultLauncher for image picking

        setUserData();
        // Get the current user UID
        String uid = getCurrentUserUid();
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Handle the picked image URI
                        imageUri = result.getData().getData();
                        profilePic.setImageURI(imageUri);
                        // Save the image URI to SharedPreferences
                        saveImageUri(imageUri);
                        // Call the method to upload the image to Firebase
                        uploadImageToFirebase();
                        loadCircularImage(imageUri);
                    }
                });

        // Check if there is a previously uploaded image URI and load it using Glide
        imageUri = loadImageUri();
        if (imageUri != null) {
            loadCircularImage(imageUri);
        }

        updateProfileBtn.setOnClickListener(view -> pickImage());
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickLauncher.launch(intent);
    }

    private void loadCircularImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(profilePic);
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Get the UID of the current user
            String userUid = getCurrentUserUid();
            // Get the reference to the image file in Firebase Storage
            StorageReference imageRef = storageReference.child("profile_images/user_" + userUid + ".jpg");

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putFile(imageUri);

            // Register observers to listen for the upload process
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(profileActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                // Get the download URL (if needed) and load the image using Glide
                loadCircularImage(imageUri);
            }).addOnFailureListener(e -> {
                // Handle unsuccessful uploads
                Toast.makeText(profileActivity.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                // Track the progress of the upload
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            });
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveProfileImageUrl(userUid, imageUrl);
            });
        } else {
            // No image selected
            Toast.makeText(profileActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    // Save the image URL to SharedPreferences with user UID
    private void saveProfileImageUrl(String userUid, String imageUrl) {
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profileImageUrl_user_" + userUid, imageUrl);
        editor.apply();
    }

    private void saveImageUri(Uri uri) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("imageUri", uri.toString());
        editor.apply();
    }

    private Uri loadImageUri() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String uriString = prefs.getString("imageUri", null);
        if (uriString != null) {
            return Uri.parse(uriString);
        } else {
            return null;
        }
    }

    private String getCurrentUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            return user.getUid();
        } else {
            // Handle the case where the user is not authenticated
            return "";
        }
    }
}
