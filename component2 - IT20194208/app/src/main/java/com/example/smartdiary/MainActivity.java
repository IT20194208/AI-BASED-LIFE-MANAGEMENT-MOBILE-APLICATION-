package com.example.smartdiary;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity();
            }
        });

        ImageSlider imageSlider = findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.slider01, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider02, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider03, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slider04, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);



//        ArrayList<SlideModel> imageList = new ArrayList<>(); // Create image list
//
//// imageList.add(new SlideModel("String Url" or R.drawable));
//// imageList.add(new SlideModel("String Url" or R.drawable, "title")); // You can add title
//
//        imageList.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ImageView.ScaleType.CENTER_CROP));
//        imageList.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct."));
//        imageList.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that."));
//
//        ImageSlider imageSlider = findViewById(R.id.image_slider);
//        imageSlider.setImageList(imageList);

//        ImageSlider imageSlider = findViewById(R.id.slider);

//        List<SlideModel> slideModels = new ArrayList<>();

//        slideModels.add(new SlideModel( ("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years."));
//        slideModels.add(new SlideModel( ("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct."));
//        slideModels.add(new SlideModel(("https://bit.ly/3fLJf72", "And people do that."));
//        imageSlider.setImageList(slideModels,true);


    }
    public void openMenuActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}